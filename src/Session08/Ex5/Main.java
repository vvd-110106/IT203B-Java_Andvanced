package Session08.Ex5;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface Observer {
    void update(int temperature);
}

interface Subject {
    void attach(Observer o);
    void notifyObservers();
}

interface Command {
    void execute();
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
    public void attach(Observer o) { observers.add(o); }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) o.update(temperature);
    }
}

class Light {
    public void off() { System.out.println("Đèn: Tắt"); }
}

class Fan implements Observer {
    public void setSpeed(String speed) { System.out.println("Quạt: Chạy tốc độ " + speed); }
    @Override
    public void update(int temperature) {
        if (temperature > 30) System.out.println("Quạt: Nhiệt độ cao (>30), tự động chạy tốc độ MẠNH");
    }
}

class AirConditioner implements Observer {
    private int temp;
    public void setTemp(int temp) {
        this.temp = temp;
        System.out.println("Điều hòa: Nhiệt độ = " + temp);
    }
    @Override
    public void update(int temperature) {
        if (temperature > 30) System.out.println("Điều hòa: Nhiệt độ phòng cao, duy trì làm mát tối đa");
    }
}

class SleepModeCommand implements Command {
    private Light light;
    private AirConditioner ac;
    private Fan fan;

    public SleepModeCommand(Light light, AirConditioner ac, Fan fan) {
        this.light = light;
        this.ac = ac;
        this.fan = fan;
    }

    @Override
    public void execute() {
        System.out.println("--- Kích hoạt SleepMode ---");
        light.off();
        ac.setTemp(28);
        fan.setSpeed("thấp");
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Light light = new Light();
        AirConditioner ac = new AirConditioner();
        Fan fan = new Fan();
        TemperatureSensor sensor = new TemperatureSensor();

        sensor.attach(ac);
        sensor.attach(fan);

        Command sleepMode = new SleepModeCommand(light, ac, fan);

        while (true) {
            System.out.println("\n1. Chế độ ngủ | 2. Thay đổi nhiệt độ | 3. Thoát");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    sleepMode.execute();
                    break;
                case 2:
                    System.out.print("Nhập nhiệt độ phòng: ");
                    sensor.setTemperature(sc.nextInt());
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Lựa chọn không hợp lệ");
            }
        }
    }
}