package dette.boutique.services.list;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.Paiement;
import dette.boutique.data.repository.interfaces.DetteRepository;
import dette.boutique.services.interfaces.DetteServiceImpl;

public class DetteService implements DetteServiceImpl {
    private DetteRepository detteRepository;

    public DetteService(DetteRepository detteRepository) {
        this.detteRepository = detteRepository;
    }

    public boolean createDette(Dette dette) {
        detteRepository.articlesDette(dette);
        return true;
    }

    public void updateDette(Dette dette) {
        dette.setMontantRestant(dette.getMontant() - dette.getMontantVerse());
        detteRepository.insert(dette);
    }

    public void create(Dette dette) {
        if (dette == null) {
            throw new IllegalArgumentException("La dette ne peut pas être nulle.");
        }
        
        detteRepository.insert(dette);
        System.out.println("Dette créée avec succès !");
    }

    public List<Dette> listDettesByClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Le client ne peut pas être null.");
        }
        return detteRepository.findByClient(client);
    }

    @Override
    public void save(Dette element) {
        detteRepository.insert(element);
    }

    @Override
    public boolean update(Dette element) {
        return detteRepository.update(element);
    }

    @Override
    public void selectById(int id) {
        detteRepository.findById(id);
    }

    @Override
    public List<Dette> selectAll() {
        return detteRepository.selectAll();
    }

    @Override
    public Dette findDette(Date date) {
        return selectAll().stream()
                    .filter(dette -> dette.getDate().compareTo(date) == 0)
                    .findFirst()
                    .orElse(null); 
    }

    public Dette findById(int id) {
        return detteRepository.findById(id);
    }

    public void ajouterPaiement(Paiement paiement) {
        if (paiement.getDette() == null) {
            throw new IllegalArgumentException("Le paiement doit être associé à une dette.");
        }
        detteRepository.ajouterPaiement(paiement);
    }

    public void afficherDettes(List<Dette> dettes) {
        for (Dette dette : dettes) {
            System.out.println(dette);
        }
    }

    public List<Dette> findByClient(Client client) {
        return detteRepository.selectAll().stream()
                .filter(dette -> {
                    Client clientDette = dette.getClient();
                    return clientDette != null && clientDette.equals(client);
                })
                .collect(Collectors.toList());
    }

    public List<Dette> archiverDettesSoldees() {
        List<Dette> toutesLesDettes = detteRepository.selectAll();
        List<Dette> dettesSoldees = toutesLesDettes.stream()
                .filter(dette -> dette.getMontantRestant() == 0 && !dette.isArchivee())
                .toList();
        for (Dette dette : dettesSoldees) {
            dette.setArchivee(true);
            detteRepository.update(dette);
        }
        return toutesLesDettes.stream()
                .filter(dette -> dette.getMontantRestant() > 0)
                .toList();
    }

    public List<Dette> getDettesSoldees() {
        List<Dette> toutesLesDettes = detteRepository.selectAll();
        return toutesLesDettes.stream()
                .filter(dette -> (dette.getMontant() - dette.getMontantVerse()) == 0 && !dette.isArchivee())
                .toList();
    }
    
    public void archiverDettes(List<Dette> dettesAArchiver) {
        for (Dette dette : dettesAArchiver) {
            dette.setArchivee(true);
            detteRepository.update(dette);
        }
    }

}