package dette.boutique.views;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import dette.boutique.core.ViewImpl;
import dette.boutique.data.entities.Article;
import dette.boutique.data.entities.Details;
import dette.boutique.data.entities.Dette;
import dette.boutique.services.list.ArticleService;
import dette.boutique.services.list.DetailService;
import dette.boutique.services.list.DetteService;

public class DetailView extends ViewImpl<Details> {
    private DetailService detailService;
    private DetteService detteService;
    private ArticleService articleService;
    public DetteView detteView;
    public ArticleView articleView;

    public DetailView(DetailService detailService, DetteService detteService, ArticleService articleService) {
        this.detailService = detailService;
        this.detteService = detteService;
        this.articleService = articleService;
    }

    public List<Details> saisirDetails() {
        List<Details> details = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        int montantTotal = 0;
        while (true) {
            Article article = articleService.choisirArticle();
            if (article == null) {
                System.out.println("Aucun article sélectionné.");
                break;
            }
            System.out.print("Saisissez la quantité pour l'article " + article.getLibelle() + ": ");
            int quantite = scanner.nextInt();
            int prixTotal = article.getPrixUnitaire() * quantite;
            montantTotal += prixTotal;
            Details detail = new Details();
            detail.setArticle(article);
            detail.setQte(quantite);
            detail.setPrixTotal(prixTotal);
            details.add(detail);
            System.out.print("Voulez-vous ajouter un autre article ? (oui/non) : ");
            String reponse = scanner.next();
            if (!reponse.equalsIgnoreCase("oui")) {
                break;
            }
        }
        System.out.print("Montant total des articles : " + montantTotal + " FCFA\n");
        System.out.print("Saisissez le montant versé par le client : ");
        int montantVerse = scanner.nextInt();
        int montantRestant = montantTotal - montantVerse;
        System.out.println("Montant restant à payer : " + montantRestant + " FCFA");
        Dette dette = new Dette();
        dette.setDetails(details);
        dette.setMontant(montantTotal);
        dette.setMontantVerse(montantVerse);
        dette.setMontantRestant(montantRestant);
        detteService.create(dette);
        System.out.println("Dette ajoutée avec succès !");
        return details;
    }    

}