package functionalprogramming;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class CompletableFuture02AnyOf {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        CompletableFuture<Integer> firstFuture = Helper.waitAndReturn(100, 1);
        CompletableFuture<Integer> secondFuture = Helper.waitAndReturn(500, 2);
        CompletableFuture<Integer> thirdFuture = Helper.waitAndReturn(1000, 3);

        CompletableFuture.anyOf(firstFuture, secondFuture, thirdFuture)
                .thenAcceptAsync(v -> {
                    log.info("after allFo");
                    try {
                        log.info("after anyOf");
                        log.info("firstValue: {}", v);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).join();
        long endTime = System.currentTimeMillis();
        log.info("elapsed: {}ms", endTime - startTime);
    }
}
