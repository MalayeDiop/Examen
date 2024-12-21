package dette.boutique.core.factory;


import dette.boutique.core.factory.impl.Factory;
import dette.boutique.data.repository.interfaces.ArticleRepository;
import dette.boutique.data.repository.interfaces.ClientRepository;
import dette.boutique.data.repository.interfaces.DetailRepository;
import dette.boutique.data.repository.interfaces.DetteRepository;
import dette.boutique.data.repository.listImpl.ArticleRepositoryListImpl;
import dette.boutique.data.repository.listImpl.ClientRepositoryListImpl;
import dette.boutique.data.repository.listImpl.DetteRepositoryListImpl;
import dette.boutique.data.repository.listImpl.UserRepositoryListImpl;
import dette.boutique.data.repository.listImpl.DetailRepositoryListImpl;
import dette.boutique.data.repository.interfaces.UserRepository;

public class FactoryRepositoryList implements Factory {

    public FactoryRepositoryList() {

    }

    @Override
    public ClientRepository getinstanceClientRepository() {
        return new ClientRepositoryListImpl();
    }

    @Override
    public UserRepository getinstanceUserRepository() {
        return new UserRepositoryListImpl();
    }

    @Override
    public ArticleRepository getinstanceArticleRepository() {
        return new ArticleRepositoryListImpl();
    }

    @Override
    public DetteRepository getinstanceDetteRepository() {
        return new DetteRepositoryListImpl();
    }

    @Override
    public DetailRepository getinstanceDetailRepository() {
        return new DetailRepositoryListImpl();
    }
    
}