package kg.megacom.authorization.services.impl;

import kg.megacom.authorization.config.EmailSender;
import kg.megacom.authorization.exceptions.SignInErrorException;
import kg.megacom.authorization.models.dtos.AccountDto;
import kg.megacom.authorization.models.dtos.RoleDto;
import kg.megacom.authorization.models.dtos.UserCodeDto;
import kg.megacom.authorization.models.dtos.UserDto;
import kg.megacom.authorization.models.entities.Role;
import kg.megacom.authorization.models.entities.UserCode;
import kg.megacom.authorization.models.request.SignInRequest;
import kg.megacom.authorization.models.response.Message;
import kg.megacom.authorization.services.*;
import kg.megacom.authorization.utils.ConfirmCodeGenerator;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Service
public class AuthServiceImpl implements AuthService {
    @Autowired private UserService userService;
    @Autowired private AccountService accountService;
    @Autowired private RoleService roleService;
    @Autowired private EmailSender emailSender;
    @Autowired private ConfirmCodeGenerator confirmCodeGenerator;
    @Autowired private UserCodeService userCodeService;

    @Override
    public ResponseEntity<?> signIn(SignInRequest signInRequest) {
        AccountDto accountDto = accountService.findByEmail(signInRequest.getEmail());
        System.out.println("CHECK");
        if(accountDto != null){
            System.out.println("TEST");
            return new ResponseEntity<>(Message.of("Пользователь с данным email уже зарегистрирован"), HttpStatus.NOT_ACCEPTABLE);
        }

        String violationResponse = validateConstraints(signInRequest);
        if(violationResponse != null){
            return new ResponseEntity<>(Message.of(violationResponse), HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            RoleDto role = RoleDto.builder().role(signInRequest.getRoleType()).build();
            role = roleService.save(role);

            UserDto userDto = userService.save(UserDto.builder()
                    .email(signInRequest.getEmail())
                    .name(signInRequest.getName())
                    .phone(signInRequest.getPhone())
                    .role(role)
                    .confirm(false)
                    .build());

            accountService.save(AccountDto.builder()
                    .login(signInRequest.getEmail())
                    .password(signInRequest.getPassword())
                    .user(userDto)
                    .build());
            System.out.println("OK");
            //вызвать метод sendCode, он вернет код, который был сгенерирован и отправлен на почту
            ResponseEntity<?> codeSendResponse = sendCode(signInRequest.getEmail());

            if(!codeSendResponse.getStatusCode().equals(HttpStatus.OK)) return codeSendResponse;
            //Надо сохранить этот код в нашей базе. Для этого создаем новый объект UserCode
            UserCodeDto userCodeDto = UserCodeDto.builder()
                    .user(userDto)
                    .code(codeSendResponse.getBody().toString())
                    .sentDate(new Date())
                    .confirm(false)
                    .build();
            userCodeService.save(userCodeDto);
            // заполняем его поля
            //save UserCode - сохраняем
            return new ResponseEntity<>(Message.of("Вам отправлен код подтверждения на email"), HttpStatus.OK);
        }catch (Exception ex){
            return new ResponseEntity<>(Message.of(ex.getMessage()), HttpStatus.NOT_ACCEPTABLE);
           // throw new SignInErrorException(ex.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> logIn(String login, String password) {
        AccountDto accountDto = accountService.findByEmail(login);
        if(accountDto == null){
            return new ResponseEntity<>(Message.of("Не верный логин"), HttpStatus.NOT_ACCEPTABLE);
        }else if(!accountDto.getPassword().equals(password)){
            return new ResponseEntity<>(Message.of("Не верный пароль"), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Message.of("Добро пожаловать в систему"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> sendCode(String email) {
        //generate random code - рандомно сгеренрировать 4-х значное число
        String randCode = (int)(Math.random() * 10000) + "";
        String code = confirmCodeGenerator.generatePassword(4);
       // emailSender.sendSimpleMessage(email, "Код подтверждения", "Ваш код подтверждения: " + code);

        try {
            emailSender.sendSimpleMessage(email, "Код подтверждения", "Ваш код подтверждения: " + randCode);
        }catch (Exception ex){
            // вернуть ответ с ошибкой, что произошла ошибка в процессе отправки кода
            return new ResponseEntity<>(Message.of("Ошибка в процессе отправки кода на почту"), HttpStatus.NOT_IMPLEMENTED);
        }
        //return code;
        return new ResponseEntity<>(randCode, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> confirmation(String email, String code) {
        // найти User по email
        // найти UserCode по User
        // сравнить код из UserCode с code
        // если совпадают подтверждаем и в UserCode апдейтим поле confirm = true
        // если нет, возвращаем ответ, что введен неверный код
        return null;
    }

    private <T> String validateConstraints(T object){
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);

        if (constraintViolations.size() > 0) {
            Set<String> violationMessages = new HashSet<String>();

            for (ConstraintViolation<T> constraintViolation : constraintViolations) {
                violationMessages.add(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage());
            }

            return "Неверные данные:\n" + StringUtils.join(violationMessages, '\n');
        }
        return null;
    }
}
