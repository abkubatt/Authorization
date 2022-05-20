package kg.megacom.authorization.services.impl;

import kg.megacom.authorization.dao.UserCodeDao;
import kg.megacom.authorization.mappers.UserCodeMapper;
import kg.megacom.authorization.mappers.UserMapper;
import kg.megacom.authorization.models.dtos.UserCodeDto;
import kg.megacom.authorization.models.dtos.UserDto;
import kg.megacom.authorization.models.entities.UserCode;
import kg.megacom.authorization.services.UserCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserCodeServiceImpl implements UserCodeService {

    @Autowired private UserCodeDao userCodeDao;
    private UserCodeMapper userCodeMapper = UserCodeMapper.INSTANCE;
    private UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public UserCodeDto save(UserCodeDto userCodeDto) {
        return userCodeMapper.toDto(userCodeDao.save(userCodeMapper.toEntity(userCodeDto)));
    }

    @Override
    public UserCodeDto findByUserAndExpDate(UserDto userDto, Date date) {
        UserCode userCode = userCodeDao.findByUserAndExpirationDateAfter(userMapper.toEntity(userDto), date);
        return userCodeMapper.toDto(userCode);
    }
}
