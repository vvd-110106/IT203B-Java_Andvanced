package Session05.MiniProject.model;

public abstract class MenuItem {
    private String id;
    private String name;
    private double price;
    private int stock;

    public MenuItem(String id, String name, double price, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void reduceStock(int amount) throws IllegalArgumentException {
        if (amount < 0 || amount > stock) {
            throw new IllegalArgumentException("Invalid stock reduction");
        }
        stock -= amount;
    }

    public abstract double calculatePrice();

    @Override
    public String toString() {
        return String.format("[%s] %s - $%.2f (stock=%d)", id, name, calculatePrice(), stock);
    }
}
