package Session05.MiniProject.model;

public class Food extends MenuItem {
    public Food(String id, String name, double price, int stock) {
        super(id, name, price, stock);
    }

    @Override
    public double calculatePrice() {
        // base price
        return getPrice();
    }
}
