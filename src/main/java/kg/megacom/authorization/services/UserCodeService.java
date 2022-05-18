package kg.megacom.authorization.services;

import kg.megacom.authorization.models.dtos.UserCodeDto;

public interface UserCodeService {

    UserCodeDto save(UserCodeDto userCodeDto);
}
