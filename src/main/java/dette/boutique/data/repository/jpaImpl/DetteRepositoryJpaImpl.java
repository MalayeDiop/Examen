package dette.boutique.data.repository.jpaImpl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import dette.boutique.core.repo.impl.RepositoryJpaImpl;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.Paiement;
import dette.boutique.data.repository.interfaces.DetteRepository;

public class DetteRepositoryJpaImpl extends RepositoryJpaImpl<Dette> implements DetteRepository {
    private EntityManager em;
    private List<Paiement> paiements = new ArrayList<>();

    public DetteRepositoryJpaImpl(EntityManager em, Class<Dette> type) {
        super(type);
        this.em = em;
    }

    @Override
    public boolean articlesDette(Dette dette) {
        return false;
    }

    @Override
    public List<Dette> findByClient(Client client) {
        try {
            String query = "SELECT d FROM Dette d WHERE d.client.id = :clientId";
            return em.createQuery(query, Dette.class)
                    .setParameter("clientId", client.getId())
                    .getResultList();
        } catch (Exception e) {
            System.out.println("Erreur lors de la recherche des dettes pour le client : " + e.getMessage());
        }
        return null;
    }

    @Override
    public void ajouterPaiement(Paiement paiement) {
        try {
            em.getTransaction().begin();
            Dette dette = em.find(Dette.class, paiement.getDette().getId());
            if (dette != null) {
                addPaiement(paiement);
                em.merge(dette);
            } else {
                System.out.println("Dette non trouvée pour le paiement");
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Erreur lors de l'ajout du paiement : " + e.getMessage());
        }
    }

    public void addPaiement(Paiement paiement) {
        if (paiement != null) {
            this.paiements.add(paiement);
        }
    }
    
}