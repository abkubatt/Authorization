package kg.megacom.authorization.dao;

import kg.megacom.authorization.models.entities.Account;
import kg.megacom.authorization.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountDao extends JpaRepository<Account, Long> {
    Account findByUser(User user);
    Account findByLogin(String login);
}
