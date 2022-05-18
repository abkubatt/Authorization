package kg.megacom.authorization.dao;

import kg.megacom.authorization.models.entities.UserCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCodeDao extends JpaRepository<UserCode, Long> {
}
