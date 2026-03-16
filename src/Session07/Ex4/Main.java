package Session07.Ex4;

import java.util.ArrayList;
import java.util.List;

interface OrderRepository {
    void save(String orderId);
    List<String> findAll();
}

interface NotificationService {
    void send(String message, String recipient);
}

class FileOrderRepository implements OrderRepository {
    public void save(String orderId) { System.out.println("Lưu đơn hàng vào file: " + orderId); }
    public List<String> findAll() { return new ArrayList<>(); }
}

class DatabaseOrderRepository implements OrderRepository {
    public void save(String orderId) { System.out.println("Lưu đơn hàng vào database: " + orderId); }
    public List<String> findAll() { return new ArrayList<>(); }
}

class EmailService implements NotificationService {
    public void send(String message, String recipient) { System.out.println("Gửi email: " + message); }
}

class SMSNotification implements NotificationService {
    public void send(String message, String recipient) { System.out.println("Gửi SMS: " + message); }
}

class OrderService {
    private final OrderRepository repository;
    private final NotificationService notification;

    public OrderService(OrderRepository repository, NotificationService notification) {
        this.repository = repository;
        this.notification = notification;
    }

    public void createOrder(String orderId, String recipient) {
        repository.save(orderId);
        notification.send("Đơn hàng " + orderId + " đã được tạo", recipient);
    }
}

public class Main {
    public static void main(String[] args) {
        OrderService orderService;

        orderService = new OrderService(new FileOrderRepository(), new EmailService());
        orderService.createOrder("ORD001", "user@example.com");

        System.out.println("--- Chuyển đổi sang Database & SMS ---");

        orderService = new OrderService(new DatabaseOrderRepository(), new SMSNotification());
        orderService.createOrder("ORD002", "0909123456");
    }
}