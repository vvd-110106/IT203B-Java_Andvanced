package Session07.Ex1;

import java.util.ArrayList;
import java.util.List;

class Product {
    String id, name;
    double price;

    public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
}

class Customer {
    String name, email;

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
    }
}

class Order {
    String orderId;
    Customer customer;
    List<Product> products = new ArrayList<>();

    public Order(String orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
    }

    public void addProduct(Product p) {
        products.add(p);
    }
}

class OrderCalculator {
    public double calculateTotal(Order order) {
        return order.products.stream().mapToDouble(p -> p.price).sum();
    }
}

class OrderRepository {
    public void save(Order order) {
        System.out.println("Đã lưu đơn hàng " + order.orderId);
    }
}

class EmailService {
    public void sendConfirmation(Order order) {
        System.out.println("Đã gửi email đến " + order.customer.email + ": Đơn hàng " + order.orderId + " đã được tạo");
    }
}

public class Main {
    public static void main(String[] args) {
        Product p1 = new Product("SP01", "Laptop", 15000000);
        Product p2 = new Product("SP02", "Chuột", 300000);
        System.out.println("Đã thêm sản phẩm SP01, SP02");

        Customer customer = new Customer("Nguyễn Văn A", "a@example.com");
        System.out.println("Đã thêm khách hàng");

        Order order = new Order("ORD001", customer);
        order.addProduct(p1);
        order.addProduct(p2);
        order.addProduct(p2);
        System.out.println("Đơn hàng ORD001 được tạo");

        OrderCalculator calculator = new OrderCalculator();
        System.out.println("Tổng tiền: " + (long)calculator.calculateTotal(order));

        OrderRepository repository = new OrderRepository();
        repository.save(order);

        EmailService emailService = new EmailService();
        emailService.sendConfirmation(order);
    }
}