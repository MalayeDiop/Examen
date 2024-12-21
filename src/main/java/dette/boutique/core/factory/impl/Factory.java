package dette.boutique.core.factory.impl;

import dette.boutique.data.repository.interfaces.ArticleRepository;
import dette.boutique.data.repository.interfaces.ClientRepository;
import dette.boutique.data.repository.interfaces.DetailRepository;
import dette.boutique.data.repository.interfaces.DetteRepository;
import dette.boutique.data.repository.interfaces.UserRepository;

public interface Factory {
    ClientRepository getinstanceClientRepository();
    UserRepository getinstanceUserRepository();
    ArticleRepository getinstanceArticleRepository();
    DetteRepository getinstanceDetteRepository();
    DetailRepository getinstanceDetailRepository();
}