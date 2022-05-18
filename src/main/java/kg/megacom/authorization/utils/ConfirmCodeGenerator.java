package kg.megacom.authorization.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;
import java.util.Random;

@Configuration
public class ConfirmCodeGenerator {

    @Bean
    public ConfirmCodeGenerator getPasswordGenerator() {
        return new ConfirmCodeGenerator();
    }

    private final char[] numbers = "0123456789".toCharArray();

    public String generatePassword(int length) {

        if (length < 4) {
            length = 4;
        }
        Random random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(numbers[random.nextInt(numbers.length)]);
        }
        return password.toString();
    }


}
