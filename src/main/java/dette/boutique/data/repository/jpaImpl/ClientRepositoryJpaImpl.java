package dette.boutique.data.repository.jpaImpl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import dette.boutique.core.repo.impl.RepositoryJpaImpl;
import dette.boutique.data.entities.Client;
import dette.boutique.data.repository.interfaces.ClientRepository;
import dette.boutique.data.repository.interfaces.UserRepository;

public class ClientRepositoryJpaImpl extends RepositoryJpaImpl<Client> implements ClientRepository {
    UserRepository userRepository;
    EntityManager em;

    public ClientRepositoryJpaImpl(EntityManager em, Class<Client> type, UserRepository userRepository) {
        super(type);
        this.em = em;
        this.userRepository = userRepository;
    }

    @Override
    public Client findByTel(String telephone) {
        Client client = null;
        try {
            TypedQuery<Client> query = this.em.createQuery("SELECT c FROM Client c WHERE c.telephone = :tel",
                    Client.class);
            query.setParameter("tel", telephone);
            client = query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Erreur lors de la recherche du client par téléphone : " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return client;
    }

    @Override
    public boolean insertWithoutUser(Client client) {
        try {
            em.getTransaction().begin();
            Client existingClient = em.find(Client.class, client.getId());
            if (existingClient != null) {
                existingClient.setUser(client.getUser());
                em.merge(existingClient);
            } else {
                System.out.println("Client non trouvé avec l'ID : " + client.getId());
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Erreur lors de la mise à jour de l'utilisateur du client : " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return true;
    }

    @Override
    public boolean updateUserForClient(Client client) {
        try {
            em.getTransaction().begin();
            Client existingClient = em.find(Client.class, client.getId());
            if (existingClient != null) {
                existingClient.setUser(client.getUser());
                em.merge(existingClient);
            } else {
                System.out.println("Client non trouvé avec l'ID : " + client.getId());
                em.getTransaction().rollback();
                return false;
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Erreur lors de la mise à jour de l'utilisateur pour le client : " + e.getMessage());
            return false;
        }
    }

    @Override
    public void updateClient(int id, int userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateClient'");
    }
    
}