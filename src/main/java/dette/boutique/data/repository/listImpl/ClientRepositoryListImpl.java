package dette.boutique.data.repository.listImpl;

import dette.boutique.core.repo.impl.RepositoryListImpl;
import dette.boutique.data.entities.Client;
import dette.boutique.data.repository.interfaces.ClientRepository;

public class ClientRepositoryListImpl extends RepositoryListImpl<Client> implements ClientRepository {

    @Override
    public Client findByTel(String telephone) {
        return data.stream()
                .filter(client -> client.getTelephone().compareTo(telephone) == 0)
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean insertWithoutUser(Client client) {
        data.add(client);
        return true;
    }

    @Override
    public boolean updateUserForClient(Client client) {
        client.setUser(client.getUser());
        return true;
    }

    @Override
    public Client findById(int id) {
        return data.stream()
            .filter(client -> client.getId() == id)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Aucun client trouvé avec l'ID : " + id));
    }

    @Override
    public boolean update(Client client) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() == client.getId()) {
                data.set(i, client);
                return true;
            }
        }
        return false;
    }

    @Override
    public void updateClient(int id, int userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateClient'");
    }
    
}