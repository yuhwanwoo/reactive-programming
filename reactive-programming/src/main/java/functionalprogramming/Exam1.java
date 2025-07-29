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

동기 : callee의 결과에 관심이 있음? -> 응답을 받아야 함 (대기) callee 읽기 끝날 때까지 함수 리턴 안됨
비동기 : callee의 결과에 관심이 없음,  요청을 보내고 즉시 제어권을 받음

blocking : 호출한 스레드가 결과를 받을 때까지 CPU를 놀리고 기다림
nonblocking : 호출한 스레드가 기다리지 않고 즉시 리턴

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
