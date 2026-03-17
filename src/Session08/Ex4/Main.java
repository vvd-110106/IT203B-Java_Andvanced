package Session08.Ex4;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface Observer {
    void update(int temperature);
}

interface Subject {
    void attach(Observer o);
    void detach(Observer o);
    void notifyObservers();
}

class TemperatureSensor implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private int temperature;

    public void setTemperature(int temperature) {
        this.temperature = temperature;
        System.out.println("Cảm biến: Nhiệt độ = " + temperature);
        notifyObservers();
    }

    @Override
    public void attach(Observer o) {
        observers.add(o);
    }

    @Override
    public void detach(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(temperature);
        }
    }
}

class Fan implements Observer {
    @Override
    public void update(int temperature) {
        if (temperature < 20) {
            System.out.println("Quạt: Nhiệt độ thấp, tự động TẮT");
        } else if (temperature <= 25) {
            System.out.println("Quạt: Nhiệt độ ổn định, chạy tốc độ trung bình");
        } else {
            System.out.println("Quạt: Nhiệt độ cao, chạy tốc độ mạnh");
        }
    }
}

class Humidifier implements Observer {
    @Override
    public void update(int temperature) {
        System.out.println("Máy tạo ẩm: Điều chỉnh độ ẩm cho nhiệt độ " + temperature);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TemperatureSensor sensor = new TemperatureSensor();
        Fan fan = new Fan();
        Humidifier humidifier = new Humidifier();

        while (true) {
            System.out.println("\n1. Đăng ký thiết bị | 2. Set nhiệt độ | 3. Hủy đăng ký | 4. Thoát");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.println("1. Quạt | 2. Máy tạo ẩm");
                    int device = sc.nextInt();
                    switch (device) {
                        case 1:
                            sensor.attach(fan);
                            System.out.println("Quạt: Đã đăng ký nhận thông báo");
                            break;
                        case 2:
                            sensor.attach(humidifier);
                            System.out.println("Máy tạo ẩm: Đã đăng ký nhận thông báo");
                            break;
                    }
                    break;
                case 2:
                    System.out.print("Nhập nhiệt độ mới: ");
                    sensor.setTemperature(sc.nextInt());
                    break;
                case 3:
                    System.out.println("1. Quạt | 2. Máy tạo ẩm");
                    int detachDevice = sc.nextInt();
                    switch (detachDevice) {
                        case 1: sensor.detach(fan); break;
                        case 2: sensor.detach(humidifier); break;
                    }
                    System.out.println("Đã hủy đăng ký.");
                    break;
                case 4:
                    return;
            }
        }
    }
}