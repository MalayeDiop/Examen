package dette.boutique.data.repository.listImpl;

import dette.boutique.core.repo.impl.RepositoryListImpl;
import dette.boutique.data.entities.Article;
import dette.boutique.data.repository.interfaces.ArticleRepository;

public class ArticleRepositoryListImpl extends RepositoryListImpl<Article> implements ArticleRepository {

    @Override
    public Article findByLibelle(String libelle) {
        return data.stream()
                .filter(article -> article.getLibelle().compareTo(libelle) == 0)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean updateQteStock(String libelle, int qte) {
        return data.stream()
                .filter(article -> article.getLibelle().compareTo(libelle) == 0)
                .findFirst()
                .map(article -> {
                    int newQte = article.getQteStock() + qte;
                    if (newQte >= 0) {
                        article.setQteStock(newQte);
                        return true;
                    } else {
                        System.out.println("La quantité en stock ne peut pas être négative.");
                        return false;
                    }
                })
                .orElse(false);
    }

    @Override
    public Article findById(int id) {
        return data.stream()
            .filter(article -> article.getId() == id)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Aucun article trouvé avec l'ID : " + id));
    }

    @Override
    public boolean update(Article article) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() == article.getId()) {
                data.set(i, article);
                return true;
            }
        }
        return false;
    }
    
}