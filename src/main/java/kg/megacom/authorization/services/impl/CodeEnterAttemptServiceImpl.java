package kg.megacom.authorization.services.impl;

import kg.megacom.authorization.dao.CodeEnterAttemptDao;
import kg.megacom.authorization.mappers.CodeEnterAttemptMapper;
import kg.megacom.authorization.mappers.UserCodeMapper;
import kg.megacom.authorization.models.dtos.CodeEnterAttemptDto;
import kg.megacom.authorization.models.dtos.UserCodeDto;
import kg.megacom.authorization.models.entities.CodeEnterAttempt;
import kg.megacom.authorization.services.CodeEnterAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CodeEnterAttemptServiceImpl implements CodeEnterAttemptService {

    @Autowired
    private CodeEnterAttemptDao codeEnterAttemptDao;
    private CodeEnterAttemptMapper codeEnterAttemptMapper = CodeEnterAttemptMapper.INSTANCE;
    private UserCodeMapper userCodeMapper = UserCodeMapper.INSTANCE;

    @Override
    public CodeEnterAttemptDto save(CodeEnterAttemptDto codeEnterAttemptDto) {
        return codeEnterAttemptMapper
                .toDto(codeEnterAttemptDao.save(codeEnterAttemptMapper.toEntity(codeEnterAttemptDto)));
    }

    @Override
    public List<CodeEnterAttemptDto> findAllByUserCode(UserCodeDto userCodeDto) {
        List<CodeEnterAttempt> attempts = codeEnterAttemptDao.findAllByUserCode(userCodeMapper.toEntity(userCodeDto));
        return codeEnterAttemptMapper.toDtoList(attempts);
    }
}
