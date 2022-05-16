package kg.megacom.authorization.models.dtos;

import lombok.Data;

import java.util.Date;
@Data
public class UserCodeDto {

    private Long id;
    private UserDto user;
    private String code;
    private Date sentDate;
}
