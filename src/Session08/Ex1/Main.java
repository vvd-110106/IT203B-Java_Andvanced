package Session08.Ex1;

import java.util.Scanner;

class HardwareConnection {
    private static HardwareConnection instance;

    private HardwareConnection() {
        System.out.println("HardwareConnection: Đã kết nối phần cứng.");
    }

    public static HardwareConnection getInstance() {
        if (instance == null) {
            instance = new HardwareConnection();
        }
        return instance;
    }

    public void connect() {}
}

interface Device {
    void turnOn();
    void turnOff();
}

class Light implements Device {
    @Override
    public void turnOn() { System.out.println("Đèn: Bật sáng."); }
    @Override
    public void turnOff() { System.out.println("Đèn: Tắt."); }
}

class Fan implements Device {
    @Override
    public void turnOn() { System.out.println("Quạt: Quay."); }
    @Override
    public void turnOff() { System.out.println("Quạt: Dừng."); }
}

class AirConditioner implements Device {
    @Override
    public void turnOn() { System.out.println("Điều hòa: Làm mát."); }
    @Override
    public void turnOff() { System.out.println("Điều hòa: Nghỉ."); }
}

abstract class DeviceFactory {
    public abstract Device createDevice();
}

class LightFactory extends DeviceFactory {
    @Override
    public Device createDevice() {
        System.out.println("LightFactory: Đã tạo đèn mới.");
        return new Light();
    }
}

class FanFactory extends DeviceFactory {
    @Override
    public Device createDevice() {
        System.out.println("FanFactory: Đã tạo quạt mới.");
        return new Fan();
    }
}

class AirConditionerFactory extends DeviceFactory {
    @Override
    public Device createDevice() {
        System.out.println("AirConditionerFactory: Đã tạo điều hòa mới.");
        return new AirConditioner();
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Device currentDevice = null;
        boolean running = true;

        while (running) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Kết nối phần cứng");
            System.out.println("2. Tạo thiết bị mới");
            System.out.println("3. Bật thiết bị");
            System.out.println("4. Thoát");
            System.out.print("Chọn: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    HardwareConnection.getInstance();
                    break;
                case 2:
                    System.out.print("Chọn loại (1. Đèn, 2. Quạt, 3. Điều hòa): ");
                    int type = sc.nextInt();
                    DeviceFactory factory = null;
                    switch (type) {
                        case 1: factory = new LightFactory(); break;
                        case 2: factory = new FanFactory(); break;
                        case 3: factory = new AirConditionerFactory(); break;
                        default: System.out.println("Loại không hợp lệ!");
                    }
                    if (factory != null) currentDevice = factory.createDevice();
                    break;
                case 3:
                    if (currentDevice != null) currentDevice.turnOn();
                    else System.out.println("Chưa có thiết bị!");
                    break;
                case 4:
                    running = false;
                    break;
                default:
                    System.out.println("Lựa chọn sai!");
            }
        }
        sc.close();
    }
}