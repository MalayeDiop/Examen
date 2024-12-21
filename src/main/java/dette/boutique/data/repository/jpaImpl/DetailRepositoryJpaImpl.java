package dette.boutique.data.repository.jpaImpl;

import javax.persistence.EntityManager;

import dette.boutique.data.entities.Details;

public class DetailRepositoryJpaImpl {
    private EntityManager entityManager;
    private Class<Details> type;

    public DetailRepositoryJpaImpl(EntityManager em, Class<Details> class1) {
        this.entityManager = entityManager;
        this.type = type;
    }
    
}