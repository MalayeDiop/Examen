package dette.boutique.core.factory.impl;

import dette.boutique.core.factory.FactoryRepositoryList;
import dette.boutique.core.factory.FactoryRepositoryBD;
import dette.boutique.core.factory.FactoryRepositoryJPA;
import dette.boutique.data.repository.interfaces.ArticleRepository;
import dette.boutique.data.repository.interfaces.ClientRepository;
import dette.boutique.data.repository.interfaces.DetailRepository;
import dette.boutique.data.repository.interfaces.DetteRepository;
import dette.boutique.data.repository.interfaces.UserRepository;

public class RepositoryFactory {
    private static final String DEFAULT_TYPE = "JPA";

    private static FactoryRepositoryJPA getFactory(String type) {
        switch (type.toUpperCase()) {
            case "JPA":
                return new FactoryRepositoryJPA();
            case "BD":
                return new FactoryRepositoryJPA();
            case "LISTE":
                return new FactoryRepositoryJPA();
            default:
                throw new IllegalArgumentException("Type de Factory non supporté : " + type);
        }
    }

    public static ClientRepository createClientRepository(String type) {
        FactoryRepositoryJPA factory = getFactory(type);
        return factory.getinstanceClientRepository();
    }

    public static UserRepository createUserRepository(String type) {
        FactoryRepositoryJPA factory = getFactory(type);
        return factory.getinstanceUserRepository();
    }

    public static ArticleRepository createArticleRepository(String type) {
        FactoryRepositoryJPA factory = getFactory(type);
        return factory.getinstanceArticleRepository();
    }

    public static DetteRepository createDetteRepository(String type) {
        FactoryRepositoryJPA factory = getFactory(type);
        return factory.getinstanceDetteRepository();
    }

    public static DetailRepository createDetailRepository(String type) {
        FactoryRepositoryJPA factory = getFactory(type);
        return factory.getinstanceDetailRepository();
    }
}