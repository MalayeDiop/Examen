package dette.boutique.services;

import java.util.List;
import java.util.Scanner;

import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.User;
import dette.boutique.data.enums.Role;
import dette.boutique.services.list.ClientService;
import dette.boutique.services.list.UserService;
import dette.boutique.views.DetteView;
import dette.boutique.views.UserView;

public class Connexion {
    private UserService userService;
    private ClientService clientService;
    private UserView userView;

    public Connexion(UserService userService, ClientService clientService, UserView userView) {
        this.userService = userService;
        this.clientService = clientService;
        this.userView = userView;
    }

    public User seConnecter() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Page de Connexion ===");
        System.out.print("Entrez votre login : ");
        String login = scanner.nextLine();
        System.out.print("Entrez votre mot de passe : ");
        String password = scanner.nextLine();
        System.out.println("Choisissez votre rôle :");
        Role role = Role.choisirRole();
        if (role == null) {
            System.out.println("Aucun rôle sélectionné. Connexion annulée.");
            return null;
        }
        User user = userService.findUsersByLoginAndPassword(login, password, role);
        if (user != null) {
            System.out.println("Connexion réussie. Bienvenue, " + user.getLogin() + " !");
            if (role == Role.CLIENT) {
                List<Client> clients = clientService.selectAll();
                if (clients.isEmpty()) {
                    System.out.println("Aucun client disponible.");
                    return null;
                }
                System.out.println("Veuillez sélectionner votre compte client :");
                for (int i = 0; i < clients.size(); i++) {
                    System.out.println((i + 1) + ". " + clients.get(i).getSurname());
                }
                System.out.print("Choix : ");
                int choix = scanner.nextInt();
                if (choix < 1 || choix > clients.size()) {
                    System.out.println("Choix invalide. Connexion annulée.");
                    return null;
                }
                Client clientSelectionne = clients.get(choix - 1);
                user.setClient(clientSelectionne);
                System.out.println("Vous êtes connecté en tant que client : " + clientSelectionne.getSurname());
            } else if (role == Role.ADMIN) {
                userView.afficherMenuAdmin();
            }
            return user;
        } else {
            System.out.println("Échec de connexion. Vérifiez vos identifiants et votre rôle.");
            return null;
        }
    }

}