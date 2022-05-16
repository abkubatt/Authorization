package kg.megacom.authorization.services;

import kg.megacom.authorization.models.request.SignInRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<?> signIn(SignInRequest signInRequest);

    ResponseEntity<?> logIn(String login, String password);

    String sendCode(String email);

    ResponseEntity<?> confirmation(String email, String code);


}
