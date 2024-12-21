package dette.boutique.data.repository.interfaces;

import dette.boutique.core.repo.Repository;
import dette.boutique.data.entities.Article;

public interface ArticleRepository extends Repository<Article> {
    Article findByLibelle(String libelle);

    boolean updateQteStock(String libelle, int qte);

}