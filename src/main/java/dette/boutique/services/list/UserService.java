package dette.boutique.services.list;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import dette.boutique.Main;
import dette.boutique.data.entities.User;
import dette.boutique.data.enums.Role;
import dette.boutique.data.repository.interfaces.UserRepository;
import dette.boutique.services.interfaces.UserServiceImpl;

public class UserService implements UserServiceImpl {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean createWithoutUser(User user) {
        userRepository.insertWithoutClient(user);
        return true;
    }

    public void create(User user) {
        userRepository.insert(user);
    }

    public boolean updateClientForUser(User user) {
        userRepository.updateClientForUser(user);
        return true;
    }

    public List<User> list() {
        return userRepository.selectAll();
    }

    public void afficherListeUsers(List<User> users) {
        for (User user : users) {
            System.out.println(user);
        }
    }

    public boolean findLogin(String login) {
        return list().stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst()
                .isPresent();
    }

    public List<User> listUserActive() {
        return list().stream()
                .filter(user -> user.isActive() == true)
                .toList();
    }

    public List<User> listUserParRole(Role role) {
        return list().stream()
                .filter(user -> user.getRole().equals(role))
                .toList();
    }

    public List<User> listeUsersDispo() {
        return userRepository.selectAll().stream()
                .filter(user -> user.getClient() == null)
                .toList();
    }

    public List<User> listUserInactive() {
        return list().stream()
                .filter(user -> user.isActive() == false)
                .toList();
    }

    public User choixUserDesactiver() {
        List<User> users = listUserActive();
        if (users == null || users.isEmpty()) {
            System.out.println("Aucun utilisateur actif.");
            return null;
        }
        System.out.println("Liste des utilisateurs actifs:");
        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ". " + users.get(i));
        }
        int choix = numUserChoisi(users);
        return users.get(choix - 1);
    }

    public User choixUserActiver() {
        List<User> users = listUserInactive();
        if (users == null || users.isEmpty()) {
            System.out.println("Aucun utilisateur actif.");
            return null;
        }
        System.out.println("Liste des utilisateurs inactifs :");
        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ". " + users.get(i));
        }
        int choix = numUserChoisi(users);
        return users.get(choix - 1);
    }

    public void activerUtilisateur(User user) {
        user.setActive(true);
    }

    public void desactiverUtilisateur(User user) {
        user.setActive(true);
    }

    public static int numUserChoisi(List<User> userYi) {
        int choix = -1;
        Scanner scanner = Main.getScanner();
        while (true) {
            System.out.print("Veuillez choisir un client (entrez le numéro correspondant) : ");
            try {
                choix = scanner.nextInt();
                if (choix < 1 || choix > userYi.size()) {
                    System.out.println(
                            "Erreur : choix invalide. Veuillez entrer un numéro entre 1 et " + userYi.size() + ".");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Erreur : saisie invalide. Veuillez entrer un nombre.");
                scanner.nextLine();
            }
        }
        return choix;
    }

    @Override
    public void save(User element) {
        userRepository.insert(element);
    }

    @Override
    public boolean update(User element) {
        return userRepository.update(element);
    }

    @Override
    public void selectById(int id) {
        userRepository.findById(id);
    }

    @Override
    public List<User> selectAll() {
        return userRepository.selectAll();
    }

    @Override
    public User findUser(String nom) {
        return list().stream()
                .filter(user -> user.getNom().compareTo(nom) == 0)
                .findFirst()
                .orElse(null);
    }

    public User findUsersByLoginAndPassword(String login, String password, Role role) {
        return userRepository.findAll().stream()
            .filter(user -> user.getLogin().equals(login) &&
                            user.getPassword().equals(password) &&
                            user.getRole() == role)
            .findFirst()
            .orElse(null);
    }

    public List<User> getUsersActifs() {
        List<User> allUsers = userRepository.selectAll();
        return allUsers.stream()
                        .filter(User::isActive)
                        .collect(Collectors.toList());
    }

    public List<User> listUsersByRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Le rôle ne peut pas être nul !");
        }
        return userRepository.selectAll().stream()
                .filter(user -> user.getRole() == role)
                .toList();
    }
}