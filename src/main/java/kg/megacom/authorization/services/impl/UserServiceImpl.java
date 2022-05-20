package kg.megacom.authorization.services.impl;

import kg.megacom.authorization.dao.UserDao;
import kg.megacom.authorization.mappers.UserMapper;
import kg.megacom.authorization.models.dtos.UserDto;
import kg.megacom.authorization.models.entities.User;
import kg.megacom.authorization.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    private UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public UserDto save(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        return userMapper.toDto(userDao.save(user));
    }

    @Override
    public UserDto findById(Long id) {
        User user = userDao.findById(id).orElse(null);
        return userMapper.toDto(user);
    }

    @Override
    public UserDto findByEmail(String email) {
        return userMapper.toDto(userDao.findByEmail(email));
    }
}
