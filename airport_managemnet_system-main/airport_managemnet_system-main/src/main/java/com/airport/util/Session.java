package com.airport.util;

public class Session {
    private static String username;
    private static String role;

    public static void login(String loggedInUsername, String loggedInRole) {
        username = loggedInUsername;
        role = loggedInRole;
    }

    public static void logout() {
        username = null;
        role = null;
    }

    public static String getUsername() {
        return username;
    }

    public static String getRole() {
        return role;
    }

    public static boolean isAdmin() {
        return role != null && role.equalsIgnoreCase("ADMIN");
    }

    public static boolean isStaff() {
        return role != null && role.equalsIgnoreCase("STAFF");
    }

    public static boolean isPassenger() {
        return role != null && role.equalsIgnoreCase("PASSENGER");
    }
}