package Session05.MiniProject.test;

import Session05.MiniProject.exception.InsufficientStockException;
import Session05.MiniProject.exception.InvalidOrderIdException;
import Session05.MiniProject.model.Food;
import Session05.MiniProject.model.Order;
import Session05.MiniProject.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderServiceTest {
    private OrderService orderService;
    private Food sampleFood;

    @BeforeEach
    public void setUp() {
        orderService = new OrderService();
        sampleFood = new Food("F1", "Burger", 5.0, 10);
    }

    @Test
    public void testCreateAndCalculateTotal() throws InvalidOrderIdException, InsufficientStockException {
        Order o = orderService.createOrder();
        orderService.addItemToOrder(o.getId(), sampleFood, 2);
        o.setServiceFee(1.0);
        o.setDiscount(0.5);
        double total = o.calculateTotal();
        Assertions.assertEquals(10 - 0.5 + 1.0, total, 0.001);
    }

    @Test
    public void testInsufficientStockThrows() {
        Order o = orderService.createOrder();
        Assertions.assertThrows(InsufficientStockException.class, () -> {
            orderService.addItemToOrder(o.getId(), sampleFood, 20);
        });
    }

    @Test
    public void testInvalidOrderIdThrows() {
        Assertions.assertThrows(InvalidOrderIdException.class, () -> {
            orderService.addItemToOrder("nonexistent", sampleFood, 1);
        });
    }

    @Test
    public void testRevenueCalculationForDate() throws InvalidOrderIdException, InsufficientStockException {
        Order o = orderService.createOrder();
        orderService.addItemToOrder(o.getId(), sampleFood, 1);
        o.setStatus(Order.Status.PAID);
        double rev = orderService.revenueForDate(o.getCreatedAt().toLocalDate());
        Assertions.assertTrue(rev > 0);
    }
}
