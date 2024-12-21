package dette.boutique.views;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import dette.boutique.Main;
import dette.boutique.core.ViewImpl;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.User;
import dette.boutique.data.enums.Role;
import dette.boutique.services.list.ClientService;
import dette.boutique.services.list.DetteService;
import dette.boutique.services.list.UserService;

public class UserView extends ViewImpl<User> {
    private UserService userService;
    private DetteService detteService;
    private ClientService clientService;
    private ClientView clientView;
    private ArticleView articleView;

    public UserView(UserService userService, DetteService detteService, ClientService clientService, ClientView clientView,
                    ArticleView articleView) {
        this.userService = userService;
        this.detteService = detteService;
        this.clientService = clientService;
        this.clientView = clientView;
        this.articleView = articleView;
    }

    public String saisiePrenom() {
        Scanner scanner = Main.getScanner();
        String prenom = "";
        boolean isValide = false;
        while (!isValide) {
            System.out.println("Veuillez saisir le prénom  de l'utilisateur:");
            prenom = scanner.nextLine().trim();
            try {
                if (prenom.isEmpty()) {
                    System.out.println("Erreur : le prénom ne doit pas être vide.");
                } else if (!prenom.matches("[a-zA-Z]+")) {
                    System.out.println("Erreur : le prénom ne doit contenir que des lettres.");
                } else {
                    isValide = true;
                    System.out.println("Prénom accepté !");
                }
            } catch (InputMismatchException e) {
                System.out.println("Veuillez saisir des caractères valides.");
                scanner.nextLine();
            }
        }
        return prenom;
    }

    public String saisieNom() {
        Scanner scanner = Main.getScanner();
        String nom = "";
        boolean isValide = false;
        while (!isValide) {
            System.out.println("Veuillez saisir le nom  de l'utilisateur:");
            nom = scanner.nextLine().trim();
            try {
                if (nom.isEmpty()) {
                    System.out.println("Erreur : le nom ne doit pas être vide.");
                } else if (!nom.matches("[a-zA-Z]+")) {
                    System.out.println("Erreur : le nom ne doit contenir que des lettres.");
                } else {
                    isValide = true;
                    System.out.println("Nom accepté !");
                }
            } catch (InputMismatchException e) {
                System.out.println("Veuillez saisir des caractères valides.");
                scanner.nextLine();
            }
        }
        return nom;
    }

    public String saisieLogin() {
        Scanner scanner = Main.getScanner();
        String login = "";
        boolean isValide = false;
        while (!isValide) {
            System.out.println("Veuillez saisir le login  de l'utilisateur:");
            login = scanner.nextLine().trim();
            try {
                if (login.isEmpty()) {
                    System.out.println("Erreur : le login ne doit pas être vide.");
                } else if (login.length() < 4) {
                    System.out.println("Erreur : le login doit contenir au moins 4 caractères.");
                } else {
                    isValide = true;
                    System.out.println("Login accepté !");
                }
            } catch (InputMismatchException e) {
                System.out.println("Veuillez saisir des caractères valides.");
                scanner.nextLine();
            }
        }
        return login;
    }

    public String saisiePassword() {
        Scanner scanner = Main.getScanner();
        String password = "";
        boolean isValide = false;
        while (!isValide) {
            System.out.println("Veuillez saisir le password  de l'utilisateur:");
            password = scanner.nextLine().trim();
            try {
                if (password.isEmpty()) {
                    System.out.println("Erreur : le password ne doit pas être vide.");
                }else if (password.length() < 4) {
                    System.out.println("Erreur : le mot de passe doit contenir au moins 4 caractères.");
                } else {
                    isValide = true;
                    System.out.println("Password accepté !");
                }
            } catch (InputMismatchException e) {
                System.out.println("Veuillez saisir des caractères valides.");
                scanner.nextLine();
            }
        }
        return password;
    }

    public User choisirUser() {
        List<User> users = userService.list();
        if (users == null || users.isEmpty()) {
            System.out.println("Aucun utilisateur disponible.");
            return null;
        } else {
            afficherList(users);
            System.out.println("Veuillez saisir l'index de l'utilisateur que vous voulez choisir.");
            int choix = obtenirChoixUser(1, users.size());
            return users.get(choix - 1);
        }
    }

    public void afficherMenuAdmin() {
        boolean continuer = true;
        while (continuer) {
            System.out.println("\n--- Menu ADMIN ---");
            System.out.println("1. Créer un compte utilisateur pour un client");
            System.out.println("2. Créer un utilisateur avec rôle (Boutiquier ou Admin)");
            System.out.println("3. Activer/Désactiver un compte utilisateur");
            System.out.println("4. Afficher les utilisateurs (actifs/par rôle)");
            System.out.println("5. Gestion des articles");
            System.out.println("6. Archiver les dettes soldées");
            System.out.println("7. Quitter");
            System.out.print("Choisissez une option : ");

            int choix = Main.getScanner().nextInt();
    
            switch (choix) {
                case 1:
                    creerComptePourClient(userService, clientView, clientService);
                    break;
                case 2:
                    adminOuBoutiquier(userService);
                    break;
                case 3:
                    activerOuDesactiver(userService);
                    break;
                case 4:
                    for (User user : userService.selectAll()) {
                        System.out.println(user);
                    }
                    afficherComptes(userService);
                    break;
                case 5:
                    articleView.menuArticle();
                    break;
                case 6:
                    afficherEtArchiverDettes();
                    break;
                case 7:
                    continuer = false;
                    System.out.println("Déconnexion du menu ADMIN.");
                    break;
                default:
                    System.out.println("Choix invalide, veuillez réessayer.");
            }
        }
    }

    private void creerComptePourClient(UserService userService, ClientView clientView, ClientService clientService) {
        Client client = clientView.choisirCLient();
        if (client != null && client.getUser() == null) {
            System.out.println("Création d'un utilisateur pour le client : " + client.getSurname());
            String prenom = saisiePrenom();
            String nom = saisieNom();
            String login = saisieLogin();
            String password = saisiePassword();
            Role role = Role.CLIENT;
            User user = new User(prenom, nom, login, password, role);
            user.setClient(client);
            userService.create(user);
            clientService.updateUserForClient(client);
            System.out.println("Utilisateur créé et associé avec succès !");
        } else {
            System.out.println("Client introuvable ou déjà associé à un utilisateur.");
        }
    }

    private void adminOuBoutiquier(UserService userService) {
        String prenom = saisiePrenom();
        String nom = saisieNom();
        String login = saisieLogin();
        String password = saisiePassword();
        Role role = Role.choisirRole();
        if (role != null && (role == Role.ADMIN || role == Role.BOUTIQUIER)) {
            User user = new User(prenom, nom, login, password, role);
            userService.createWithoutUser(user);
            System.out.println("Utilisateur " + role + " créé avec succès !");
        } else {
            System.out.println("Rôle invalide ou non autorisé.");
        }
    }

    private void activerOuDesactiver(UserService userService) {
        User user = choisirUser();
        if (user != null) {
            boolean actif = user.isActive();
            user.setActive(!actif);
            System.out.println(user);
            userService.update(user);
            System.out.println("Utilisateur " + (actif ? "désactivé" : "activé") + " avec succès.");
        } else {
            System.out.println("Utilisateur non trouvé.");
        }
    }

    public void afficherListeUsers(List<User> users) {
        if (users.isEmpty()) {
            System.out.println("Aucun utilisateur trouvé.");
        } else {
            System.out.println("Liste des utilisateurs :");
            for (User user : users) {
                System.out.println("- " + user);
            }
        }
    }

    private void afficherComptes(UserService userService) {
        System.out.println("\n1. Afficher les utilisateurs actifs");
        System.out.println("2. Afficher les utilisateurs par rôle");
        int choix = obtenirChoixUser(1, 2);
        // userService.selectAll();
        if (choix == 1) {
            List<User> usersActifs = userService.getUsersActifs();
            if (usersActifs.isEmpty()) {
                System.out.println("Aucun utilisateur actif.");
            } else {
                System.out.println("=== Utilisateurs Actifs ===");
                for (User user : usersActifs) {
                    System.out.println("ID: " + user.getId() + ", Login: " + user.getLogin() + ", Rôle: " + user.getRole());
                }
            }
        } else if (choix == 2) {
            Role role = Role.choisirRole();
            List<User> usersParRole = userService.listUsersByRole(role);
            afficherListeUsers(usersParRole);
        } else {
            System.out.println("Option invalide.");
        }
    }

    public void afficherEtArchiverDettes() {
        List<Dette> dettesSoldees = detteService.getDettesSoldees();
        if (dettesSoldees.isEmpty()) {
            System.out.println("Aucune dette soldée disponible pour l'archivage.");
            return;
        }
        System.out.println("=== Dettes Soldées ===");
        for (int i = 0; i < detteService.getDettesSoldees().size(); i++) {
            Dette dette = detteService.getDettesSoldees().get(i);
            System.out.println((i + 1) + ". ID: " + dette.getId() + ", Montant: " + dette.getMontant() + ", Montant Restant: " + dette.getMontantRestant());
        }
        System.out.println("\nEntrez les numéros des dettes à archiver, ou 0 pour tout archiver : ");
        Main.getScanner().nextLine();
        String choix = Main.getScanner().nextLine();
        if ("0".equals(choix.trim())) {
            detteService.archiverDettes(dettesSoldees);
            System.out.println("Toutes les dettes soldées ont été archivées.");
        } else {
            String[] indices = choix.split(",");
            List<Dette> dettesAArchiver = new ArrayList<>();

            for (String index : indices) {
                try {
                    int idx = Integer.parseInt(index.trim()) - 1;
                    if (idx >= 0 && idx < dettesSoldees.size()) {
                        dettesAArchiver.add(dettesSoldees.get(idx));
                    } else {
                        System.out.println("Indice invalide : " + (idx + 1));
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Entrée invalide : " + index);
                }
            }
            if (!dettesAArchiver.isEmpty()) {
                detteService.archiverDettes(dettesAArchiver);
                System.out.println("Les dettes sélectionnées ont été archivées.");
            } else {
                System.out.println("Aucune dette n'a été archivée.");
            }
        }
    }

}
