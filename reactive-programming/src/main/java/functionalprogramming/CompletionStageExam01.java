package functionalprogramming;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletionStage;

@Slf4j
public class CompletionStageExam01 {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        CompletionStage<Integer> stage = Helper.finishedStage();
        // thenAccept는 caller의 스레드에서 실행
        // [main] INFO functionalprogramming.Exam04 -- start main
        // [ForkJoinPool.commonPool-worker-1] INFO functionalprogramming.Helper -- supplyAsync
        // [main] INFO functionalprogramming.Exam04 -- 1 in thenAccept
        // [main] INFO functionalprogramming.Exam04 -- null in thenAccept2
        // [main] INFO functionalprogramming.Exam04 -- after thenAccept
        stage.thenAccept(i -> {
            log.info("{} in thenAccept", i);
        }).thenAccept(i -> {
            log.info("{} in thenAccept2", i);
        });
        log.info("after thenAccept");

        Thread.sleep(100);
    }
}
