package dette.boutique.services.interfaces;

import dette.boutique.core.service.Service;
import dette.boutique.data.entities.User;

public interface UserServiceImpl extends Service<User> {
    User findUser(String nom);
}