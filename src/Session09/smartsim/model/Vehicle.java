package Session09.smartsim.model;

import Session09.smartsim.traffic.TrafficLightObserver;
import Session09.smartsim.traffic.TrafficLightState;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Vehicle implements Runnable, TrafficLightObserver {
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    protected final int id;
    protected final VehicleType type;
    protected final int speedMillis;
    protected final boolean priority;
    protected final String name;
    protected volatile boolean active = true;

    protected Vehicle(VehicleType type, int speedMillis, boolean priority) {
        this.id = NEXT_ID.getAndIncrement();
        this.type = type;
        this.speedMillis = speedMillis;
        this.priority = priority;
        this.name = String.format("%s#%02d", type.name(), id);
    }

    public int getId() {
        return id;
    }

    public VehicleType getType() {
        return type;
    }

    public boolean isPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }

    public int getSpeedMillis() {
        return speedMillis;
    }

    public void stop() {
        active = false;
    }

    protected abstract void driveToIntersection() throws InterruptedException;

    protected abstract void crossIntersection() throws InterruptedException;

    @Override
    public void run() {
        try {
            driveToIntersection();
            // The concrete vehicle implementation should use the Intersection and
            // TrafficLight to decide when to cross.
            crossIntersection();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private volatile boolean greenLight = false;

    public boolean isGreenLight() {
        return greenLight;
    }

    @Override
    public void onLightChanged(TrafficLightState newState) {
        greenLight = "GREEN".equals(newState.getName());
        synchronized (this) {
            notifyAll();
        }
    }

    @Override
    public String toString() {
        return name + "(" + (priority ? "Priority" : "Standard") + ")";
    }
}
