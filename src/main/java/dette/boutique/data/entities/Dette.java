package dette.boutique.data.entities;

import java.util.Date;
import java.util.List;

import dette.boutique.data.enums.EtatDette;
import lombok.EqualsAndHashCode;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "dettes")
@ToString(exclude = "details")
public class Dette {
    // private static int currentId = 1;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column(name = "montant", nullable = false)
    private int montant;

    @Column(name = "montant_verse", nullable = false)
    private int montantVerse;

    @Column(name = "montant_restant", nullable = false)
    @Transient
    private int montantRestant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "dette", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Details> details;

    @Column(nullable = false)
    @Transient
    private boolean soldee;

    @Column(nullable = false)
    private boolean archivee;

    public Dette(Date date) {
        this.date = date;
    }

    public boolean isSoldee() {
        return soldee;
    }

    public void setSoldee(boolean soldee) {
        this.soldee = soldee;
    }

    public boolean isArchivee() {
        return archivee;
    }

    public void setArchivee(boolean archivee) {
        this.archivee = archivee;
    }

    public Dette() {
        this.montantRestant = montant - montantVerse;
    }

    public void recalculerMontants() {
        montant = details.stream().mapToInt(Details::getPrixTotal).sum();
        montantRestant = montant - montantVerse;
    }

    @Override
    public String toString() {
        return "Dette{id=" + id + ", details=" + (details != null ? details.size() : 0) + "}";
    }
    
}