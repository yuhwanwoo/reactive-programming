package functionalprogramming;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletionStage;

@Slf4j
public class CompletionStageExam05 {
    public static void main(String[] args) throws InterruptedException {
        CompletionStage<Integer> stage = Helper.completionStage();
        stage.thenComposeAsync(value -> {
                    CompletionStage<Integer> next = Helper.addOne(value);
                    log.info("in thenComposeAsync: {}", next);
                    return next;
                })
                .thenComposeAsync(value -> {
                    CompletionStage<String> next = Helper.addResultPrefix(value);
                    log.info("in thenComposeAsync2: {}", next);
                    return next;
                })
                .thenAcceptAsync(value -> {
                    log.info("in thenAcceptAsync: {}", value);
                });
        Thread.sleep(1000);
    }
}
