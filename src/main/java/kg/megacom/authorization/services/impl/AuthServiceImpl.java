package kg.megacom.authorization.services.impl;

import kg.megacom.authorization.config.EmailSender;
import kg.megacom.authorization.exceptions.SignInErrorException;
import kg.megacom.authorization.models.dtos.AccountDto;
import kg.megacom.authorization.models.dtos.RoleDto;
import kg.megacom.authorization.models.dtos.UserDto;
import kg.megacom.authorization.models.entities.Role;
import kg.megacom.authorization.models.request.SignInRequest;
import kg.megacom.authorization.models.response.Message;
import kg.megacom.authorization.services.AccountService;
import kg.megacom.authorization.services.AuthService;
import kg.megacom.authorization.services.RoleService;
import kg.megacom.authorization.services.UserService;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;


@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private EmailSender emailSender;

    @Override
    public ResponseEntity<?> signIn(SignInRequest signInRequest) {
        AccountDto accountDto = accountService.findByEmail(signInRequest.getEmail());
        System.out.println("CHECK");
        if(accountDto != null){
            System.out.println("TEST");
            return new ResponseEntity<>(Message.of("Пользователь с данным email уже зарегистрирован"), HttpStatus.NOT_ACCEPTABLE);
        }

        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<SignInRequest>> constraintViolations = validator.validate(signInRequest);

        if (constraintViolations.size() > 0) {
            Set<String> violationMessages = new HashSet<String>();

            for (ConstraintViolation<SignInRequest> constraintViolation : constraintViolations) {
                violationMessages.add(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage());
            }

            return new ResponseEntity<>(Message.of("Неверные данные:\n" + StringUtils.join(violationMessages, '\n')), HttpStatus.NOT_ACCEPTABLE);

         //   throw new RuntimeException("Неверные данные:\n" + StringUtils.join(violationMessages, '\n'));
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
            //Надо сохранить этот код в нашей базе. Для этого создаем новый объект UserCode
            // заполняем его поля
            //save UserCode - сохраняем
            return new ResponseEntity<>(Message.of("Вы успешно зарегистрировались в системе"), HttpStatus.OK);
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
    public String sendCode(String email) {
        //generate random code - рандомно сгеренрировать 4-х значное число
        try {
            emailSender.sendSimpleMessage(email, "Код подтверждения", "Ваш код подтверждения: " + "code");
        }catch (Exception ex){
            // вернуть ответ с ошибкой, что произошла ошибка в процессе отправки кода
        }
        //return code;
        return null;
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

//    private String validateConstraints(Object object, String type){
//        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//        Set<ConstraintViolation<String>> constraintViolations = validator.validate(object);
//
//        if (constraintViolations.size() > 0) {
//            Set<String> violationMessages = new HashSet<String>();
//
//            for (ConstraintViolation<String> constraintViolation : constraintViolations) {
//                violationMessages.add(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage());
//            }
//
//            return new ResponseEntity<>(Message.of("Неверные данные:\n" + StringUtils.join(violationMessages, '\n')), HttpStatus.NOT_ACCEPTABLE);
//
//            //   throw new RuntimeException("Неверные данные:\n" + StringUtils.join(violationMessages, '\n'));
//        }
//    }
}
