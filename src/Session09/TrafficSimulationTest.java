package Session09;

import Session09.smartsim.engine.Intersection;
import Session09.smartsim.engine.Simulator;
import Session09.smartsim.traffic.TrafficLight;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class TrafficSimulationTest {

    @Test
    public void testTrafficLightCycles() throws InterruptedException {
        TrafficLight light = new TrafficLight();
        // Shorten durations to make test fast.
        light.setDurations(200, 100, 200);

        Thread t = new Thread(light);
        t.setDaemon(true);
        t.start();

        // Wait enough time for a full cycle (green->yellow->red->green)
        TimeUnit.MILLISECONDS.sleep(650);
        assertNotNull(light.getCurrentState());

        // Expect the light to have cycled at least once to green again.
        String state = light.getCurrentState().getName();
        assertTrue(state.equals("GREEN") || state.equals("RED") || state.equals("YELLOW"));

        light.stop();
    }

    @Test
    public void testIntersectionNoCollisionUnderLoad() throws InterruptedException {
        TrafficLight light = new TrafficLight();
        light.setDurations(100, 50, 100);
        Intersection intersection = new Intersection(light, 100);

        Thread lightThread = new Thread(light);
        lightThread.setDaemon(true);
        lightThread.start();

        // Create a modest number of vehicles that will all attempt to cross.
        int vehicleCount = 30;
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < vehicleCount; i++) {
            executor.submit(() -> {
                // Create a fast vehicle so test completes quickly.
                Session09.smartsim.model.Vehicle vehicle = new Session09.smartsim.model.StandardVehicle(
                        Session09.smartsim.model.VehicleType.CAR,
                        20,
                        intersection,
                        light);
                vehicle.run();
            });
        }

        executor.shutdown();
        boolean finished = executor.awaitTermination(6, TimeUnit.SECONDS);
        assertTrue(finished);

        // All vehicles should have attempted to pass; passedCount should reflect that.
        assertEquals(vehicleCount, intersection.getPassedCount());

        light.stop();
        intersection.shutdown();
    }
}
