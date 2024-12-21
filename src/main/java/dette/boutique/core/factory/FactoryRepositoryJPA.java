package dette.boutique.core.factory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import dette.boutique.core.factory.impl.Factory;
import dette.boutique.data.entities.Article;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.User;
import dette.boutique.data.repository.interfaces.ArticleRepository;
import dette.boutique.data.repository.interfaces.ClientRepository;
import dette.boutique.data.repository.interfaces.DetailRepository;
import dette.boutique.data.repository.interfaces.DetteRepository;
import dette.boutique.data.repository.interfaces.UserRepository;
import dette.boutique.data.repository.jpaImpl.ArticleRepositoryJpaImpl;
import dette.boutique.data.repository.jpaImpl.ClientRepositoryJpaImpl;
import dette.boutique.data.repository.jpaImpl.DetailRepositoryJpaImpl;
import dette.boutique.data.repository.jpaImpl.DetteRepositoryJpaImpl;
import dette.boutique.data.repository.jpaImpl.UserRepositoryJpaImpl;

public class FactoryRepositoryJPA implements Factory {
    private EntityManager em;

    public FactoryRepositoryJPA() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("POSTGRESQLDETTES");
        this.em = emf.createEntityManager();
    }

    @Override
    public ClientRepository getinstanceClientRepository() {
        return new ClientRepositoryJpaImpl(em, Client.class, getinstanceUserRepository());
    }

    @Override
    public UserRepository getinstanceUserRepository() {
        return new UserRepositoryJpaImpl(em, User.class);
    }

    @Override
    public ArticleRepository getinstanceArticleRepository() {
        return new ArticleRepositoryJpaImpl(em, Article.class);
    }

    @Override
    public DetteRepository getinstanceDetteRepository() {
        return new DetteRepositoryJpaImpl(em, Dette.class);
    }

    @Override
    public DetailRepository getinstanceDetailRepository() {
        // return new DetailRepositoryJpaImpl(em, Details.class);
        return null;
    }
}