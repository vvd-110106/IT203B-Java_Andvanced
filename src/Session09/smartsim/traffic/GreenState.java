package Session09.smartsim.traffic;

public class GreenState implements TrafficLightState {
    private final TrafficLight trafficLight;

    public GreenState(TrafficLight trafficLight) {
        this.trafficLight = trafficLight;
    }

    @Override
    public String getName() {
        return "GREEN";
    }

    @Override
    public long getDurationMillis() {
        return trafficLight.getGreenDurationMillis();
    }

    @Override
    public TrafficLightState next() {
        return new YellowState(trafficLight);
    }
}
