package Session09.smartsim.factory;

import Session09.smartsim.engine.Intersection;
import Session09.smartsim.model.PriorityVehicle;
import Session09.smartsim.model.StandardVehicle;
import Session09.smartsim.model.Vehicle;
import Session09.smartsim.model.VehicleType;
import Session09.smartsim.traffic.TrafficLight;

import java.util.concurrent.ThreadLocalRandom;

public class VehicleFactory {
    private final Intersection intersection;
    private final TrafficLight trafficLight;

    public VehicleFactory(Intersection intersection, TrafficLight trafficLight) {
        this.intersection = intersection;
        this.trafficLight = trafficLight;
    }

    public Vehicle createRandomVehicle() {
        VehicleType type = pickRandomType();
        int speed = pickSpeedMillis(type);
        if (type == VehicleType.AMBULANCE) {
            return new PriorityVehicle(type, speed, intersection, trafficLight);
        }
        return new StandardVehicle(type, speed, intersection, trafficLight);
    }

    private VehicleType pickRandomType() {
        VehicleType[] values = VehicleType.values();
        int idx = ThreadLocalRandom.current().nextInt(values.length);
        return values[idx];
    }

    private int pickSpeedMillis(VehicleType type) {
        switch (type) {
            case MOTORCYCLE:
                return ThreadLocalRandom.current().nextInt(400, 700);
            case CAR:
                return ThreadLocalRandom.current().nextInt(600, 900);
            case TRUCK:
                return ThreadLocalRandom.current().nextInt(800, 1200);
            case AMBULANCE:
                return ThreadLocalRandom.current().nextInt(300, 600);
            default:
                return 700;
        }
    }
}
