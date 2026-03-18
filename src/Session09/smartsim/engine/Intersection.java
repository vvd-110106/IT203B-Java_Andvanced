package Session09.smartsim.engine;

import Session09.smartsim.exception.CollisionException;
import Session09.smartsim.exception.TrafficJamException;
import Session09.smartsim.model.Vehicle;
import Session09.smartsim.traffic.TrafficLight;
import Session09.smartsim.traffic.TrafficLightState;
import Session09.smartsim.util.LogUtil;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Intersection is a critical section representing the crossroad.
 * It allows a limited number of vehicles to cross at the same time.
 */
public class Intersection implements Session09.smartsim.traffic.TrafficLightObserver {
    private final ReentrantLock lock = new ReentrantLock(true);
    private final Condition greenCondition = lock.newCondition();

    private final BlockingQueue<Vehicle> waitingQueue = new LinkedBlockingQueue<>();
    private final int maxQueueSize;
    private final TrafficLight trafficLight;

    private volatile int passedCount = 0;
    private volatile int jamCount = 0;

    public Intersection(TrafficLight trafficLight, int maxQueueSize) {
        this.trafficLight = trafficLight;
        this.maxQueueSize = maxQueueSize;
        this.trafficLight.registerObserver(this);
    }

    public void enterQueue(Vehicle vehicle) throws InterruptedException {
        if (waitingQueue.size() >= maxQueueSize) {
            jamCount++;
            throw new TrafficJamException("Hàng chờ quá dài (" + waitingQueue.size() + ")");
        }
        waitingQueue.put(vehicle);
    }

    /**
     * Called by a vehicle when it is ready to cross.
     * Vehicles will only allow crossing when the light is green or they are
     * priority vehicles.
     */
    public void awaitGreenAndCross(Vehicle vehicle) throws InterruptedException {
        // Vehicles wait until they are allowed to cross.
        // Priority vehicles may overtake standard vehicles when the light is not green.
        while (true) {
            Vehicle head = waitingQueue.peek();
            if (head == null) {
                TimeUnit.MILLISECONDS.sleep(50);
                continue;
            }

            if (head.equals(vehicle)) {
                break;
            }

            if (vehicle.isPriority() && canOvertake(priorityAhead(), vehicle)) {
                break;
            }

            TimeUnit.MILLISECONDS.sleep(50);
        }

        lock.lock();
        try {
            while (!canCross(vehicle)) {
                greenCondition.await(100, TimeUnit.MILLISECONDS);
            }

            if (!waitingQueue.remove(vehicle)) {
                throw new CollisionException("Xe " + vehicle + " không nằm trong hàng chờ khi chuẩn bị vào giao lộ");
            }

            LogUtil.log("%s đang đi qua ngã tư", vehicle);
            TimeUnit.MILLISECONDS.sleep(vehicle.getSpeedMillis());
            passedCount++;
            LogUtil.log("%s đã qua giao lộ", vehicle);
        } finally {
            lock.unlock();
        }
    }

    private boolean priorityAhead() {
        for (Vehicle v : waitingQueue) {
            if (v.isPriority()) {
                return true;
            }
        }
        return false;
    }

    private boolean canOvertake(boolean priorityAhead, Vehicle vehicle) {
        // A priority vehicle can overtake standard vehicles if it is the first priority
        // in queue
        if (!vehicle.isPriority()) {
            return false;
        }
        if (!priorityAhead) {
            return true;
        }
        // If there is a priority ahead of this vehicle, it should wait for that vehicle
        // first.
        for (Vehicle v : waitingQueue) {
            if (v.equals(vehicle)) {
                return true;
            }
            if (v.isPriority()) {
                return false;
            }
        }
        return true;
    }

    private boolean canCross(Vehicle vehicle) {
        boolean isGreen = vehicle.isGreenLight();
        return isGreen || vehicle.isPriority();
    }

    @Override
    public void onLightChanged(TrafficLightState newState) {
        if ("GREEN".equals(newState.getName())) {
            lock.lock();
            try {
                greenCondition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    public int getPassedCount() {
        return passedCount;
    }

    public int getJamCount() {
        return jamCount;
    }

    public int getQueueSize() {
        return waitingQueue.size();
    }

    public void shutdown() {
        trafficLight.unregisterObserver(this);
    }
}
