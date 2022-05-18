package kg.megacom.authorization.models.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
@Data
@Builder
public class UserCodeDto {

    private Long id;
    private UserDto user;
    private String code;
    private Date sentDate;
    private boolean confirm;
}
