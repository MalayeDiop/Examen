package dette.boutique.data.entities;

import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "paiements")
public class Paiement {
    // private static int currentId = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "montantpaye", nullable = false)
    private int montantPaye;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detteid", referencedColumnName = "id", nullable = false)
    private Dette dette;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    // public Paiement() {
    //     this.id = currentId++;
    //     this.date = new Date(System.currentTimeMillis());
    // }

    public Paiement(int montantPaye, Dette dette) {
        // this();
        this.montantPaye = montantPaye;
        this.dette = dette;
    }
}