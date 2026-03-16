package Session07.Ex2;

interface DiscountStrategy {
    double applyDiscount(double totalAmount);
}

class PercentageDiscount implements DiscountStrategy {
    private double percentage;
    public PercentageDiscount(double percentage) { this.percentage = percentage; }
    public double applyDiscount(double totalAmount) { return totalAmount * (1 - percentage / 100); }
}

class FixedDiscount implements DiscountStrategy {
    private double amount;
    public FixedDiscount(double amount) { this.amount = amount; }
    public double applyDiscount(double totalAmount) { return totalAmount - amount; }
}

class NoDiscount implements DiscountStrategy {
    public double applyDiscount(double totalAmount) { return totalAmount; }
}

class HolidayDiscount implements DiscountStrategy {
    private double percentage;
    public HolidayDiscount(double percentage) { this.percentage = percentage; }
    public double applyDiscount(double totalAmount) { return totalAmount * (1 - percentage / 100); }
}

class OrderCalculator {
    private DiscountStrategy strategy;

    public void setDiscountStrategy(DiscountStrategy strategy) {
        this.strategy = strategy;
    }

    public double calculate(double totalAmount) {
        return strategy.applyDiscount(totalAmount);
    }
}

public class Main {
    public static void main(String[] args) {
        double total = 1000000;
        OrderCalculator calculator = new OrderCalculator();

        calculator.setDiscountStrategy(new PercentageDiscount(10));
        System.out.println("Số tiền sau giảm (10%): " + (long)calculator.calculate(total));

        calculator.setDiscountStrategy(new FixedDiscount(50000));
        System.out.println("Số tiền sau giảm (50.000đ): " + (long)calculator.calculate(total));

        calculator.setDiscountStrategy(new NoDiscount());
        System.out.println("Số tiền sau giảm (Không giảm): " + (long)calculator.calculate(total));

        calculator.setDiscountStrategy(new HolidayDiscount(15));
        System.out.println("Số tiền sau giảm (Holiday 15%): " + (long)calculator.calculate(total));
    }
}