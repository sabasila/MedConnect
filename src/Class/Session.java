package Class;

public class Session {
    private static int userId;
    private static String role;

    public static void setUser(int id, String userRole) {
        userId = id;
        role = userRole;
    }

    public static int getUserId() {
        return userId;
    }

    public static String getRole() {
        return role;
    }

    public static boolean isDoctor() {
        return "doctor".equalsIgnoreCase(role);
    }

    public static boolean isPatient() {
        return "patient".equalsIgnoreCase(role);
    }
}
