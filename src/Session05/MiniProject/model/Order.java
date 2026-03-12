package Session05.MiniProject.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Order {
    public enum Status {
        PENDING, PAID, CANCELLED
    }

    private String id;
    private List<OrderItem> items = new ArrayList<>();
    private Status status = Status.PENDING;
    private double discount; // absolute
    private double serviceFee;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Order(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void addItem(MenuItem menuItem, int quantity) {
        items.add(new OrderItem(menuItem, quantity));
    }

    public double calculateTotal() {
        double sub = items.stream().mapToDouble(OrderItem::getTotalPrice).sum();
        double total = sub - discount + serviceFee;
        return total;
    }

    public Optional<OrderItem> findItem(String menuItemId) {
        return items.stream().filter(i -> i.getItem().getId().equals(menuItemId)).findFirst();
    }
}
