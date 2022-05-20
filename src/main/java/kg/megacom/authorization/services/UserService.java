package kg.megacom.authorization.services;

import kg.megacom.authorization.models.dtos.UserDto;

public interface UserService {

    UserDto save(UserDto userDto);
    UserDto findById(Long id);
    UserDto findByEmail(String email);
}
