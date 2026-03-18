package Session09.smartsim.model;

import Session09.smartsim.engine.Intersection;
import Session09.smartsim.traffic.TrafficLight;
import Session09.smartsim.util.LogUtil;

import java.util.concurrent.TimeUnit;

public class StandardVehicle extends Vehicle {
    private final Intersection intersection;
    private final TrafficLight trafficLight;

    public StandardVehicle(VehicleType type, int speedMillis, Intersection intersection, TrafficLight trafficLight) {
        super(type, speedMillis, false);
        this.intersection = intersection;
        this.trafficLight = trafficLight;
    }

    @Override
    protected void driveToIntersection() throws InterruptedException {
        LogUtil.log("%s đang di chuyển tới ngã tư", this);
        // Simulate travel time before reaching the intersection
        TimeUnit.MILLISECONDS.sleep(speedMillis);
        LogUtil.log("%s đã tới ngã tư và chờ đèn", this);
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
        // Not used in this basic model; intersection coordinates behavior.
    }
}
