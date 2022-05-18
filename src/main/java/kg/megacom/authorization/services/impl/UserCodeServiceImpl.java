package kg.megacom.authorization.services.impl;

import kg.megacom.authorization.dao.UserCodeDao;
import kg.megacom.authorization.mappers.UserCodeMapper;
import kg.megacom.authorization.models.dtos.UserCodeDto;
import kg.megacom.authorization.models.entities.UserCode;
import kg.megacom.authorization.services.UserCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCodeServiceImpl implements UserCodeService {

    @Autowired private UserCodeDao userCodeDao;
    private UserCodeMapper userCodeMapper = UserCodeMapper.INSTANCE;

    @Override
    public UserCodeDto save(UserCodeDto userCodeDto) {
        return userCodeMapper.toDto(userCodeDao.save(userCodeMapper.toEntity(userCodeDto)));
    }
}
