package dette.boutique.data.repository.listImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dette.boutique.core.repo.impl.RepositoryListImpl;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.Paiement;
import dette.boutique.data.repository.interfaces.DetteRepository;

public class DetteRepositoryListImpl extends RepositoryListImpl<Dette> implements DetteRepository {
    private List<Dette> dettes = new ArrayList<>();
    private List<Paiement> paiements = new ArrayList<>();

    @Override
    public boolean articlesDette(Dette dette) {
        data.add(dette);
        return true;
    }

    @Override
    public List<Dette> findByClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Le client ne peut pas être null.");
        }
        return dettes.stream()
            .filter(dette -> client.equals(dette.getClient()))
            .collect(Collectors.toList());
    }

    @Override
    public Dette findById(int id) {
        return selectAll().stream()
            .filter(dettes -> dettes.getId()==id)
            .findFirst()
            .orElse(null);
    }

    @Override
    public boolean update(Dette dette) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() == dette.getId()) {
                data.set(i, dette);
                return true;
            }
        }
        return false;
    }

    @Override
    public void ajouterPaiement(Paiement paiement) {
        paiements.add(paiement);
    }
    
}