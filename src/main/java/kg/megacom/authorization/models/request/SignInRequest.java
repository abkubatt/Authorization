package kg.megacom.authorization.models.request;

import kg.megacom.authorization.models.enums.RoleType;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class SignInRequest {
    @NotEmpty(message = "Имя не должно быть пустым")
    private String name;
    private String phone;
    @Email(message = "Неверный формат email")
    @NotEmpty
    private String email;
    @Size(min = 5, max = 8, message = "Пароль должен быть не менее 5 и не более 8 символов")
    @NotEmpty
    private String password;
    private RoleType roleType;
}
