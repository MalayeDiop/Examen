package dette.boutique.data.repository.interfaces;

import java.util.List;

import dette.boutique.core.repo.Repository;
import dette.boutique.data.entities.User;

public interface UserRepository extends Repository<User> {
    boolean insertWithoutClient(User user);

    boolean updateClientForUser(User user);

    User selectByLogin(String login);

    List<User> findAll();

}