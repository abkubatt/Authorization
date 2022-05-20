package kg.megacom.authorization.models.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import kg.megacom.authorization.models.enums.Status;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CodeEnterAttemptDto {

    private Long id;
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private Date attemptDate;
    private UserCodeDto userCode;
    private Status status;
}
