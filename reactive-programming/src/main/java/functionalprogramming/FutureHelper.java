package functionalprogramming;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FutureHelper {

    public static Future<Integer> getFuture() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            return executor.submit(() -> {
                return 1;
            });
        } finally {
            executor.shutdown();
        }
    }

    public static Future<Integer> getFutureCompleteAfter1s() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            return executor.submit(() -> {
                Thread.sleep(1000);
                return 1;
            });
        } finally {
            executor.shutdown();
        }
    }
}
