package kg.megacom.authorization.dao;

import kg.megacom.authorization.models.entities.User;
import kg.megacom.authorization.models.entities.UserCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserCodeDao extends JpaRepository<UserCode, Long> {

    UserCode findByUserAndExpirationDateAfter(User user, Date date);
}
