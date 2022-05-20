package kg.megacom.authorization.dao;

import kg.megacom.authorization.models.entities.CodeEnterAttempt;
import kg.megacom.authorization.models.entities.UserCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeEnterAttemptDao extends JpaRepository<CodeEnterAttempt, Long> {

    List<CodeEnterAttempt> findAllByUserCode(UserCode userCode);
}
