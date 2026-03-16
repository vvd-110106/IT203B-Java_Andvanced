package Session07.Ex6;

import java.util.Scanner;

interface DiscountStrategy { String apply(double amount); }
interface PaymentMethod { void pay(double amount); }
interface NotificationService { void notify(String message); }

interface SalesChannelFactory {
    DiscountStrategy createDiscount();
    PaymentMethod createPayment();
    NotificationService createNotification();
}

class WebsiteFactory implements SalesChannelFactory {
    public DiscountStrategy createDiscount() { return a -> "Giảm 10% (WEB10) -> " + (a * 0.9); }
    public PaymentMethod createPayment() { return a -> System.out.println("Thanh toán Credit Card online: " + a); }
    public NotificationService createNotification() { return m -> System.out.println("Email: " + m); }
}

class MobileAppFactory implements SalesChannelFactory {
    public DiscountStrategy createDiscount() { return a -> "Giảm 15% (Lần đầu) -> " + (a * 0.85); }
    public PaymentMethod createPayment() { return a -> System.out.println("Thanh toán MoMo tích hợp: " + a); }
    public NotificationService createNotification() { return m -> System.out.println("Push Notification: " + m); }
}

class StorePOSFactory implements SalesChannelFactory {
    public DiscountStrategy createDiscount() { return a -> "Giảm 5% (Thành viên) -> " + (a * 0.95); }
    public PaymentMethod createPayment() { return a -> System.out.println("Thanh toán tiền mặt POS: " + a); }
    public NotificationService createNotification() { return m -> System.out.println("In hóa đơn giấy: " + m); }
}

class OrderProcessor {
    public void process(SalesChannelFactory factory, double amount) {
        System.out.println(factory.createDiscount().apply(amount));
        factory.createPayment().pay(amount);
        factory.createNotification().notify("Đơn hàng đã thành công!");
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        OrderProcessor processor = new OrderProcessor();

        System.out.println("Chọn kênh: 1.Website, 2.MobileApp, 3.POS");
        int choice = sc.nextInt();

        SalesChannelFactory factory = switch (choice) {
            case 1 -> new WebsiteFactory();
            case 2 -> new MobileAppFactory();
            case 3 -> new StorePOSFactory();
            default -> null;
        };

        if (factory != null) {
            processor.process(factory, 1000000);
        }
    }
}