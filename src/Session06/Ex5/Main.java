package Session06.Ex5;

import java.util.*;

class Ticket {
    String id;
    boolean isHeld = false;
    boolean isSold = false;
    long expiryTime = 0;
    boolean isVIP;

    public Ticket(String id, boolean isVIP) {
        this.id = id;
        this.isVIP = isVIP;
    }
}

class TicketPool {
    private List<Ticket> tickets = new ArrayList<>();
    String roomName;

    public TicketPool(String roomName, int qty) {
        this.roomName = roomName;
        for (int i = 1; i <= qty; i++) tickets.add(new Ticket(roomName + "-" + i, i <= 2));
    }

    public synchronized Ticket holdTicket(boolean wantVIP) {
        for (Ticket t : tickets) {
            if (!t.isSold && !t.isHeld && (t.isVIP == wantVIP)) {
                t.isHeld = true;
                t.expiryTime = System.currentTimeMillis() + 5000;
                return t;
            }
        }
        return null;
    }

    public synchronized void sellTicket(Ticket t) {
        t.isHeld = false;
        t.isSold = true;
    }

    public synchronized void releaseExpired() {
        long now = System.currentTimeMillis();
        for (Ticket t : tickets) {
            if (t.isHeld && now > t.expiryTime) {
                t.isHeld = false;
                System.out.println("TimeoutManager: Vé " + t.id + " hết hạn, trả lại kho.");
            }
        }
    }
}

class TimeoutManager implements Runnable {
    private List<TicketPool> pools;
    public TimeoutManager(List<TicketPool> pools) { this.pools = pools; }
    public void run() {
        while (true) {
            for (TicketPool p : pools) p.releaseExpired();
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }
    }
}

class BookingCounter implements Runnable {
    private String name;
    private List<TicketPool> pools;
    public BookingCounter(String name, List<TicketPool> pools) { this.name = name; this.pools = pools; }

    public void run() {
        TicketPool p = pools.get(new Random().nextInt(pools.size()));
        Ticket t = p.holdTicket(false);
        if (t != null) {
            System.out.println(name + ": Đã giữ vé " + t.id + ". Vui lòng thanh toán trong 5s");
            try { Thread.sleep(3000); } catch (InterruptedException e) {}
            p.sellTicket(t);
            System.out.println(name + ": Thanh toán thành công vé " + t.id);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        List<TicketPool> pools = Arrays.asList(new TicketPool("A", 5),
                new TicketPool("B", 5),
                new TicketPool("C", 5));
        new Thread(new TimeoutManager(pools)).start();
        for (int i = 1; i <= 5; i++) new Thread(new BookingCounter("Quầy " + i, pools)).start();
    }
}