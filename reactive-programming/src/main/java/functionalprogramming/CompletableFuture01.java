package functionalprogramming;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFuture01 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = new CompletableFuture<>();
        assert !future.isDone();

        boolean triggered = future.complete(1);
        assert future.isDone();
        assert triggered;
        assert future.get() == 1;

        triggered = future.complete(2); // 완료되지 않았을 떄만 주어진 값으로 채움
        assert future.isDone();
        assert !triggered;
        assert future.get() == 1;


        CompletableFuture<Integer> futureWithException = CompletableFuture.supplyAsync(() -> {
            return 1 / 0;
        });
        Thread.sleep(100);
        assert futureWithException.isDone();
        assert futureWithException.isCompletedExceptionally();
    }
}
