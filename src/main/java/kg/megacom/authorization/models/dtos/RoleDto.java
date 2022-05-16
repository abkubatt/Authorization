package kg.megacom.authorization.models.dtos;

import kg.megacom.authorization.models.enums.RoleType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleDto {

    private Long id;
    private RoleType role;
}
