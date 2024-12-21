package dette.boutique.services;

import dette.boutique.data.entities.User;
import dette.boutique.data.enums.Role;

public class UserConnected {
    public static User userConnected;

    public static User getUserConnected() {
        return userConnected;
    }

    public static void setUserConnected(User userConnected) {
        UserConnected.userConnected = userConnected;
    }

    public static boolean isUserConnected() {
        return userConnected != null;
    }

    public static boolean isAdmin() {
        return userConnected != null && userConnected.getRole() != null && userConnected.getRole() == Role.ADMIN;
    }

    public static boolean isBoutiquier() {
        return userConnected != null && userConnected.getRole() != null && userConnected.getRole() == Role.BOUTIQUIER;
    }

    public static boolean isClient() {
        return userConnected != null && userConnected.getRole() != null && userConnected.getRole() == Role.CLIENT;
    }
}