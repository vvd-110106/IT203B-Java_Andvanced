package Session07.Ex5;

import java.util.*;

class Product {
    String id, name, category;
    double price;
    public Product(String id, String name, double price, String category) {
        this.id = id; this.name = name; this.price = price; this.category = category;
    }
}

class Customer {
    String name, email, phone;
    public Customer(String name, String email, String phone) {
        this.name = name; this.email = email; this.phone = phone;
    }
}

class OrderItem {
    Product product;
    int quantity;
    public OrderItem(Product product, int quantity) { this.product = product; this.quantity = quantity; }
}

class Order {
    String id;
    Customer customer;
    List<OrderItem> items = new ArrayList<>();
    double totalAmount;
    public Order(String id, Customer customer) { this.id = id; this.customer = customer; }
}

interface DiscountStrategy { double apply(double total); }
interface PaymentMethod { void pay(double amount); }
interface OrderRepository { void save(Order order); List<Order> findAll(); }
interface NotificationService { void send(String message, String recipient); }

class PercentageDiscount implements DiscountStrategy {
    private double percent;
    public PercentageDiscount(double percent) { this.percent = percent; }
    public double apply(double total) { return total * (1 - percent / 100); }
}

class CreditCardPayment implements PaymentMethod {
    public void pay(double amount) { System.out.println("Thanh toán thẻ tín dụng: " + amount); }
}

class EmailNotification implements NotificationService {
    public void send(String message, String recipient) { System.out.println("Gửi email tới " + recipient + ": " + message); }
}

class FileOrderRepository implements OrderRepository {
    private List<Order> orders = new ArrayList<>();
    public void save(Order order) { orders.add(order); System.out.println("Đã lưu đơn hàng " + order.id); }
    public List<Order> findAll() { return orders; }
}

class OrderService {
    private OrderRepository repo;
    private NotificationService notify;

    public OrderService(OrderRepository repo, NotificationService notify) {
        this.repo = repo;
        this.notify = notify;
    }

    public void processOrder(Order order, PaymentMethod payment, DiscountStrategy discount) {
        double finalPrice = discount.apply(order.totalAmount);
        payment.pay(finalPrice);
        repo.save(order);
        notify.send("Đơn hàng " + order.id + " đã tạo thành công", order.customer.email);
    }
}



public class Main {
    public static void main(String[] args) {
        OrderRepository repo = new FileOrderRepository();
        NotificationService notify = new EmailNotification();
        OrderService orderService = new OrderService(repo, notify);

        Product laptop = new Product("SP01", "Laptop", 15000000, "Điện tử");
        Customer customer = new Customer("Nguyễn Văn A", "a@example.com", "0123456789");

        Order order = new Order("ORD001", customer);
        order.items.add(new OrderItem(laptop, 1));
        order.totalAmount = 15000000;

        orderService.processOrder(order, new CreditCardPayment(), new PercentageDiscount(10));

        System.out.println("Tổng doanh thu: " + repo.findAll().stream().mapToDouble(o -> o.totalAmount).sum());
    }
}