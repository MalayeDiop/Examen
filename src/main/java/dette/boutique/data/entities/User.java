package dette.boutique.data.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Data;

import dette.boutique.data.enums.Role;
import java.util.Date;
import dette.boutique.data.entities.Client;
import javax.persistence.*;

@Data
@Entity
@Table(name = "users")
public class User {
    // private static int currentId = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean active;

    public boolean isActive() {
        return this.active;
    }

    public void setActif(boolean actif) {
        this.active = actif;
    }

    public void setRole(Role roleId) {
        this.role = roleId;
    }

    // public User(boolean active) {
    //     this.id = currentId++;
    //     this.active = true;
    // }

    public User(String prenom, String nom, String login, String password, Role role) {
        this.prenom = prenom;
        this.nom = nom;
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public User(String login, String password, Role role) {
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public User(String surname, String login2, String password2, Client clientAffilié, Role roleUser) {
        
    }

    public User() {   
    }

    @Override
    public String toString() {
        return "User[login=" + login + ", role=" + role
                + ", actif=" + active + "]";
    }
}