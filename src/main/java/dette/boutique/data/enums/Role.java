package dette.boutique.data.enums;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public enum Role {
    ADMIN(1),
    BOUTIQUIER(2),
    CLIENT(3);

    private int roleId;

    Role(int roleId) {
        this.roleId = roleId;
    }

    public int getId() {
        return roleId;
    }

    public static Role fromId(int id) {
        for (Role role : Role.values()) {
            if (role.roleId == id) {
                return role;
            }
        }
        throw new IllegalArgumentException("Aucun rôle trouvé pour l'ID : " + id);
    }

    public static Role getValue(String value) {
        for (Role r : Role.values()) {
            if (r.name().compareToIgnoreCase(value) == 0) {
                return r;
            }
        }
        return null;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public static List<Role> listerRole() {
        return Arrays.asList(Role.values());
    }

    public static Role choisirRole() {
        List<Role> rolesDisponibles = listerRole();
        System.out.println("Liste des rôles disponibles :");
        for (int i = 0; i < rolesDisponibles.size(); i++) {
            System.out.println((i + 1) + ". " + rolesDisponibles.get(i));
        }
        Scanner scanner = new Scanner(System.in);
        int choix = numRoleChoisi(rolesDisponibles, scanner);
        return rolesDisponibles.get(choix - 1);
    }
    
    public static int numRoleChoisi(List<Role> roleYi, Scanner scanner) {
        int choix = -1;

        while (true) {
            System.out.print("Veuillez choisir un rôle (entrez le numéro correspondant) : ");
            try {
                choix = scanner.nextInt();
                if (choix < 1 || choix > roleYi.size()) {
                    System.out.println(
                            "Erreur : choix invalide. Veuillez entrer un numéro entre 1 et " + roleYi.size() + ".");
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

}