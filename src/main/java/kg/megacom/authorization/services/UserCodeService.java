package kg.megacom.authorization.services;

import kg.megacom.authorization.models.dtos.UserCodeDto;
import kg.megacom.authorization.models.dtos.UserDto;

import java.util.Date;

public interface UserCodeService {

    UserCodeDto save(UserCodeDto userCodeDto);

    UserCodeDto findByUserAndExpDate(UserDto userDto, Date date);
}
