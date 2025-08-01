package functionalprogramming;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletionStage;

@Slf4j
public class CompletionStageExam06 {
    public static void main(String[] args) throws InterruptedException {
        CompletionStage<Integer> stage = Helper.completionStage();
        stage.thenRunAsync(() -> {
            log.info("in thenRunAsync");
        }).thenRunAsync(() -> {
            log.info("in thenRunAsync2");
        }).thenAcceptAsync(value -> {
            log.info("{} in thenAcceptAsync", value);
        });
        Thread.sleep(100);
    }
}
