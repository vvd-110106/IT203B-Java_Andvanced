package Session09.smartsim.model;

import Session09.smartsim.engine.Intersection;
import Session09.smartsim.traffic.TrafficLight;
import Session09.smartsim.util.LogUtil;

import java.util.concurrent.TimeUnit;

public class PriorityVehicle extends Vehicle {
    private final Intersection intersection;
    private final TrafficLight trafficLight;

    public PriorityVehicle(VehicleType type, int speedMillis, Intersection intersection, TrafficLight trafficLight) {
        super(type, speedMillis, true);
        this.intersection = intersection;
        this.trafficLight = trafficLight;
    }

    @Override
    protected void driveToIntersection() throws InterruptedException {
        LogUtil.log("%s (ưu tiên) đang di chuyển tới ngã tư", this);
        TimeUnit.MILLISECONDS.sleep(speedMillis);
        LogUtil.log("%s (ưu tiên) đã tới ngã tư", this);
        trafficLight.registerObserver(this);
        intersection.enterQueue(this);
    }

    @Override
    protected void crossIntersection() throws InterruptedException {
        intersection.awaitGreenAndCross(this);
        trafficLight.unregisterObserver(this);
    }

    @Override
    public void onLightChanged(Session09.smartsim.traffic.TrafficLightState newState) {
        // Priority vehicles could log or take special action.
        super.onLightChanged(newState);
    }
}
