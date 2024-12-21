package dette.boutique.services.interfaces;

import dette.boutique.core.service.Service;
import dette.boutique.data.entities.Client;

public interface ClientServiceImpl extends Service<Client>{
    Client findClient(String telephone);
}