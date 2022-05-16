package kg.megacom.authorization.services.impl;

import kg.megacom.authorization.dao.AccountDao;
import kg.megacom.authorization.mappers.AccountMapper;
import kg.megacom.authorization.mappers.UserMapper;
import kg.megacom.authorization.models.dtos.AccountDto;
import kg.megacom.authorization.models.dtos.UserDto;
import kg.megacom.authorization.models.entities.Account;
import kg.megacom.authorization.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountDao accountDao;
    private AccountMapper accountMapper = AccountMapper.INSTANCE;
    private UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public AccountDto save(AccountDto accountDto) {
        Account account = accountMapper.toEntity(accountDto);
        return accountMapper.toDto(accountDao.save(account));
    }

    @Override
    public AccountDto findByUser(UserDto userDto) {
        Account account = accountDao.findByUser(userMapper.toEntity(userDto));
        return accountMapper.toDto(account);
    }

    @Override
    public AccountDto findByEmail(String email) {
        Account account = accountDao.findByLogin(email);
        return accountMapper.toDto(account);
    }
}
