package lk.ijse.busmanagementsystem.util;

import lk.ijse.busmanagementsystem.dto.UserDTO;

public class SessionManager {
    private static UserDTO loggedInUser;
    private static long loginTime;

    public static void setLoggedInUser(UserDTO user) {
        loggedInUser = user;
        loginTime = System.currentTimeMillis();
        System.out.println("Session started for: " + user.getName());
    }

    public static UserDTO getLoggedInUser() {
        return loggedInUser;
    }

    public static void clearSession() {
        if (loggedInUser != null) {
            System.out.println("Session cleared for: " + loggedInUser.getName());
        }
        loggedInUser = null;
        loginTime = 0;
    }

    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public static Integer getCurrentUserId() {
        return loggedInUser != null ? loggedInUser.getUserId() : null;
    }

    public static String getCurrentUserRole() {
        return loggedInUser != null ? loggedInUser.getRole() : null;
    }

    public static String getCurrentUserName() {
        return loggedInUser != null ? loggedInUser.getName() : null;
    }

    public static boolean isOwner() {
        return loggedInUser != null && "Owner".equalsIgnoreCase(loggedInUser.getRole());
    }

    public static boolean isManager() {
        return loggedInUser != null && "Manager".equalsIgnoreCase(loggedInUser.getRole());
    }

    public static long getSessionDuration() {
        return loggedInUser != null ? System.currentTimeMillis() - loginTime : 0;
    }

    public static void printSessionInfo() {
        if (loggedInUser != null) {
            System.out.println("=== Session Info ===");
            System.out.println("User: " + loggedInUser.getName());
            System.out.println("Role: " + loggedInUser.getRole());
            System.out.println("Duration: " + (getSessionDuration() / 1000) + " seconds");
        } else {
            System.out.println("No active session");
        }
    }
}