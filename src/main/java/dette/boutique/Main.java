package dette.boutique;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import dette.boutique.core.factory.impl.RepositoryFactory;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.User;
import dette.boutique.data.repository.interfaces.ArticleRepository;
import dette.boutique.data.repository.interfaces.ClientRepository;
import dette.boutique.data.repository.interfaces.DetailRepository;
import dette.boutique.data.repository.interfaces.DetteRepository;
import dette.boutique.data.repository.interfaces.UserRepository;
import dette.boutique.services.Connexion;
import dette.boutique.services.UserConnected;
import dette.boutique.services.list.ArticleService;
import dette.boutique.services.list.ClientService;
import dette.boutique.services.list.DetailService;
import dette.boutique.services.list.DetteService;
import dette.boutique.services.list.UserService;
import dette.boutique.views.ArticleView;
import dette.boutique.views.ClientView;
import dette.boutique.views.DetailView;
import dette.boutique.views.DetteView;
import dette.boutique.views.UserView;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static Scanner getScanner() {
        return scanner;
    }
    public static void main(String[] args) {
        String repositoryType = "BD";
        
        ClientRepository clientRepository = RepositoryFactory.createClientRepository(repositoryType);
        UserRepository userRepository = RepositoryFactory.createUserRepository(repositoryType);
        ArticleRepository articleRepository = RepositoryFactory.createArticleRepository(repositoryType);
        DetteRepository detteRepository = RepositoryFactory.createDetteRepository(repositoryType);
        DetailRepository detailRepository = RepositoryFactory.createDetailRepository(repositoryType);
        ClientService clientService = new ClientService(clientRepository);
        UserService userService = new UserService(userRepository);
        ArticleService articleService = new ArticleService(articleRepository);
        DetteService detteService = new DetteService(detteRepository);
        DetailService detailService = new DetailService(detailRepository);

        DetailView detailView = new DetailView(detailService, detteService, articleService);
        DetteView detteView = new DetteView(detteService, detailView, clientService);
        ArticleView articleView = new ArticleView(articleService);
        ClientView clientView = new ClientView(clientService, detteService, articleView, detteView, detailView, userService);
        UserView userView = new UserView(userService, detteService, clientService, clientView, articleView);

        Connexion connexion = new Connexion(userService, clientService, userView);

        boolean continuerApp = true;

        while (continuerApp) {
            User user = connexion.seConnecter();
            if (user != null) {
                UserConnected.setUserConnected(user);
                System.out.println("\nBienvenue, " + user.getLogin() + " (" + user.getRole() + ") !");
                boolean continuer = true;

                while (continuer) {

                    if (UserConnected.isBoutiquier()) {
                        System.out.println("\nMenu BOUTIQUIER :");
                        System.out.println("1. Section client");
                        System.out.println("2. Enregistre un paiement");
                        System.out.println("3. Quitter");
                        System.out.print("Choisissez une option : ");
                        int choix = userView.obtenirChoixUser(1, 3);
                        
                        try {
                            scanner.nextLine();

                            switch (choix) {
                                case 1:
                                    System.out.println("------Section Client------");
                                    clientView.menuClient();
                                    break;
                                case 2:
                                    detteView.effectuerPaiement(); 
                                    break; 
                                case 3:
                                    userView.afficherMenuAdmin();        
                                case 4:
                                    continuer = false;
                                    System.out.println("Quitter l'application.");
                                    break;
                            
                                default:
                                    break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Erreur : entrée invalide. Veuillez entrer un nombre entier.");
                            scanner.next();
                        }
                    } else if (UserConnected.isClient()){
                        Client client = user.getClient();

                        if (client != null) {
                            boolean continuerClient = true;
                            while (continuerClient) {
                                System.out.println("\n=== Menu Du Client ===");
                                System.out.println("1. Voir mes dettes");
                                System.out.println("2. Quitter");
                                System.out.print("Choisissez une option : ");

                                int choix = scanner.nextInt();
                                switch (choix) {
                                    case 1:
                                        detteView.afficherDettesParClient(client);
                                        break;
                                    case 2:
                                        continuerClient = false;
                                        System.out.println("Quitter l'application.");
                                        connexion.seConnecter();
                                        break;                                       
                                    default:
                                        System.out.println("Choix invalide.");
                                        break;
                                }
                            }
                        } else {
                            System.out.println("Aucun client associé. Contactez l'administrateur.");
                        }
                    } else {
                        System.out.println("Rôle non reconnu.");
                        continuer = false;
                    }
                    
                }
            }
        }
    }
}