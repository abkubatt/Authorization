package kg.megacom.authorization.models.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
@Builder
public class UserDto {

    private Long id;
    private String name;
    private String phone;
    private String email;
    private RoleDto role;
    private boolean confirm;
}
