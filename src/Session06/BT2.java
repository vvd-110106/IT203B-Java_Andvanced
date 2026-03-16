package Session06;

import java.util.Random;

public class BT2 {
    public static void main(String[] args) {
        String[] students = {"An", "Dũng", "Anh", "Nam"};
        String[] arrdress = {"Hà Nội", "Hưng Yên", "Bắc Ninh", "Nghệ An"};
        Random random = new Random();
        while (true) {
            int idx = random.nextInt(students.length);
            System.out.println("Tên ngẫu nhiên" + students[idx] + "Địa chỉ: " + arrdress[idx]);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
