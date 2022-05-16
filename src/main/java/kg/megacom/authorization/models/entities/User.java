package kg.megacom.authorization.models.entities;

import kg.megacom.authorization.models.entities.Role;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Data
@Entity
@Table(name = "tb_user")
public class User {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String phone;
    @Email(message = "Email is not valid")
    private String email;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
    @Column
    private boolean confirm;
}
