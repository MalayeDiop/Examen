package dette.boutique.data.repository.interfaces;

import dette.boutique.core.repo.Repository;
import dette.boutique.data.entities.Client;

public interface ClientRepository extends Repository<Client> {
    Client findByTel(String telephone);

    boolean insertWithoutUser(Client client);

    boolean updateUserForClient(Client client);

    void updateClient(int id,int userId);

}