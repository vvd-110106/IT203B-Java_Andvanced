package Session02;

interface UserActions {
    default void logActivity(String activity) {
        System.out.println("User log: " + activity);
    }
}
interface AdminActions {
    default void logActivity(String activity) {
        System.out.println("Admin log: " + activity);
    }
}
public class Ex5 implements UserActions, AdminActions {

    @Override
    public void logActivity(String activity) {
        AdminActions.super.logActivity(activity);
    }
    public static void main(String[] args) {
        Ex5 superadmin = new Ex5();
        superadmin.logActivity("Login");
    }
}