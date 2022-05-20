package kg.megacom.authorization.services.impl;

import kg.megacom.authorization.config.EmailSender;
import kg.megacom.authorization.exceptions.SignInErrorException;
import kg.megacom.authorization.models.dtos.*;
import kg.megacom.authorization.models.entities.Role;
import kg.megacom.authorization.models.entities.UserCode;
import kg.megacom.authorization.models.enums.Status;
import kg.megacom.authorization.models.request.SignInRequest;
import kg.megacom.authorization.models.response.Message;
import kg.megacom.authorization.services.*;
import kg.megacom.authorization.utils.ConfirmCodeGenerator;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;


@Service
public class AuthServiceImpl implements AuthService {
    @Autowired private UserService userService;
    @Autowired private AccountService accountService;
    @Autowired private RoleService roleService;
    @Autowired private EmailSender emailSender;
    @Autowired private ConfirmCodeGenerator confirmCodeGenerator;
    @Autowired private UserCodeService userCodeService;
    @Autowired private CodeEnterAttemptService attemptService;

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
            ResponseEntity<?> codeSendResponse = sendCode(userDto);
            if(!codeSendResponse.getStatusCode().equals(HttpStatus.OK)) return codeSendResponse;
            //Надо сохранить этот код в нашей базе. Для этого создаем новый объект UserCode

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
    public ResponseEntity<?> sendCode(UserDto userDto) {
        //generate random code - рандомно сгеренрировать 4-х значное число
//        String randCode = (int)(Math.random() * 10000) + "";
        String code = confirmCodeGenerator.generatePassword(4);
       // emailSender.sendSimpleMessage(email, "Код подтверждения", "Ваш код подтверждения: " + code);

        try {
            emailSender.sendSimpleMessage(userDto.getEmail(), "Код подтверждения", "Ваш код подтверждения: " + code);
        }catch (Exception ex){
            // вернуть ответ с ошибкой, что произошла ошибка в процессе отправки кода
            return new ResponseEntity<>(Message.of("Ошибка в процессе отправки кода на почту"), HttpStatus.NOT_IMPLEMENTED);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 3);
        System.out.println(calendar.getTime());

        UserCodeDto userCodeDto = UserCodeDto.builder()
                .user(userDto)
                .code(code)
                .sentDate(new Date())
                .expirationDate(calendar.getTime())
                .confirm(false)
                .build();
        System.out.println(userCodeDto.getExpirationDate());
        userCodeService.save(userCodeDto);
        //return code;
        return new ResponseEntity<>(code, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> confirmation(String email, String code) {
        // найти User по email
        UserDto userDto = userService.findByEmail(email);
        // найти UserCode по User
        UserCodeDto userCodeDto = userCodeService.findByUserAndExpDate(userDto, new Date());
        if(userCodeDto == null){
            return new ResponseEntity<>(Message.of("Срок действия кода истёк"), HttpStatus.NOT_ACCEPTABLE);
        }
        // сравнить код из UserCode с code

        List<CodeEnterAttemptDto> attemptDtos = attemptService.findAllByUserCode(userCodeDto);
        if(attemptDtos.size() < 3) {
            if (userCodeDto.getCode().equals(code)) {
                CodeEnterAttemptDto codeEnterAttemptDto = CodeEnterAttemptDto.builder()
                        .userCode(userCodeDto)
                        .attemptDate(new Date())
                        .status(Status.SUCCESSFUL)
                        .build();
                attemptService.save(codeEnterAttemptDto);

                userCodeDto.setConfirm(true);
                userCodeService.save(userCodeDto);

                userDto.setConfirm(true);
                userService.save(userDto);

                return new ResponseEntity<>(Message.of("Добро пожаловать в систему"), HttpStatus.OK);
            } else {
                CodeEnterAttemptDto codeEnterAttemptDto = CodeEnterAttemptDto.builder()
                        .userCode(userCodeDto)
                        .attemptDate(new Date())
                        .status(Status.FAILED)
                        .build();
                attemptService.save(codeEnterAttemptDto);
                return new ResponseEntity<>(Message.of("Вы не правильно ввели код. Попробуйте еще раз"), HttpStatus.NOT_ACCEPTABLE);
            }
        }else{
            userCodeDto.setConfirm(false);
            userCodeService.save(userCodeDto);

            userDto.setConfirm(false);
            userService.save(userDto);
            return new ResponseEntity<>(Message.of("Количество попыток ввода кода исчерпано!"), HttpStatus.NOT_ACCEPTABLE);
        }
        // если совпадают подтверждаем и в UserCode апдейтим поле confirm = true
        // если нет, возвращаем ответ, что введен неверный код
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
