package dette.boutique.data.entities;

import lombok.EqualsAndHashCode;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "articles")
public class Article {
    // private static int currentId = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "prix_unitaire", nullable = false)
    private int prixUnitaire;

    @Column(name = "qte_stock", nullable = false)
    private int qteStock;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Details> details;

    // public Article(String libelle) {
    //     this.id = currentId++;
    //     this.libelle = libelle;
    // }

    public int getQteStock() {
        return qteStock;
    }

    public void setQteStock(int qteStock) {
        this.qteStock = qteStock;
    }

    public Article() { 
    }
    
    public Article(String libelle, int prixUnitaire, int qteSock) {
        this.libelle = libelle;
        this.prixUnitaire = prixUnitaire;
        this.qteStock = qteSock;
    }

    public Article(int id2, String libelle2, int prixUnitaire2, int qteStock2) {
        //TODO Auto-generated constructor stub
    }

    @Override
    public String toString() {
        return "Article[Libelle=" + libelle + ", Prix Unitaire=" + prixUnitaire + ", QteStock=" + qteStock + "]";
    }
}