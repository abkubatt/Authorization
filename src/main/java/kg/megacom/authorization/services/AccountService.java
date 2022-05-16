package kg.megacom.authorization.services;

import kg.megacom.authorization.models.dtos.AccountDto;
import kg.megacom.authorization.models.dtos.UserDto;

public interface AccountService {

    AccountDto save(AccountDto accountDto);
    AccountDto findByUser(UserDto userDto);
    AccountDto findByEmail(String email);
}
