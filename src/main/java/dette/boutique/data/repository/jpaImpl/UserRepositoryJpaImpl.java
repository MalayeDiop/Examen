package dette.boutique.data.repository.jpaImpl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import dette.boutique.core.repo.impl.RepositoryJpaImpl;
import dette.boutique.data.entities.User;
import dette.boutique.data.repository.interfaces.UserRepository;

public class UserRepositoryJpaImpl extends RepositoryJpaImpl<User> implements UserRepository {
    private EntityManager em;

    public UserRepositoryJpaImpl(EntityManager em, Class<User> type) {
        super(type);
        this.em = em;
    }

    @Override
    public boolean insertWithoutClient(User user) {
        try {
            em.getTransaction().begin();
            User existingUser = em.find(User.class, user.getId());
            if (existingUser == null) {
                
                em.persist(user);
            } else {
                System.out.println("Utilisateur déjà existant avec l'ID : " + user.getId());
                em.getTransaction().rollback();
                return false;
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Erreur lors de l'insertion de l'utilisateur sans client : " + e.getMessage());
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean updateClientForUser(User user) {
        try {
            em.getTransaction().begin();
            
            User existingUser = em.find(User.class, user.getId());
            if (existingUser != null) {
                
                existingUser.setClient(user.getClient());
                em.merge(existingUser);
            } else {
                System.out.println("Utilisateur non trouvé avec l'ID : " + user.getId());
                em.getTransaction().rollback();
                return false;
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Erreur lors de la mise à jour du client pour l'utilisateur : " + e.getMessage());
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public User selectByLogin(String login) {
        User user = null;
        try {
            TypedQuery<User> query = this.em.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class);
            query.setParameter("login", login);

            user = query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Erreur lors de la recherche de l'utilisateur par login : " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }
    
}