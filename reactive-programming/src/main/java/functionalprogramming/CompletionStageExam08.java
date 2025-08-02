package functionalprogramming;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * supplyAsync, runAsync 차이
 */
@Slf4j
public class CompletionStageExam08 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return 1;
        });
        assert !future.isDone();

        Thread.sleep(1000);
        assert future.isDone();
        assert future.get() == 1;

        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        assert !future1.isDone();

        Thread.sleep(1000);
        assert future1.isDone();
        assert future1.get() == null;

    }
}
