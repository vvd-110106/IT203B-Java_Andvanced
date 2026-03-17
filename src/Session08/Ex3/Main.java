package Session08.Ex3;

import java.util.Scanner;
import java.util.Stack;

interface Command {
    void execute();
    void undo();
}

class Light {
    public void on() { System.out.println("Đèn: Bật sáng."); }
    public void off() { System.out.println("Đèn: Tắt."); }
}

class Fan {
    public void on() { System.out.println("Quạt: Quay."); }
    public void off() { System.out.println("Quạt: Dừng."); }
}

class AirConditioner {
    private int temperature = 25;
    public void setTemperature(int temp) {
        this.temperature = temp;
        System.out.println("Điều hòa: Nhiệt độ = " + temperature);
    }
    public int getTemperature() { return temperature; }
}

class LightOnCommand implements Command {
    private Light light;
    public LightOnCommand(Light light) { this.light = light; }
    public void execute() { light.on(); }
    public void undo() { light.off(); }
}

class LightOffCommand implements Command {
    private Light light;
    public LightOffCommand(Light light) { this.light = light; }
    public void execute() { light.off(); }
    public void undo() { light.on(); }
}

class ACSetTempCommand implements Command {
    private AirConditioner ac;
    private int newTemp;
    private int oldTemp;
    public ACSetTempCommand(AirConditioner ac, int temp) {
        this.ac = ac;
        this.newTemp = temp;
    }
    public void execute() {
        oldTemp = ac.getTemperature();
        ac.setTemperature(newTemp);
    }
    public void undo() {
        ac.setTemperature(oldTemp);
    }
}

class RemoteControl {
    private Command[] slots = new Command[5];
    private Stack<Command> history = new Stack<>();

    public void setCommand(int slot, Command command) {
        slots[slot] = command;
    }

    public void pressButton(int slot) {
        if (slots[slot] != null) {
            slots[slot].execute();
            history.push(slots[slot]);
        } else {
            System.out.println("Nút này chưa được gán lệnh!");
        }
    }

    public void pressUndo() {
        if (!history.isEmpty()) {
            Command lastCommand = history.pop();
            System.out.print("Undo: ");
            lastCommand.undo();
        } else {
            System.out.println("Không có lệnh nào để Undo!");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        RemoteControl remote = new RemoteControl();
        Light light = new Light();
        AirConditioner ac = new AirConditioner();

        boolean running = true;
        while (running) {
            System.out.println("\n--- REMOTE CONTROL ---");
            System.out.println("1. Gán nút 1: Bật đèn");
            System.out.println("2. Gán nút 2: Tắt đèn");
            System.out.println("3. Gán nút 3: Set AC 26°C");
            System.out.println("4. Nhấn nút (1-3)");
            System.out.println("5. Nhấn Undo");
            System.out.println("6. Thoát");
            System.out.print("Chọn: ");

            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    remote.setCommand(1, new LightOnCommand(light));
                    System.out.println("Đã gán LightOnCommand cho nút 1");
                    break;
                case 2:
                    remote.setCommand(2, new LightOffCommand(light));
                    System.out.println("Đã gán LightOffCommand cho nút 2");
                    break;
                case 3:
                    remote.setCommand(3, new ACSetTempCommand(ac, 26));
                    System.out.println("Đã gán ACSetTempCommand(26) cho nút 3");
                    break;
                case 4:
                    System.out.print("Nhấn nút số: ");
                    int btn = sc.nextInt();
                    remote.pressButton(btn);
                    break;
                case 5:
                    remote.pressUndo();
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Lựa chọn sai!");
            }
        }
        sc.close();
    }
}