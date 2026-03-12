package Session05.KiemTraDauGio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

class InvalidProductException extends RuntimeException {
    public InvalidProductException(String message) {
        super(message);
    }
}

class Product {
    private int id;
    private String name;
    private double price;
    private int quantity;
    private String category;

    public Product() {}
    public Product(int id, String name, double price, int quantity, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
}
class ProductManager {
    private List<Product> list = new ArrayList<>();
    public void addProduct(Product p) {
        boolean exists = list.stream().anyMatch(item -> item.getId() == p.getId());
        if (exists) {
            throw new InvalidProductException("Mã ID sản phẩm đã tồn tại trong danh sách.");
        }
        list.add(p);
    }
    public void displayAll() {
        System.out.printf("%-5s %-15s %-10s %-10s %-15s\n", "ID", "Tên", "Giá", "Số lượng", "Danh mục");
        list.forEach(p -> System.out.printf("%-5d %-15s %-10.2f %-10d %-15s\n",
                p.getId(), p.getName(), p.getPrice(), p.getQuantity(), p.getCategory()));
    }
    public void updateQuantity(int id, int newQuantity) {
        Optional<Product> found = list.stream().filter(p -> p.getId() == id).findFirst();
        if (found.isPresent()) {
            found.get().setQuantity(newQuantity);
        } else {
            throw new InvalidProductException("Không tìm thấy ID sản phẩm để cập nhật.");
        }
    }
    public void delete() {
        list.removeIf(p -> p.getQuantity() == 0);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ProductManager manager = new ProductManager();
        int choice = -1;
        while (choice != 5) {
            System.out.println("========= PRODUCTMANAGEMENTSYSTEM=========");
            System.out.println("1. Thêm sản phẩm mới");
            System.out.println("2. Hiêển thị danh sách sản phẩm");
            System.out.println("3. Cập nhật số lượng theo ID");
            System.out.println("4. Xóa sản phẩm đã hết hàng");
            System.out.println("5. Thoát chương trình");
            System.out.println("=============================================");
            System.out.print("Lựa chọn của bạn là:");
            choice = sc.nextInt();
            try {
                switch (choice) {
                    case 1:
                        System.out.print("Nhập vào Id sản phẩm: ");
                        int id = sc.nextInt();
                        sc.nextLine();
                        System.out.print("Nhập vào tên sản phẩm: ");
                        String name = sc.next();
                        System.out.print("Nhập vào giá sản phẩm: ");
                        double price = sc.nextDouble();
                        System.out.print("Nhập vào số lượng sản phẩm: ");
                        int quantity = sc.nextInt();
                        System.out.print("Nhập vào danh mục sản phẩm: ");
                        String category = sc.nextLine();
                        manager.addProduct(new Product(id, name, price, quantity, category));
                        break;
                    case 2:
                        manager.displayAll();
                        break;
                    case 3:
                        System.out.print("Nhập Id cần cập nhật: ");
                        int updateId = sc.nextInt();
                        System.out.print("Nhập số luượng sản phẩm mới: ");
                        int updateQuantity = sc.nextInt();
                        manager.updateQuantity(updateId, updateQuantity);
                        break;
                    case 4:
                        manager.delete();
                        System.out.println("Đã xóa những sản phẩm có só lượng bằng 0");
                        break;
                    case 5:
                        System.out.println("Kết thúc chương trình.");
                        break;
                }
            } catch (InvalidProductException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
