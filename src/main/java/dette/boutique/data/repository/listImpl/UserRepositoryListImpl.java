package dette.boutique.data.repository.listImpl;

import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.repo.impl.RepositoryListImpl;
import dette.boutique.data.entities.User;
import dette.boutique.data.enums.Role;
import dette.boutique.data.repository.interfaces.UserRepository;

public class UserRepositoryListImpl extends RepositoryListImpl<User> implements UserRepository {
    private List<User> users = new ArrayList<>();

    public UserRepositoryListImpl() {
        users.add(new User("admin", "password123", Role.ADMIN));
        users.add(new User("client1", "clientpass", Role.CLIENT));
        users.add(new User("boutiquier", "boutiquierpass", Role.BOUTIQUIER));
    }

    @Override
    public boolean insertWithoutClient(User user) {
        data.add(user);
        return true;
    }

    @Override
    public boolean updateClientForUser(User user) {
        user.setClient(user.getClient());
        user.setNom(user.getClient().getSurname());
        user.setPrenom(user.getClient().getSurname());
        return true;
    }

    @Override
    public User selectByLogin(String login) {
        return data.stream()
               .filter(user -> user.getLogin().equalsIgnoreCase(login))
               .findFirst()
               .orElse(null);
    }

    @Override
    public User findById(int id) {
        return data.stream()
            .filter(user -> user.getId() == id)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Aucun user trouvé avec l'ID : " + id));
    }

    @Override
    public boolean update(User user) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() == user.getId()) {
                data.set(i, user);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users);
    }
    
}