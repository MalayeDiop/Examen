package dette.boutique.data.repository.interfaces;

import java.util.List;

import dette.boutique.core.repo.Repository;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.Paiement;

public interface DetteRepository extends Repository<Dette> {
    boolean articlesDette(Dette dette);


    List<Dette> findByClient(Client client);


    void ajouterPaiement(Paiement paiement);
}