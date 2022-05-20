package kg.megacom.authorization.mappers;

import kg.megacom.authorization.models.dtos.CodeEnterAttemptDto;
import kg.megacom.authorization.models.entities.CodeEnterAttempt;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CodeEnterAttemptMapper extends BaseMapper<CodeEnterAttempt, CodeEnterAttemptDto> {
    CodeEnterAttemptMapper INSTANCE = Mappers.getMapper(CodeEnterAttemptMapper.class);
}
