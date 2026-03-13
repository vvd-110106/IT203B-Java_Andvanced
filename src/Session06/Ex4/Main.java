package Session06.Ex4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Ticket {
    private String ticketId;
    private String roomName;
    private boolean isSold;

    public Ticket(String ticketId, String roomName) {
        this.ticketId = ticketId;
        this.roomName = roomName;
        this.isSold = false;
    }

    public boolean isSold() { return isSold; }
    public void setSold(boolean sold) { isSold = sold; }
    public String getInfo() { return roomName + "-" + ticketId; }
}

class TicketPool {
    private String roomName;
    private List<Ticket> tickets = new ArrayList<>();

    public TicketPool(String roomName, int quantity) {
        this.roomName = roomName;
        for (int i = 1; i <= quantity; i++) {
            tickets.add(new Ticket(String.format("%03d", i), roomName));
        }
    }

    public synchronized Ticket sellTicket() {
        for (Ticket t : tickets) {
            if (!t.isSold()) {
                t.setSold(true);
                return t;
            }
        }
        return null;
    }

    public boolean hasTickets() {
        for (Ticket t : tickets) {
            if (!t.isSold()) return true;
        }
        return false;
    }
}

class BookingCounter implements Runnable {
    private String counterName;
    private TicketPool roomA;
    private TicketPool roomB;
    private int soldCount = 0;
    private Random random = new Random();

    public BookingCounter(String name, TicketPool roomA, TicketPool roomB) {
        this.counterName = name;
        this.roomA = roomA;
        this.roomB = roomB;
    }

    @Override
    public void run() {
        while (roomA.hasTickets() || roomB.hasTickets()) {
            Ticket ticket = null;
            if (random.nextBoolean()) {
                ticket = roomA.sellTicket();
                if (ticket == null) ticket = roomB.sellTicket();
            } else {
                ticket = roomB.sellTicket();
                if (ticket == null) ticket = roomA.sellTicket();
            }

            if (ticket != null) {
                soldCount++;
                System.out.println(counterName + " đã bán vé " + ticket.getInfo());
            }
            try { Thread.sleep(50); } catch (InterruptedException e) {}
        }
    }

    public int getSoldCount() { return soldCount; }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        TicketPool roomA = new TicketPool("A", 10);
        TicketPool roomB = new TicketPool("B", 10);

        BookingCounter q1 = new BookingCounter("Quầy 1", roomA, roomB);
        BookingCounter q2 = new BookingCounter("Quầy 2", roomA, roomB);

        Thread t1 = new Thread(q1);
        Thread t2 = new Thread(q2);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Kết thúc chương trình");
        System.out.println("Quầy 1 bán được: " + q1.getSoldCount() + " vé");
        System.out.println("Quầy 2 bán được: " + q2.getSoldCount() + " vé");
        System.out.println("Vé còn lại phòng A: " + (roomA.hasTickets() ? "Còn" : "0"));
        System.out.println("Vé còn lại phòng B: " + (roomB.hasTickets() ? "Còn" : "0"));
    }
}