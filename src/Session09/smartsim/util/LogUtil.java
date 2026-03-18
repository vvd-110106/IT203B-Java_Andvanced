package Session09.smartsim.util;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public final class LogUtil {
    private static final Instant START = Instant.now();

    private LogUtil() {
    }

    public static void log(String format, Object... args) {
        Instant now = Instant.now();
        long elapsedSeconds = Duration.between(START, now).getSeconds();
        String timestamp = String.format("[Time: %ds]", elapsedSeconds);
        System.out.println(timestamp + " " + String.format(format, args));
    }
}
