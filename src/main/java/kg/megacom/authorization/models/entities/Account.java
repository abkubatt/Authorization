package kg.megacom.authorization.models.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "tb_account")
public class Account {

    @Id
    @GeneratedValue
    private Long id;
    private String login;
    //@Size(min = 5, max = 8, message = "Password's length must be between 5 and 8")
    private String password;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
