package Session09.smartsim.traffic;

public interface TrafficLightObserver {
    void onLightChanged(TrafficLightState newState);
}
