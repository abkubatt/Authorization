package kg.megacom.authorization.services;

import kg.megacom.authorization.models.dtos.CodeEnterAttemptDto;
import kg.megacom.authorization.models.dtos.UserCodeDto;

import java.util.List;

public interface CodeEnterAttemptService {

    CodeEnterAttemptDto save(CodeEnterAttemptDto codeEnterAttemptDto);

    List<CodeEnterAttemptDto> findAllByUserCode(UserCodeDto userCodeDto);
}
