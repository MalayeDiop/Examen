package dette.boutique.services.list;

import java.util.List;
import java.util.Scanner;

import dette.boutique.data.entities.Client;
import dette.boutique.data.repository.interfaces.ClientRepository;
import dette.boutique.services.interfaces.ClientServiceImpl;

public class ClientService implements ClientServiceImpl {
    private ClientRepository clientRepository;
    private ClientService clientService;

    public ClientService(ClientRepository clientrepository) {
        this.clientRepository=clientrepository;
    }

    public void createWithoutUser(Client client) {
        clientRepository.insertWithoutUser(client);
    };

    public boolean updateUserForClient(Client client) {
        clientRepository.updateUserForClient(client);
        return true;
    }

    public void updateClient(int id, int userId) {
        clientRepository.updateClient(id, userId);
    }

    @Override
    public List<Client> selectAll() {
        return clientRepository.selectAll();
    }

    public List<Client> listeClientsDispo() {
        return clientRepository.selectAll().stream()
                .filter(client -> client.getUser() == null)
                .toList();
    }
    
    public boolean numDispo(String tel) {
        return clientRepository.selectAll().stream()
                .filter(client -> client.getTelephone() == tel)
                .findFirst()
                .isPresent();
    }

    public Client findClient(String telephone) {
        return selectAll().stream()
                .filter(client -> client.getTelephone().compareTo(telephone) == 0)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(Client element) {
        clientRepository.insert(element);
    }

    @Override
    public boolean update(Client client) {
        return clientRepository.update(client);
    }

    @Override
    public void selectById(int id) {
        clientRepository.findById(id);
    }
    
    public Client choisirClient() {
        List<Client> clients = selectAll();
        if (clients.isEmpty()) {
            System.out.println("Aucun client disponible.");
            return null;
        }
        System.out.println("Liste des clients disponibles :");
        for (int i = 0; i < clients.size(); i++) {
            Client client = clients.get(i);
            System.out.println((i + 1) + ". " + client.getSurname() + " - Téléphone: " + client.getTelephone());
        }
        System.out.print("Choisissez un client (entrez le numéro) : ");
        Scanner scanner = new Scanner(System.in);
        int choix = scanner.nextInt();
        if (choix < 1 || choix > clients.size()) {
            System.out.println("Choix invalide.");
            return null;
        }
        Client client = clients.get(choix - 1);
        System.out.println("Client choisi : " + client.getSurname());
        return client;
    }
}