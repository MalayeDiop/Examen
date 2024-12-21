package dette.boutique.data.entities;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Data;

@Data
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "adresse", nullable = false)
    private String adresse;

    @Column(name = "telephone", nullable = false, unique = true)
    private String telephone;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dette> dettes = new ArrayList<>(); 

    public void addDette(Dette dette) {
        dettes.add(dette);
    }
    
    public void setDettes(Dette dette) {
        dette.setClient(this);
        dettes.add(dette);
    }


    public void ajouterDette(Dette dette) {
        if (dette != null) {
            dette.setClient(this);
            this.dettes.add(dette);
        }
    }
    
    public Client() {
        this.user = null;
    }

    public Client(String surname,String adresse, String telephone) {
        this.surname = surname;
        this.adresse = adresse;
        this.telephone = telephone;
        this.user = null;
    }

    public Client(String surname,String adresse, String telephone, User user) {
        this.surname = surname;
        this.adresse = adresse;
        this.telephone = telephone;
        this.user = user;
    }

    public Client(int id, String surname,String telephone, String adresse, User user) {
        this.id = id;
        this.surname = surname;
        this.adresse = adresse;
        this.telephone = telephone;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Client[Surname=" + surname + ", Adresse=" + adresse + ", Telephone=" + telephone + "]";
    }
}