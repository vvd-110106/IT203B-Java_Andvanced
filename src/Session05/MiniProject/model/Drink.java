package Session05.MiniProject.model;

public class Drink extends MenuItem {
    public enum Size {
        S, M, L
    }

    private Size size;

    public Drink(String id, String name, double basePrice, int stock, Size size) {
        super(id, name, basePrice, stock);
        this.size = size;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    @Override
    public double calculatePrice() {
        double price = getPrice();
        switch (size) {
            case M:
                price += 1.0;
                break;
            case L:
                price += 2.0;
                break;
            default:
                break;
        }
        return price;
    }
}
