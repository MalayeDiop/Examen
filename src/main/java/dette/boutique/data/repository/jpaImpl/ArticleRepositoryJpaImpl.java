package dette.boutique.data.repository.jpaImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import dette.boutique.core.repo.impl.RepositoryJpaImpl;
import dette.boutique.data.entities.Article;
import dette.boutique.data.repository.interfaces.ArticleRepository;

public class ArticleRepositoryJpaImpl extends RepositoryJpaImpl<Article> implements ArticleRepository {
    private EntityManager em;

    public ArticleRepositoryJpaImpl(EntityManager em, Class<Article> type) {
        super(type);
        this.em = em;
    }

    @Override
    public Article findByLibelle(String libelle) {
        Article article = null;
        try {
            TypedQuery<Article> query = this.em.createQuery("SELECT a FROM Article a WHERE a.libelle = :libelle", Article.class);
            query.setParameter("libelle", libelle);

            article = query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Erreur lors de la recherche de l'article par libellé : " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return article;
    }

    @Override
    public boolean updateQteStock(String libelle, int qte) {
        try {
            em.getTransaction().begin();
            TypedQuery<Article> query = this.em.createQuery("SELECT a FROM Article a WHERE a.libelle = :libelle", Article.class);
            query.setParameter("libelle", libelle);
            Article article = query.getSingleResult();
            if (article != null) {
                article.setQteStock(qte);
                em.merge(article);
            } else {
                System.out.println("Article non trouvé avec le libellé : " + libelle);
                em.getTransaction().rollback();
                return false;
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Erreur lors de la mise à jour de la quantité en stock : " + e.getMessage());
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    
}