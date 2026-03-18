package Session09.smartsim.engine;

import Session09.smartsim.factory.VehicleFactory;
import Session09.smartsim.model.Vehicle;
import Session09.smartsim.traffic.TrafficLight;
import Session09.smartsim.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Simulator coordinates traffic light and vehicle threads.
 */
public class Simulator {
    private final TrafficLight trafficLight;
    private final Intersection intersection;
    private final VehicleFactory factory;

    public Simulator() {
        this.trafficLight = new TrafficLight();
        this.intersection = new Intersection(trafficLight, 15);
        this.factory = new VehicleFactory(intersection, trafficLight);
    }

    public void start(int runSeconds) {
        LogUtil.log("Bắt đầu mô phỏng trong %d giây", runSeconds);

        Thread lightThread = new Thread(trafficLight, "TrafficLightThread");
        lightThread.setDaemon(true);
        lightThread.start();

        ExecutorService vehiclePool = Executors.newCachedThreadPool();
        List<Vehicle> launched = new ArrayList<>();

        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < runSeconds * 1000L) {
            Vehicle v = factory.createRandomVehicle();
            launched.add(v);
            vehiclePool.submit(v);

            // Random spawn interval between vehicles
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        // Graceful shutdown
        vehiclePool.shutdown();
        try {
            if (!vehiclePool.awaitTermination(5, TimeUnit.SECONDS)) {
                vehiclePool.shutdownNow();
            }
        } catch (InterruptedException e) {
            vehiclePool.shutdownNow();
            Thread.currentThread().interrupt();
        }

        trafficLight.stop();
        intersection.shutdown();

        LogUtil.log("Mô phỏng kết thúc. Xe đi qua: %d, Số lần kẹt: %d", intersection.getPassedCount(),
                intersection.getJamCount());
    }
}
