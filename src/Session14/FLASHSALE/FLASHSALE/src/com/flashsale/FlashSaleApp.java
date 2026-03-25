package Session14.FLASHSALE.FLASHSALE.src.com.flashsale;

import Session14.FLASHSALE.FLASHSALE.src.com.flashsale.dao.ProductDAO;
import Session14.FLASHSALE.FLASHSALE.src.com.flashsale.dao.UserDAO;
import Session14.FLASHSALE.FLASHSALE.src.com.flashsale.service.OrderService;
import Session14.FLASHSALE.FLASHSALE.src.com.flashsale.service.ReportService;
import Session14.FLASHSALE.FLASHSALE.src.com.flashsale.util.DatabaseInitializer;
import Session14.FLASHSALE.FLASHSALE.src.com.flashsale.test.ConcurrencyTest;
import java.sql.SQLException;

public class FlashSaleApp {
    public static void main(String[] args) {
        // Initialize Database Schema
        DatabaseInitializer.initialize();

        UserDAO userDAO = new UserDAO();
        ProductDAO productDAO = new ProductDAO();
        OrderService orderService = new OrderService();
        ReportService reportService = new ReportService();

        try {
            // Setup initial data (might fail if already exists, just catch)
            try {
                userDAO.addUser("john_doe", "john@example.com");
                System.out.println("User added.");
            } catch (SQLException e) {
                System.out.println("User already exists or error adding user: " + e.getMessage());
            }
            
            try {
                productDAO.addProduct("Flash Sale Item", 100.0, 10);
                System.out.println("Product added.");
            } catch (SQLException e) {
                 System.out.println("Product already exists or error adding product: " + e.getMessage());
            }

            // Simulate purchase
            System.out.println("\nPlacing order...");
            // User ID 1, Product ID 1, Quantity 1
            // Note: In a real app, we'd fetch IDs dynamically. Here assuming IDs start at 1.
            orderService.placeOrder(1, 1, 1); 
            
            // Generate Reports
            System.out.println("\nGenerating Reports...");
            reportService.getTopBuyers();
            reportService.getProductRevenue();

            // Run Concurrency Test
            // ConcurrencyTest.runTest();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
