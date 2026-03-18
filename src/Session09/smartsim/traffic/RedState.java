package Session09.smartsim.traffic;

public class RedState implements TrafficLightState {
    private final TrafficLight trafficLight;

    public RedState(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
    }

    @Override
    public String getName() {
        return "RED";
    }

    @Override
    public long getDurationMillis() {
        return trafficLight.getRedDurationMillis();
    }

    @Override
    public TrafficLightState next() {
        return new GreenState(trafficLight);
    }
}
