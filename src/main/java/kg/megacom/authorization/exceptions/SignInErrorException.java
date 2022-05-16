package kg.megacom.authorization.exceptions;

public class SignInErrorException extends RuntimeException {

    public SignInErrorException(String message){
        super(message);
    }
}
