package Session09.smartsim.traffic;

public interface TrafficLightState {
    String getName();

    long getDurationMillis();

    TrafficLightState next();
}
