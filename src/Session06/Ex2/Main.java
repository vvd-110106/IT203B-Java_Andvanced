package Session06.Ex2;

import java.util.LinkedList;
import java.util.Queue;

class TicketPool {
    private String roomName;
    private Queue<String> tickets = new LinkedList<>();
    private int ticketCount = 0;

    public TicketPool(String roomName, int initialQty) {
        this.roomName = roomName;
        addTickets(initialQty);
    }

    public String getRoomName() {
        return roomName;
    }

    public synchronized String sellTicket(String counterName) throws InterruptedException {
        while (tickets.isEmpty()) {
            System.out.println(counterName + ": Hết vé phòng " + roomName + ", đang chờ...");
            wait();
        }
        return tickets.poll();
    }

    public synchronized void addTickets(int quantity) {
        for (int i = 0; i < quantity; i++) {
            ticketCount++;
            tickets.add(roomName + "-" + String.format("%03d", ticketCount));
        }
        System.out.println("Nhà cung cấp: Đã thêm " + quantity + " vé vào phòng " + roomName);
        notifyAll();
    }
}

class BookingCounter extends Thread {
    private String counterName;
    private TicketPool pool;

    public BookingCounter(String name, TicketPool pool) {
        this.counterName = name;
        this.pool = pool;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 5; i++) {
                String ticket = pool.sellTicket(counterName);
                System.out.println(counterName + " bán vé " + ticket);
                Thread.sleep(400);
            }
        } catch (InterruptedException e) {
            return;
        }
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        TicketPool roomA = new TicketPool("A", 2);
        TicketPool roomB = new TicketPool("B", 5);

        System.out.println("BẮT ĐẦU HỆ THỐNG (WAIT/NOTIFY)");

        BookingCounter q1 = new BookingCounter("Quầy 1", roomA);
        BookingCounter q2 = new BookingCounter("Quầy 2", roomB);

        q1.start();
        q2.start();

        Thread.sleep(3000);
        roomA.addTickets(3);
    }
}