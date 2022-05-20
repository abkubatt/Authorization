package kg.megacom.authorization.models.entities;

import com.fasterxml.jackson.databind.DatabindException;
import kg.megacom.authorization.models.enums.Status;
import lombok.Data;
import org.springframework.data.domain.AfterDomainEventPublication;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tb_attempts")
@Data
public class CodeEnterAttempt {

    @Id
    @GeneratedValue
    private Long id;
    private Date attemptDate;
    @ManyToOne
    @JoinColumn(name = "user_code_id")
    private UserCode userCode;
    @Enumerated(EnumType.STRING)
    private Status status;
}
