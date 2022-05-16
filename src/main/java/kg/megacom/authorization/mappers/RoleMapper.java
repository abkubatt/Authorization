package kg.megacom.authorization.mappers;

import kg.megacom.authorization.models.dtos.RoleDto;
import kg.megacom.authorization.models.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper extends BaseMapper<Role, RoleDto> {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
}
