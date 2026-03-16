package Session06;

import java.util.Random;

public class BT1 {
    public static  void main(String[]  args){
        String[] students = {"An", "Dũng","Anh", "Nam"};
        Random random = new Random();
        while(true){
            int index = random.nextInt(students.length);
            System.out.println("Tên ngẫu nhiên" + students[index]);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
