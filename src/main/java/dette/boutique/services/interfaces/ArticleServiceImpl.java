package dette.boutique.services.interfaces;

import dette.boutique.core.service.Service;
import dette.boutique.data.entities.Article;

public interface ArticleServiceImpl extends Service<Article> {
    Article findArticle(String libelle);
}