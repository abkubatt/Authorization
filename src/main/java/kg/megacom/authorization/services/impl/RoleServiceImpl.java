package kg.megacom.authorization.services.impl;

import kg.megacom.authorization.dao.RoleDao;
import kg.megacom.authorization.mappers.RoleMapper;
import kg.megacom.authorization.models.dtos.RoleDto;
import kg.megacom.authorization.models.entities.Role;
import kg.megacom.authorization.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;
    private RoleMapper roleMapper = RoleMapper.INSTANCE;

    @Override
    public RoleDto save(RoleDto roleDto) {
        Role role = roleMapper.toEntity(roleDto);
        return roleMapper.toDto(roleDao.save(role));
    }
}
