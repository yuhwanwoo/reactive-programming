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
}
