package Session06.Ex1;

class TicketPool {
    private String roomName;
    private int tickets;

    public TicketPool(String roomName, int tickets) {
        this.roomName = roomName;
        this.tickets = tickets;
    }

    public String getRoomName() {
        return roomName;
    }

    public synchronized boolean hasTicket() {
        return tickets > 0;
    }

    public synchronized void sellTicket() {
        if (tickets > 0) tickets--;
    }
}

class BookingCounter extends Thread {
    private String counterName;
    private TicketPool firstLock;
    private TicketPool secondLock;

    public BookingCounter(String name, TicketPool first, TicketPool second) {
        this.counterName = name;
        this.firstLock = first;
        this.secondLock = second;
    }

    public void sellCombo() {
        synchronized (firstLock) {
            System.out.println(counterName + ": Đã lấy vé " + firstLock.getRoomName());

            try { Thread.sleep(100); } catch (InterruptedException e) {}

            System.out.println(counterName + ": Đang chờ vé " + secondLock.getRoomName() + "...");

            synchronized (secondLock) {
                if (firstLock.hasTicket() && secondLock.hasTicket()) {
                    firstLock.sellTicket();
                    secondLock.sellTicket();
                    System.out.println(counterName + " bán combo thành công: " + firstLock.getRoomName() + " & " + secondLock.getRoomName());
                } else {
                    System.out.println(counterName + ": Hết vé, bán combo thất bại.");
                }
            }
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            sellCombo();
            try { Thread.sleep(50); } catch (InterruptedException e) {}
        }
    }
}

public class Main {
    public static void main(String[] args) {
        TicketPool roomA = new TicketPool("A", 10);
        TicketPool roomB = new TicketPool("B", 10);

//         ĐẶT GÂY DEADLOCK

//        BookingCounter q1 = new BookingCounter("Quầy 1", roomA, roomB);
//        BookingCounter q2 = new BookingCounter("Quầy 2", roomB, roomA);


//       PHÒNG CHỐNG DEADLOCK
        BookingCounter q1 = new BookingCounter("Quầy 1", roomA, roomB);
        BookingCounter q2 = new BookingCounter("Quầy 2", roomA, roomB);

        q1.start();
        q2.start();
    }
}