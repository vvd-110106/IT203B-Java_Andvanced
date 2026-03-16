package Session07.Ex3;

interface PaymentMethod {
    void process(double amount);
}

interface CODPayable extends PaymentMethod {}
interface CardPayable extends PaymentMethod {}
interface EWalletPayable extends PaymentMethod {}

class CODPayment implements CODPayable {
    public void process(double amount) {
        System.out.println("Xử lý thanh toán COD: " + amount + " - Thành công");
    }
}

class CreditCardPayment implements CardPayable {
    public void process(double amount) {
        System.out.println("Xử lý thanh toán thẻ tín dụng: " + amount + " - Thành công");
    }
}

class MomoPayment implements EWalletPayable {
    public void process(double amount) {
        System.out.println("Xử lý thanh toán MoMo: " + amount + " - Thành công");
    }
}

class PaymentProcessor {
    public void execute(PaymentMethod method, double amount) {
        method.process(amount);
    }
}

public class Main {
    public static void main(String[] args) {
        PaymentProcessor processor = new PaymentProcessor();

        PaymentMethod cod = new CODPayment();
        PaymentMethod card = new CreditCardPayment();
        PaymentMethod momo = new MomoPayment();

        processor.execute(cod, 500000);
        processor.execute(card, 1000000);
        processor.execute(momo, 750000);

        System.out.println("\nKiểm tra LSP");
        PaymentMethod payment = new CreditCardPayment();
        processor.execute(payment, 1000000);

        payment = new MomoPayment();
        processor.execute(payment, 750000);
    }
}