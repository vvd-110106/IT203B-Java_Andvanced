package Session05.MiniProject;

import Session05.MiniProject.model.Drink;
import Session05.MiniProject.model.Food;
import Session05.MiniProject.model.MenuItem;
import Session05.MiniProject.model.Order;
import Session05.MiniProject.service.MenuService;
import Session05.MiniProject.service.OrderService;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    private static MenuService menuService = new MenuService();
    private static OrderService orderService = new OrderService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        prepareSampleData();
        while (true) {
            System.out.println("1. Manage menu");
            System.out.println("2. Manage orders");
            System.out.println("3. Statistics");
            System.out.println("0. Exit");
            System.out.print("Choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    menuMenu();
                    break;
                case "2":
                    orderMenu();
                    break;
                case "3":
                    statisticsMenu();
                    break;
                case "0":
                    System.exit(0);
                default:
                    System.out.println("Invalid");
            }
        }
    }

    private static void prepareSampleData() {
        menuService.addItem(new Food("F1", "Burger", 5.0, 20));
        menuService.addItem(new Food("F2", "Pizza", 8.0, 10));
        menuService.addItem(new Drink("D1", "Coke", 2.0, 30, Drink.Size.M));
    }

    // Implement menu interactions quickly
    private static void menuMenu() {
        System.out.println("--- Menu Management ---");
        System.out.println("1. Add item");
        System.out.println("2. List items");
        System.out.print("Choice: ");
        String c = scanner.nextLine();
        switch (c) {
            case "1":
                System.out.print("Id: ");
                String id = scanner.nextLine();
                System.out.print("Name: ");
                String name = scanner.nextLine();
                System.out.print("Price: ");
                double price = Double.parseDouble(scanner.nextLine());
                System.out.print("Stock: ");
                int stock = Integer.parseInt(scanner.nextLine());
                menuService.addItem(new Food(id, name, price, stock));
                System.out.println("Added");
                break;
            case "2":
                menuService.getAll().forEach(System.out::println);
                break;
            default:
                break;
        }
    }

    private static void orderMenu() {
        System.out.println("--- Order Management ---");
        System.out.println("1. Create order");
        System.out.println("2. Add item to order");
        System.out.println("3. Show orders");
        System.out.print("Choice: ");
        String c = scanner.nextLine();
        try {
            switch (c) {
                case "1":
                    Order o = orderService.createOrder();
                    System.out.println("Created order " + o.getId());
                    break;
                case "2":
                    System.out.print("Order id: ");
                    String oid = scanner.nextLine();
                    System.out.print("Item id: ");
                    String iid = scanner.nextLine();
                    System.out.print("Qty: ");
                    int qty = Integer.parseInt(scanner.nextLine());
                    MenuItem item = menuService.findById(iid).orElse(null);
                    if (item == null) {
                        System.out.println("Item not found");
                        break;
                    }
                    orderService.addItemToOrder(oid, item, qty);
                    System.out.println("Added");
                    break;
                case "3":
                    orderService.getAllOrders().forEach(o2 -> {
                        System.out.println(o2.getId() + " status=" + o2.getStatus() + " total=" + o2.calculateTotal());
                    });
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void statisticsMenu() {
        System.out.print("Date (yyyy-mm-dd): ");
        String s = scanner.nextLine();
        LocalDate d = LocalDate.parse(s);
        double revenue = orderService.revenueForDate(d);
        System.out.println("Revenue: " + revenue);
    }
}
