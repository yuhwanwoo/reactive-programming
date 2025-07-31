package functionalprogramming;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletionStage;

@Slf4j
public class CompletionStageExam02 {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        CompletionStage<Integer> stage = Helper.finishedStage();
        // thenAccept는 callee의 스레드에서 실행
        // [main] INFO functionalprogramming.Exam05 -- start main
        //[ForkJoinPool.commonPool-worker-1] INFO functionalprogramming.Helper -- supplyAsync
        //[main] INFO functionalprogramming.Exam05 -- after thenAccept
        //[ForkJoinPool.commonPool-worker-1] INFO functionalprogramming.Exam05 -- 1 in thenAcceptAsync
        //[ForkJoinPool.commonPool-worker-1] INFO functionalprogramming.Exam05 -- null in thenAcceptAsync2
        stage.thenAcceptAsync(i -> {
            log.info("{} in thenAcceptAsync", i);
        }).thenAcceptAsync(i -> {
            log.info("{} in thenAcceptAsync2", i);
        });
        log.info("after thenAcceptAsync");

        Thread.sleep(100);
    }
}
