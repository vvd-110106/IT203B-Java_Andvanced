package Session08.Ex6;

import java.util.Scanner;

interface DiscountStrategy {
    double applyDiscount(double amount);
}

interface PaymentMethod {
    void processPayment(double amount);
}

interface NotificationService {
    void notifyUser(String message);
}

class WebsiteDiscount implements DiscountStrategy {
    public double applyDiscount(double amount) { return amount * 0.1; }
}

class CreditCardPayment implements PaymentMethod {
    public void processPayment(double amount) {
        System.out.println("Xử lý thanh toán thẻ tín dụng: " + (long)amount);
    }
}

class EmailNotification implements NotificationService {
    public void notifyUser(String message) { System.out.println("Gửi email: " + message); }
}

class FirstTimeDiscount implements DiscountStrategy {
    public double applyDiscount(double amount) { return amount * 0.15; }
}

class MomoPayment implements PaymentMethod {
    public void processPayment(double amount) {
        System.out.println("Xử lý thanh toán MoMo: " + (long)amount);
    }
}

class PushNotification implements NotificationService {
    public void notifyUser(String message) { System.out.println("Gửi push notification: " + message); }
}

class MemberDiscount implements DiscountStrategy {
    public double applyDiscount(double amount) { return amount * 0.05; }
}

class CODPayment implements PaymentMethod {
    public void processPayment(double amount) {
        System.out.println("Thanh toán tiền mặt tại quầy: " + (long)amount);
    }
}

class PrintReceipt implements NotificationService {
    public void notifyUser(String message) { System.out.println("In hóa đơn: " + message); }
}

interface SalesChannelFactory {
    DiscountStrategy createDiscount();
    PaymentMethod createPayment();
    NotificationService createNotification();
}

class WebsiteFactory implements SalesChannelFactory {
    public DiscountStrategy createDiscount() { return new WebsiteDiscount(); }
    public PaymentMethod createPayment() { return new CreditCardPayment(); }
    public NotificationService createNotification() { return new EmailNotification(); }
}

class MobileAppFactory implements SalesChannelFactory {
    public DiscountStrategy createDiscount() { return new FirstTimeDiscount(); }
    public PaymentMethod createPayment() { return new MomoPayment(); }
    public NotificationService createNotification() { return new PushNotification(); }
}

class POSFactory implements SalesChannelFactory {
    public DiscountStrategy createDiscount() { return new MemberDiscount(); }
    public PaymentMethod createPayment() { return new CODPayment(); }
    public NotificationService createNotification() { return new PrintReceipt(); }
}

class OrderService {
    private DiscountStrategy discount;
    private PaymentMethod payment;
    private NotificationService notification;

    public OrderService(SalesChannelFactory factory) {
        this.discount = factory.createDiscount();
        this.payment = factory.createPayment();
        this.notification = factory.createNotification();
    }

    public void processOrder(String product, double price) {
        double discountVal = discount.applyDiscount(price);
        double total = price - discountVal;
        System.out.println("Sản phẩm: " + product + " | Giá gốc: " + (long)price);
        System.out.println("Giảm giá: " + (long)discountVal);
        payment.processPayment(total);
        notification.notifyUser("Đơn hàng thành công");
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- HỆ THỐNG BÁN HÀNG ---");
            System.out.println("1. Website\n2. Mobile App\n3. POS\n4. Thoát");
            int choice = sc.nextInt();
            SalesChannelFactory factory = null;

            switch (choice) {
                case 1: factory = new WebsiteFactory(); break;
                case 2: factory = new MobileAppFactory(); break;
                case 3: factory = new POSFactory(); break;
                case 4: return;
            }

            if (factory != null) {
                OrderService service = new OrderService(factory);
                System.out.print("Tên sản phẩm: ");
                sc.nextLine();
                String name = sc.nextLine();
                System.out.print("Giá: ");
                double price = sc.nextDouble();
                service.processOrder(name, price);
            }
        }
    }
}