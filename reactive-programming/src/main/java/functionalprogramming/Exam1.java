package functionalprogramming;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
callee의 결과에 관심은 있지만 계속 본인의 일을 할 수 있다.
-> Nonblocking : callee 호출 후 callee가 완료되지 않더라도 본인의 일을 할 수 있다.(제어권 caller가 가지고 있음)

동기지만 non-blocking

 */
@Slf4j
public class Exam1 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        log.info("start");

        var count = 1;
        Future<Integer> result = getResult();
        while (!result.isDone()) {
            log.info("waiting for result: {}", count++);
            Thread.sleep(100);
        }

        var nextValue = result.get() +1;
        assert nextValue == 1;

        log.info("finish");
    }


    private static Future<Integer> getResult() throws InterruptedException {

        log.info("in");

        CompletableFuture<Integer> future = new CompletableFuture<>();

        // 별도 스레드에서 10초 후 값 제공
        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                Thread.sleep(10000);
                future.complete(1);
            } catch (InterruptedException e) {
                future.completeExceptionally(e);
            }
        });

        return future;

    }
}
