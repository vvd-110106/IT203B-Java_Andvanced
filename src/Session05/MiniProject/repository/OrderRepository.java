package Session05.MiniProject.repository;

import Session05.MiniProject.model.Order;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderRepository {
    private List<Order> orders = new ArrayList<>();

    public void add(Order order) {
        orders.add(order);
    }

    public Optional<Order> findById(String id) {
        return orders.stream().filter(o -> o.getId().equals(id)).findFirst();
    }

    public List<Order> getAll() {
        return orders;
    }

    public double totalRevenueForDate(LocalDate date) {
        return orders.stream()
                .filter(o -> o.getCreatedAt().toLocalDate().equals(date) && o.getStatus() == Order.Status.PAID)
                .mapToDouble(Order::calculateTotal)
                .sum();
    }

    public List<Order> paidOrders() {
        return orders.stream().filter(o -> o.getStatus() == Order.Status.PAID).collect(Collectors.toList());
    }
}
