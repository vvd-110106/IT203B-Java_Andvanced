package Session09.smartsim.traffic;

import Session09.smartsim.util.LogUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TrafficLight implements Runnable {
    private final List<TrafficLightObserver> observers = new CopyOnWriteArrayList<>();
    private volatile TrafficLightState currentState;

    private long greenDurationMillis = 5000;
    private long yellowDurationMillis = 2000;
    private long redDurationMillis = 5000;

    private volatile boolean active = true;

    public TrafficLight() {
        this.currentState = new GreenState(this);
    }

    public void setDurations(long greenMs, long yellowMs, long redMs) {
        this.greenDurationMillis = greenMs;
        this.yellowDurationMillis = yellowMs;
        this.redDurationMillis = redMs;
    }

    public long getGreenDurationMillis() {
        return greenDurationMillis;
    }

    public long getYellowDurationMillis() {
        return yellowDurationMillis;
    }

    public long getRedDurationMillis() {
        return redDurationMillis;
    }

    public TrafficLightState getCurrentState() {
        return currentState;
    }

    public void registerObserver(TrafficLightObserver observer) {
        observers.add(observer);
    }

    public void unregisterObserver(TrafficLightObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (TrafficLightObserver observer : observers) {
            observer.onLightChanged(currentState);
        }
    }

    public void stop() {
        active = false;
    }

    @Override
    public void run() {
        while (active) {
            LogUtil.log("Đèn chuyển sang %s", currentState.getName());
            notifyObservers();
            try {
                Thread.sleep(currentState.getDurationMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            currentState = currentState.next();
        }
        LogUtil.log("Traffic light đã dừng hoạt động");
    }
}
