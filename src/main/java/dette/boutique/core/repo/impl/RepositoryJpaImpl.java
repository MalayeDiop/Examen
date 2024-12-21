package dette.boutique.core.repo.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import dette.boutique.core.repo.Repository;
import dette.boutique.services.YamlServiceImpl;
import dette.boutique.services.interfaces.YamlService;

public class RepositoryJpaImpl<T> implements Repository<T> {
    protected EntityManager em;
    protected EntityManagerFactory emf ;
    protected Class<T> type;
    protected String tableName;
    YamlService yamlService ;

    public RepositoryJpaImpl( Class<T> type) {
        this.type = type;
        
        if (em == null) {
            yamlService = new YamlServiceImpl();
            Map<String, Object> mapYaml = yamlService.loadYaml();
            this.emf = Persistence.createEntityManagerFactory(mapYaml.get("persistence").toString()); 
            this.em = emf.createEntityManager(); 
        } else {
            this.em = em; 
        }
    }

    @Override
    public boolean insert(T element) {
        boolean success = false;
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        try {
            if (em.contains(element)) {
                em.merge(element);
            } else {
                em.persist(element);
            }
            em.persist(element); 
            transaction.commit();
            success = true;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public List<T> selectAll() {
        return em.createQuery("SELECT t FROM " + type.getSimpleName() + " t", type).getResultList();
    }

    @Override
    public void remove(T object) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            T mergedObject = em.merge(object);  
            em.remove(mergedObject);  
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

    }

    @Override
    public T findById(int id) {
        T entity = null;
        try {
            entity = em.find(type, id);  
        } catch (Exception e) {
            e.printStackTrace();
        }
        return entity;
    }

    @Override
    public boolean update(T element) {
        try {
            em.getTransaction().begin();
            em.merge(element);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
    }

    public void closeConnexion() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

}