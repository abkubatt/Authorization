package kg.megacom.authorization.mappers;

import kg.megacom.authorization.models.dtos.UserDto;
import kg.megacom.authorization.models.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper extends BaseMapper<User, UserDto> {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
}
