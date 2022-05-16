package kg.megacom.authorization.models.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDto {

    private Long id;
    private String login;
    private String password;
    private UserDto user;
}
