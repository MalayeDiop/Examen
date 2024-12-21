package dette.boutique.views;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import dette.boutique.Main;
import dette.boutique.core.ViewImpl;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.User;
import dette.boutique.data.enums.Role;
import dette.boutique.services.list.ClientService;
import dette.boutique.services.list.DetteService;
import dette.boutique.services.list.UserService;

public class ClientView extends ViewImpl<Client> {
    private ClientService clientService;
    private DetteService detteService;
    private UserService userService;
    public UserView userView;
    public DetteView detteView;
    public ArticleView articleView;
    public DetailView detailView;

    public ClientView(ClientService clientService, DetteService detteService, ArticleView articleView, 
                DetteView detteView, DetailView detailView, UserService userService) {
        this.clientService = clientService;
        this.detteService = detteService;
        this.articleView = articleView;
        this.detteView = detteView;
        this.detailView = detailView;
        this.userService = userService;
    }

    public void menu() {
        System.out.println("1. Ajouter un client");
        System.out.println("2. Afficher tous les clients");
        System.out.println("3. Trouver un client avec son numéro de téléphone");
        System.out.println("4. Ajouter une dette à un client");
        System.out.println("5. Lister les dettes non soldées d'un client");
        System.out.println("6. Quitter");
    }

    public void menuClient() {
        while (true) {
            menu();
            int choix = obtenirChoixUser(1, 6);
            switch (choix) {
                case 1:
                    create();
                    break;
                case 2:
                    List<Client> clients = clientService.selectAll();
                    System.out.println("-----Liste des clients-----");
                    for (Client client : clients) {
                        System.out.println(client);
                    }
                    System.out.println("Fin de Liste");
                    break;
                case 3:
                    Client client = findClient();
                    if (client != null) {
                        System.out.println(client);
                    } else {
                        System.out.println("Client introuvable.");
                    }
                    break;
                case 4:
                    detteView.ajouterDette();
                    break;
                case 5:
                    detteView.listerDettesClient();
                    break;
                case 6:
                    System.out.println("Sortie du menu client.");
                    return;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }

    @Override
    public void create() {
        String surname = saisieSurname();
        String adresse = saisieAdresse();
        String telephone = saisieTelephone();
        Client client = new Client();
        client.setSurname(surname);
        client.setTelephone(telephone);
        client.setAdresse(adresse);
        // clientService.createWithoutUser(client);
        System.out.print("Souhaitez-vous associer un compte utilisateur à ce client ? (oui/non) : ");
        String reponseCompte = Main.getScanner().nextLine();
        if (reponseCompte.equalsIgnoreCase("oui")) {
            clientService.save(client);
            System.out.println("\n--- Création d'un compte utilisateur pour le client ---");
            String prenom = saisiePrenom();
            String nom = saisieNom();
            String login = saisieLogin();
            String password = saisiePassword();
            Role role = Role.CLIENT;
            User user = new User(prenom, nom, login, password, role);
            user.setClient(client);
            client.setUser(user);
            userService.create(user);
            // clientService.updateClient(client.getId(), user.getId());
            System.out.println("Compte utilisateur créé et associé au client avec succès !");
        } else {
            clientService.createWithoutUser(client);
        }
        System.out.print("Souhaitez-vous ajouter une dette à ce client ? (oui/non) : ");
        String reponseDette = Main.getScanner().nextLine();
        if (reponseDette.equalsIgnoreCase("oui")) {
            detteView.ajouterDette();
        }
        clientService.save(client);
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

    public String saisieSurname() {
        Scanner scanner = Main.getScanner();
        String surname = "";
        boolean isValide = false;
        while (!isValide) {
            System.out.println("Veuillez saisir le surname  du client:");
            surname = scanner.nextLine().trim();
            try {
                if (surname.isEmpty()) {
                    System.out.println("Erreur : le nom ne doit pas être vide.");
                } else if (!surname.matches("[a-zA-Z]+")) {
                    System.out.println("Erreur : le nom ne doit contenir que des lettres.");
                } else {
                    isValide = true;
                    System.out.println("Surname accepté !");
                }
            } catch (InputMismatchException e) {
                System.out.println("Veuillez saisir des caractères valides.");
                scanner.nextLine();
            }
        }
        return surname;
    }

    public String saisieTelephone() {
        Scanner scanner = Main.getScanner();
        String tel = "";
        boolean isValide = false;
        while (!isValide) {
            System.out.println("Veuillez saisir le numéro du client:");
            tel = scanner.nextLine().trim();
            try {
                if (tel.isEmpty()) {
                    System.out.println("Erreur : le numéro ne doit pas être vide.");
                } else if (tel.length() != 9) {
                    System.out.println("Erreur : le numéro doit contenir exactement 9 chiffres.");
                } else if (!tel.matches("[0-9]+")) {
                    System.out.println("Erreur : le numéro ne doit contenir que des chiffres.");
                } else if (clientService.numDispo(tel)) {
                    System.out.println("Ce numéro est déjà attribué.");
                } else {
                    isValide = true;
                    System.out.println("Numéro accepté!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Veuillez saisir des caractères valides.");
                scanner.nextLine();
            }
        }
        return tel;
    }

    public String saisieAdresse() {
        Scanner scanner = Main.getScanner();
        String adresse = "";
        boolean isValide = false;
        while (!isValide) {
            System.out.println("Veuillez saisir l'adresse  du client:");
            adresse = scanner.nextLine().trim();
            try {
                if (adresse.isEmpty()) {
                    System.out.println("Erreur : mettez quelque chose pour l'adresse.");
                } else if (!adresse.matches("[a-zA-Z]+")) {
                    System.out.println("Erreur : l'adresse ne doit contenir que des lettres.");
                } else {
                    isValide = true;
                    System.out.println("Adresse Valide !");
                }
            } catch (InputMismatchException e) {
                System.out.println("Veuillez saisir des caractères valides.");
                scanner.nextLine();
            }
        }
        return adresse;
    }

    public Client findClient() {
        Client client = null;
        Scanner scanner = Main.getScanner();
        String numero = "";
        boolean isValide = false;
        while (!isValide) {
            numero = scanner.nextLine().trim();
            System.out.println("Veuillez saisir le numéro du client à rechercher:");
            numero = scanner.nextLine();
            while (!isValide) {
                System.out.println("Veuillez saisir le numéro du client à rechercher:");
                numero = scanner.nextLine().trim();
        
                client = clientService.findClient(numero);
                if (client != null) {
                    System.out.println("Client trouvé !");
                    isValide = true;
                } else {
                    System.out.println("Client non trouvé. Veuillez réessayer.");
                }
            }
        }
        return client;
    }

    public Client choisirCLient() {
        List<Client> clients = clientService.listeClientsDispo();
        if (clients == null || clients.isEmpty()) {
            System.out.println("Aucun client disponible.");
            return null;
        } else {
            afficherList(clients);
            System.out.println("Veuillez saisir l'index du client que vous voulez choisir.");
            int choix = obtenirChoixUser(1, clients.size());
            return clients.get(choix - 1);
        }
    }
    
}