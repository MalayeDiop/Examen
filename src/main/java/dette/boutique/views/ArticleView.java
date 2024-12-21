package dette.boutique.views;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import dette.boutique.Main;
import dette.boutique.core.ViewImpl;
import dette.boutique.data.entities.Article;
import dette.boutique.services.list.ArticleService;

public class ArticleView extends ViewImpl<Article> {
    private ArticleService articleService;

    public ArticleView(ArticleService articleService) {
        this.articleService = articleService;
    }

    public void menu() {
        System.out.println("1. Ajouter un article");
        System.out.println("2. Afficher tous les articles");
        System.out.println("3. Trouver un article avec son libelle");
        System.out.println("4. Mettre à jour la quantite en stock");
        System.out.println("5. Quitter");
    }

    public void menuArticle() {
        while (true) {
            menu();
            int choix = obtenirChoixUser(1, 5);
            switch (choix) {
                case 1:
                    create();
                    break;
                case 2:
                    List<Article> articles = articleService.selectAll();
                    System.out.println("-----Liste des articles-----");
                    for (Article article : articles) {
                        System.out.println(article);
                    }
                    System.out.println("-----Fin de Liste-----");
                    break;
                case 3:
                    Article article = findArticle();
                    System.out.println(article);
                    break;
                case 4:
                    updateQuantiteStock();
                    break;
                case 5:
                    System.out.println("Sortie du menu article.");
                    return;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }

    @Override
    public void create() {
        String libelle = saisieLibelle();
        int prixUnitaire = saisiePrixUnitaire();
        int qteStock = saisieQteStock();
        Article article = new Article();
        article.setLibelle(libelle);
        article.setPrixUnitaire(prixUnitaire);
        article.setQteStock(qteStock);
        articleService.save(article);
    }

    public String saisieLibelle() {
        Scanner scanner = Main.getScanner();
        String libelle = "";
        boolean isValide = false;
        while (!isValide) {
            System.out.println("Veuillez saisir le libelle de l'article:");
            libelle = scanner.nextLine().trim();
            try {
                if (libelle.isEmpty()) {
                    System.out.println("Erreur : le libelle ne doit pas être vide.");
                } else if (!libelle.matches("[a-zA-Z]+")) {
                    System.out.println("Erreur : le libelle ne doit contenir que des lettres.");
                } else {
                    isValide = true;
                    System.out.println("Libelle accepté !");
                }
            } catch (InputMismatchException e) {
                System.out.println("Veuillez saisir des caractères valides.");
                scanner.nextLine();
            }
        }
        return libelle;
    }

    public int saisiePrixUnitaire() {
        Scanner scanner = Main.getScanner();
        int prixUnitaire = 0;
        boolean isValide = false;
        while (!isValide) {
            System.out.println("Veuillez saisir le prix unitaire de l'article (en nombre entier):");
            try {
                prixUnitaire = Integer.parseInt(scanner.nextLine().trim());
                if (prixUnitaire <= 0) {
                    System.out.println("Erreur : le prix unitaire doit être un nombre positif.");
                } else {
                    isValide = true;
                    System.out.println("Prix unitaire accepté !");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erreur : veuillez saisir un nombre entier valide.");
            }
        }
        return prixUnitaire;
    }

    public int saisieQteStock() {
        Scanner scanner = Main.getScanner();
        int qteStock = 0;
        boolean isValide = false;
        while (!isValide) {
            System.out.println("Veuillez saisir la quantité en stock de l'article:");
            try {
                qteStock = Integer.parseInt(scanner.nextLine().trim());
                if (qteStock <= 0) {
                    System.out.println("Erreur : la quantité en stock doit être un nombre positif.");
                } else {
                    isValide = true;
                    System.out.println("Quantité acceptée !");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erreur : veuillez saisir un nombre entier valide.");
            }
        }
        return qteStock;
    }

    public Article findArticle() {
        Article article = null;
        Scanner scanner = Main.getScanner();
        String libelle = "";
        boolean isValide = false;
        while (!isValide) {
            libelle = scanner.nextLine().trim();
            System.out.println("Veuillez saisir le libelle de l'article à rechercher:");
            libelle = scanner.nextLine();
            while (!isValide) {
                System.out.println("Veuillez saisir le libelle de l'article à rechercher:");
                libelle = scanner.nextLine().trim();
        
                article = articleService.findArticle(libelle);
                if (article != null) {
                    System.out.println("Article trouvé !");
                    isValide = true;
                } else {
                    System.out.println("Article non trouvé. Veuillez réessayer.");
                }
            }
        }
        return article;
    }

    public List<Article> choisirArticles() {
        List<Article> articlesDisponibles = articleService.selectAll();
        List<Article> articlesChoisis = new ArrayList<>();
        if (articlesDisponibles.isEmpty()) {
            System.out.println("Aucun article disponible.");
            return articlesChoisis;
        }
        System.out.println("Veuillez choisir les articles pour la dette :");
        int index = 1;
        for (Article article : articlesDisponibles) {
            System.out.println(index++ + ") " + article.getLibelle() + " - Prix : " + article.getPrixUnitaire());
        }
        boolean continuer = true;
        while (continuer) {
            System.out.print("Entrez le numéro de l'article à ajouter (ou 0 pour arrêter) : ");
            int choix = Main.getScanner().nextInt();
            if (choix == 0) {
                continuer = false;
            } else if (choix > 0 && choix <= articlesDisponibles.size()) {
                articlesChoisis.add(articlesDisponibles.get(choix - 1));
            } else {
                System.out.println("Choix invalide.");
            }
        }
        return articlesChoisis;
    }

    private void updateQuantiteStock() {
        Scanner scanner = Main.getScanner();
        System.out.print("Entrez l'ID de l'article : ");
        int articleId = scanner.nextInt();
        System.out.print("Entrez la nouvelle quantité en stock : ");
        int nouvelleQuantite = scanner.nextInt();
        boolean success = articleService.updateQuantiteStock(articleId, nouvelleQuantite);
        if (success) {
            System.out.println("Quantité en stock mise à jour avec succès !");
        } else {
            System.out.println("Erreur lors de la mise à jour de la quantité en stock.");
        }
    }

}