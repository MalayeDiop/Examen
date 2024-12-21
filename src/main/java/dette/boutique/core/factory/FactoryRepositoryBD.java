package dette.boutique.core.factory;

import dette.boutique.core.factory.impl.Factory;
import dette.boutique.data.repository.bdImpl.ArticleRepositoryDBImpl;
import dette.boutique.data.repository.bdImpl.ClientRepositoryDBImpl;
import dette.boutique.data.repository.bdImpl.DetailRepositoryDBImpl;
import dette.boutique.data.repository.bdImpl.DetteRepositoryDBImpl;
import dette.boutique.data.repository.bdImpl.UserRepositoryDBImpl;
import dette.boutique.data.repository.interfaces.ArticleRepository;
import dette.boutique.data.repository.interfaces.ClientRepository;
import dette.boutique.data.repository.interfaces.DetailRepository;
import dette.boutique.data.repository.interfaces.DetteRepository;
import dette.boutique.data.repository.interfaces.UserRepository;

public class FactoryRepositoryBD implements Factory {

    private static ClientRepository clientRepository;
    private static UserRepository userRepository;
    private static ArticleRepository articleRepository;
    private static DetteRepository detteRepository;
    private static DetailRepository detailRepository;

    public FactoryRepositoryBD() {
        this.clientRepository = new ClientRepositoryDBImpl();
        this.userRepository = new UserRepositoryDBImpl(clientRepository);
        this.articleRepository = new ArticleRepositoryDBImpl();
        this.detteRepository = new DetteRepositoryDBImpl(clientRepository, articleRepository);
        this.detailRepository = new DetailRepositoryDBImpl();
    }

    public ClientRepository getinstanceClientRepository() {
        if (clientRepository == null) {
            this.clientRepository = new ClientRepositoryDBImpl();
        }
        return clientRepository;
    }

    public UserRepository getinstanceUserRepository() {
        if (userRepository == null) {
            this.userRepository = new UserRepositoryDBImpl(clientRepository);
        }
        return userRepository;
    }

    public ArticleRepository getinstanceArticleRepository() {
        if (articleRepository == null) {
            this.articleRepository = new ArticleRepositoryDBImpl();
        }
        return articleRepository;
    }

    public DetteRepository getinstanceDetteRepository() {
        if (detteRepository == null) {
            this.detteRepository = new DetteRepositoryDBImpl(clientRepository, articleRepository);
        }
        return detteRepository;
    }

    public DetailRepository getinstanceDetailRepository() {
        if (detailRepository == null) {
            this.detailRepository = new DetailRepositoryDBImpl();
        }
        return detailRepository;
    }
}