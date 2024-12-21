package dette.boutique.data.entities;
import lombok.EqualsAndHashCode;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "details")
@ToString(exclude = "dette")
public class Details {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "qte", nullable = false)
    private int qte;

    @Column(name = "prix_total", nullable = false)
    private int prixTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dette_id", referencedColumnName = "id")
    private Dette dette;

    public Details(Article article2, int quantity) {
        //TODO Auto-generated constructor stub
    }
    public Details() {
        //TODO Auto-generated constructor stub
    }
    public Details(String libelle, int prixUnitaire) {
        //TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "Details{id=" + id + ", detteId=" + (dette != null ? dette.getId() : "null") + "}";
    }
    
}