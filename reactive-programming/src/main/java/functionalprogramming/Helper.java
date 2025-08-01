package functionalprogramming;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Slf4j
public class Helper {

    /**
     * 1을 반환하는 완료된 CompletableFuture 반환
     * @return
     */
    @SneakyThrows
    public static CompletionStage<Integer> finishedStage() {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            log.info("supplyAsync");
            return 1;
        });
        Thread.sleep(100);
        return future;
    }

    /**
     * 1초를 sleep한 후 1을 반환하는 completableFuture
     * @return
     */
    public static CompletionStage<Integer>  runningStage() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
                log.info("I'm running!");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 1;
        });
    }

    public static CompletionStage<Integer> completionStage() {
        return CompletableFuture.supplyAsync(() -> {
            log.info("return in future");
            return 1;
        });
    }

    public static CompletionStage<Integer> completionStageAfter1s() {
        return CompletableFuture.supplyAsync(() -> {
            log.info("getCompletionStage");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 1;
        });
    }

    public static CompletionStage<Integer> addOne(int value) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return value + 1;
        });
    }

    public static CompletionStage<String> addResultPrefix(int value) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "result: " + value;
        });
    }

    public static CompletableFuture<Integer> waitAndReturn(int millis, int value) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                log.info("waitAndReturn: {}ms", millis);
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return value;
        });
    }
}
