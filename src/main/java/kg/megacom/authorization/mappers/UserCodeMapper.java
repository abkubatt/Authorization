package kg.megacom.authorization.mappers;

import kg.megacom.authorization.models.dtos.UserCodeDto;
import kg.megacom.authorization.models.entities.UserCode;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserCodeMapper extends BaseMapper<UserCode, UserCodeDto> {
    UserCodeMapper INSTANCE = Mappers.getMapper(UserCodeMapper.class);
}
