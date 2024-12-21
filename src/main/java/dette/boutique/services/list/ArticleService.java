package dette.boutique.services.list;

import java.util.List;
import java.util.Scanner;

import dette.boutique.data.entities.Article;
import dette.boutique.data.repository.interfaces.ArticleRepository;
import dette.boutique.services.interfaces.ArticleServiceImpl;

public class ArticleService implements ArticleServiceImpl {
    ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public void save(Article element) {
        articleRepository.insert(element);
    }

    @Override
    public boolean update(Article article) {
        return articleRepository.update(article);
    }

    @Override
    public void selectById(int id) {
        articleRepository.findById(id);
    }

    @Override
    public List<Article> selectAll() {
        return articleRepository.selectAll();
    }

    @Override
    public Article findArticle(String libelle) {
        return selectAll().stream()
                .filter(article -> article.getLibelle().compareTo(libelle) == 0)
                .findFirst()
                .orElse(null);
    }

    public List<Article> listeArticlesDispo() {
        return articleRepository.selectAll().stream()
                .filter(article -> article.getQteStock() == 0)
                .toList();
    }

    public boolean articleDispo(String libelle) {
        return articleRepository.selectAll().stream()
                .filter(article -> article.getLibelle() == libelle)
                .findFirst()
                .isPresent();
    }

    public Article choisirArticle() {
        List<Article> articles = selectAll();
        if (articles.isEmpty()) {
            System.out.println("Aucun article disponible.");
            return null;
        }
        System.out.println("Liste des articles disponibles :");
        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            System.out.println((i + 1) + ". " + article.getLibelle() + " - Prix: " + article.getPrixUnitaire());
        }
        System.out.print("Choisissez un article (entrez le numéro) : ");
        Scanner scanner = new Scanner(System.in);
        int choix = scanner.nextInt();
        if (choix < 1 || choix > articles.size()) {
            System.out.println("Choix invalide.");
            return null;
        }
        return articles.get(choix - 1);
    }

    public boolean updateQuantiteStock(int articleId, int nouvelleQuantite) {
        if (nouvelleQuantite < 0) {
            System.out.println("La quantité en stock ne peut pas être négative.");
            return false;
        }
        Article article = articleRepository.findById(articleId);
        if (article != null) {
            article.setQteStock(nouvelleQuantite);
            return articleRepository.update(article);
        } else {
            System.out.println("Aucun article trouvé avec l'ID : " + articleId);
            return false;
        }
    }
    
}