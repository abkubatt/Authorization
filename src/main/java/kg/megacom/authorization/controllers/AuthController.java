package kg.megacom.authorization.controllers;

import kg.megacom.authorization.models.request.SignInRequest;
import kg.megacom.authorization.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest signInRequest){
        return authService.signIn(signInRequest);
    }

    @GetMapping("/logIn")
    public ResponseEntity<?> logIn(@RequestParam String login, @RequestParam String password){
        return authService.logIn(login, password);
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam  String email, @RequestParam String code){
        return authService.confirmation(email, code);
    }
}
