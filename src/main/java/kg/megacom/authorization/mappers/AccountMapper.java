package kg.megacom.authorization.mappers;

import kg.megacom.authorization.models.dtos.AccountDto;
import kg.megacom.authorization.models.entities.Account;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper extends BaseMapper<Account, AccountDto> {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);
}
