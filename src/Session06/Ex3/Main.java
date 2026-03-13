package Session06.Ex3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class TicketPool {
    private String roomName;
    private List<String> tickets = Collections.synchronizedList(new ArrayList<>());
    private int ticketCount = 0;

    public TicketPool(String roomName, int initialQty) {
        this.roomName = roomName;
        addTickets(initialQty);
    }

    public synchronized void addTickets(int count) {
        for (int i = 0; i < count; i++) {
            ticketCount++;
            tickets.add(roomName + "-" + String.format("%03d", ticketCount));
        }
        System.out.println("Nhà cung cấp: Đã thêm " + count + " vé vào " + roomName);
    }

    public synchronized String sellTicket() {
        if (!tickets.isEmpty()) {
            return tickets.remove(0);
        }
        return null;
    }

    public int getRemainingCount() {
        return tickets.size();
    }
}

class TicketSupplier implements Runnable {
    private TicketPool poolA;
    private TicketPool poolB;
    private int supplyCount;
    private int interval;
    private int rounds;

    public TicketSupplier(TicketPool poolA, TicketPool poolB, int count, int ms, int r) {
        this.poolA = poolA;
        this.poolB = poolB;
        this.supplyCount = count;
        this.interval = ms;
        this.rounds = r;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < rounds; i++) {
                Thread.sleep(interval);
                poolA.addTickets(supplyCount);
                poolB.addTickets(supplyCount);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class BookingCounter extends Thread {
    private String counterName;
    private TicketPool pool;
    private int soldCount = 0;

    public BookingCounter(String name, TicketPool pool) {
        this.counterName = name;
        this.pool = pool;
    }

    @Override
    public void run() {
        for (int i = 0; i < 15; i++) {
            String ticket = pool.sellTicket();
            if (ticket != null) {
                soldCount++;
                System.out.println(counterName + " đã bán vé " + ticket);
            }
            try { Thread.sleep(500); } catch (InterruptedException e) {}
        }
    }

    public int getSoldCount() {
        return soldCount;
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        TicketPool poolA = new TicketPool("A", 5);
        TicketPool poolB = new TicketPool("B", 5);

        BookingCounter q1 = new BookingCounter("Quầy 1", poolA);
        BookingCounter q2 = new BookingCounter("Quầy 2", poolB);

        TicketSupplier supplier = new TicketSupplier(poolA, poolB, 3, 3000, 3);
        Thread supplierThread = new Thread(supplier);

        q1.start();
        q2.start();
        supplierThread.start();

        q1.join();
        q2.join();
        supplierThread.join();

        System.out.println("KẾT THÚC CHƯƠNG TRÌNH");
        System.out.println("Quầy 1 bán được: " + q1.getSoldCount() + " vé");
        System.out.println("Quầy 2 bán được: " + q2.getSoldCount() + " vé");
        System.out.println("Vé còn lại A: " + poolA.getRemainingCount());
        System.out.println("Vé còn lại B: " + poolB.getRemainingCount());
    }
}