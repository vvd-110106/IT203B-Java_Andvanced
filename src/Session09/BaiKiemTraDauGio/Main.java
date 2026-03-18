package Session09.BaiKiemTraDauGio;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

abstract class Product {
    protected String id;
    protected String name;
    protected double price;

    public Product(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(double price) {
        this.price = price;
    }

    public abstract void displayInfo();
}

class PhysicalProduct extends Product {
    private double weight;

    public PhysicalProduct(String id, String name, double price, double weight) {
        super(id, name, price);
        this.weight = weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public void displayInfo() {
        System.out.println("[Physical] ID: " + id + ", Tên: " + name + ", Giá: " + price + ", Trọng lượng: " + weight + "kg");
    }
}

class DigitalProduct extends Product {
    private double size;

    public DigitalProduct(String id, String name, double price, double size) {
        super(id, name, price);
        this.size = size;
    }

    public void setSize(double size) { this.size = size; }

    @Override
    public void displayInfo() {
        System.out.println("[Digital] ID: " + id + ", Tên: " + name + ", Giá: " + price + ", Dung lượng: " + size + "MB");
    }
}

class ProductDatabase {
    private static ProductDatabase instance;
    private List<Product> products;

    private ProductDatabase() {
        products = new ArrayList<>();
    }

    public static ProductDatabase getInstance() {
        if (instance == null) {
            instance = new ProductDatabase();
        }
        return instance;
    }

    public void addProduct(Product p) {
        products.add(p);
    }

    public List<Product> getAllProducts() {
        return products;
    }

    public Product findById(String id) {
        for (Product p : products) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public boolean deleteProduct(String id) {
        return products.removeIf(p -> p.getId().equals(id));
    }
}

class ProductFactory {
    public static Product createProduct(int type, String id, String name, double price, double specificValue) {
        if (type == 1) {
            return new PhysicalProduct(id, name, price, specificValue);
        } else if (type == 2) {
            return new DigitalProduct(id, name, price, specificValue);
        }
        return null;
    }
}

public class Main {
    public static void main (String[] args) {
        Scanner sc = new Scanner(System.in);
        ProductDatabase db = ProductDatabase.getInstance();
        int choice;
        do {
            System.out.println("---------------------- QUẢN LÝ SẢN PHẨM----------------------");
            System.out.println("1. Thêm mới sản phẩm");
            System.out.println("2. Xem danh sách sản phẩm");
            System.out.println("3. Cập nhật thông tin sản phẩm");
            System.out.println("4. Xóa sản phẩm");
            System.out.println("5. Thoát chương trình");
            System.out.println("------------------------------------------------------------------");
            System.out.print("Lựa chọn của bạn: ");
            choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1:
                    System.out.print("Chọn loại (1.Physical, 2.Digital): ");
                    int type = Integer.parseInt(sc.nextLine());
                    System.out.print("Nhập ID: ");
                    String id = sc.nextLine();
                    System.out.print("Nhập tên: ");
                    String name = sc.nextLine();
                    System.out.print("Nhập giá: ");
                    double price = Double.parseDouble(sc.nextLine());
                    System.out.print(type == 1 ? "Nhập trọng lượng (kg): " : "Nhập dung lượng (MB): ");
                    double spec = Double.parseDouble(sc.nextLine());

                    Product p = ProductFactory.createProduct(type, id, name, price, spec);
                    if (p != null) {
                        db.addProduct(p);
                    }
                    break;
                case 2:
                    for (Product product : db.getAllProducts()) {
                        product.displayInfo();
                    }
                    break;
                case 3:
                    System.out.print("Nhập ID cần cập nhật: ");
                    String updateId = sc.nextLine();
                    Product updateP = db.findById(updateId);
                    if (updateP != null) {
                        System.out.print("Tên mới: ");
                        updateP.setName(sc.nextLine());
                        System.out.print("Giá mới: ");
                        updateP.setPrice(Double.parseDouble(sc.nextLine()));
                        if (updateP instanceof PhysicalProduct) {
                            System.out.print("Trọng lượng mới: ");
                            ((PhysicalProduct) updateP).setWeight(Double.parseDouble(sc.nextLine()));
                        } else {
                            System.out.print("Dung lượng mới: ");
                            ((DigitalProduct) updateP).setSize(Double.parseDouble(sc.nextLine()));
                        }
                    } else {
                        System.out.println("Không tìm thấy");
                    }
                    break;
                case 4:
                    System.out.print("Nhập ID cần xoá: ");
                    String delId = sc.nextLine();
                    if (db.deleteProduct(delId)) {
                        System.out.println("Đã xoá thành công.");
                    } else {
                        System.out.println("Không tìm thấy");
                    }
                    break;
                case 5:
                    System.out.println("Bạn đã thoát khỏi chương trình");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ");
            }
        } while (choice != 5);
    }
}
