package dette.boutique.views;

import java.util.Date;
import java.util.List;

import dette.boutique.Main;
import dette.boutique.core.ViewImpl;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Details;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.Paiement;
import dette.boutique.services.list.ClientService;
import dette.boutique.services.list.DetteService;

public class DetteView extends ViewImpl<Dette> {
    private DetteService detteService;
    private ClientService clientService;
    private DetailView detailView;
    public ArticleView articleView;

    public DetteView(DetteService detteService, DetailView detailView, ClientService clientService) {
        this.detteService = detteService;
        this.detailView = detailView;
        this.clientService = clientService;
    }

    public void ajouterDette() {
        Client client = clientService.choisirClient();
        if (client == null) {
            System.out.println("Aucun client sélectionné.");
            return;
        }
        List<Details> details = detailView.saisirDetails();
        int montantTotal = details.stream().mapToInt(Details::getPrixTotal).sum();
        System.out.print("Montant total des articles : " + montantTotal + " FCFA\n");
        System.out.print("Saisissez le montant versé par le client : ");
        int montantVerse = Main.getScanner().nextInt();
        int montantRestant = montantTotal - montantVerse;
        Dette dette = new Dette();
        dette.setClient(client);
        dette.setDetails(details);
        dette.setDate(new Date());
        dette.setMontant(montantTotal);
        dette.setMontantVerse(montantVerse);
        dette.setMontantRestant(montantRestant);
        detteService.create(dette);
        client.setDettes(dette);
        System.out.println("Dette ajoutée avec succès !");
    }

    public void listerDettesClient() {
        Client client = clientService.choisirClient();
        if (client == null) {
            System.out.println("Aucun client sélectionné.");
            return;
        }
        if (client.getDettes().isEmpty()) {
            System.out.println("Le client " + client.getSurname() + " n'a aucune dette.");
            return;
        }
        for (Dette dette : client.getDettes()) {
            if (dette.getMontantRestant() == 0) {
                System.out.println("La dette " + dette.getId() + " a été payée");
            } else {
                System.out.println("----- Liste des dettes pour le client " + client.getSurname() + " -----");
                for (Dette dette1 : client.getDettes()) {
                    System.out.println("- Dette ID:" + dette1.getId() + ", Montant Total: " + dette1.getMontant() +
                                    ", Montant Versé: " + dette1.getMontantVerse() + 
                                    ", Montant Restant: " + dette1.getMontantRestant() + 
                                    ", Date: " + dette1.getDate());
                    System.out.println("  Articles :");
                    for (Details detail : dette1.getDetails()) {
                        System.out.println("  - " + detail.getArticle().getLibelle() + 
                                        " (Quantité: " + detail.getQte() + 
                                        ", Prix Total: " + detail.getPrixTotal() + ")");
                    }
                    System.out.println("------------------------------Autre dette------------------------------");
                }
            }
        }
        
        System.out.println("------------------------------------------------------------------------------------");
    }

    public Dette choisirDette() {
        List<Dette> dettes = detteService.selectAll();
        if (dettes != null && !dettes.isEmpty()) {
            System.out.println("Sélectionnez une dette :");
            for (int i = 0; i < dettes.size(); i++) {
                Dette dette = dettes.get(i);
                System.out.println((i + 1) + ". Dette de " + dette.getClient().getSurname() + " - Montant : " + dette.getMontant());
            }
            int choix = obtenirChoixUser(1, dettes.size());
            return dettes.get(choix - 1);
        }
        System.out.println("Aucune dette disponible.");
        return null;
    }

    public void effectuerPaiement() {
        Client client = clientService.choisirClient();
        if (client == null) {
            System.out.println("Aucun client sélectionné. Opération annulée.");
            return;
        }
        if (client.getDettes().isEmpty()) {
            System.out.println("Ce client n'a aucune dette.");
            return;
        }
        System.out.println("Dettes du client " + client.getSurname() + " :");
        detteService.afficherDettes(client.getDettes());
        System.out.print("Saisissez l'ID de la dette pour effectuer un paiement : ");
        int detteId = Main.getScanner().nextInt();
        Dette dette = detteService.findById(detteId);
        if (dette == null) {
            System.out.println("Dette introuvable. Opération annulée.");
            return;
        }
        System.out.println("Montant restant pour cette dette : " + dette.getMontantRestant());
        System.out.print("Saisissez le montant payé : ");
        int montantPaye = Main.getScanner().nextInt();
        if (montantPaye <= 0 || montantPaye > dette.getMontantRestant()) {
            System.out.println("Montant invalide. Le paiement ne peut pas être effectué.");
            return;
        }
        Paiement paiement = new Paiement(montantPaye, dette);
        detteService.ajouterPaiement(paiement);
        dette.setMontantVerse(dette.getMontantVerse() + montantPaye);
        dette.setMontantRestant(dette.getMontantRestant() - montantPaye);
        System.out.println("Paiement effectué avec succès !");
        System.out.println("Montant versé : " + montantPaye);
        System.out.println("Montant restant : " + dette.getMontantRestant());
    }

    public void afficherDettesParClient(Client client) {
        List<Dette> dettes = detteService.findByClient(client);
        if (dettes.isEmpty()) {
            System.out.println("Aucune dette trouvée pour ce client.");
        } else {
            System.out.println("Liste des dettes :");
            dettes.forEach(dette -> {
                System.out.println("- Date : " + dette.getDate() + 
                                   ", Montant : " + dette.getMontant() + 
                                   ", Montant restant : " + dette.getMontantRestant());
            });
        }
    }

}