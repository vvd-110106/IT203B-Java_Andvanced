package Session02;

@FunctionalInterface
interface Authenticatable {
    String getPassword();
    default boolean isAuthenticated() {
        String password = getPassword();
        return password != null && !password.isEmpty();
    }
    static String encrypt(String rawPassword) {
        return "encrypted_" + rawPassword;
    }
}
public class Ex3 {
    public static void main(String[] args) {
        Authenticatable user = () -> "12345678";

        System.out.println("Mật khẩu: " + user.getPassword());
        System.out.println("Đã xác thực: " + user.isAuthenticated());
        System.out.println("Mật khẩu mã hóa: " + Authenticatable.encrypt(user.getPassword()));
    }
}