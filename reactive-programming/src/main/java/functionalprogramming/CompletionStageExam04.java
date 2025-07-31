package functionalprogramming;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletionStage;

@Slf4j
public class CompletionStageExam04 {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        CompletionStage<Integer> stage1 = Helper.runningStage();

        //thread pool에서 실행
        stage1.thenAcceptAsync(i -> {
            log.info("{} in thenAcceptAsync", i);
        }).thenAcceptAsync(i -> {
            log.info("{} in thenAcceptAsync2", i);
        });
        log.info("after thenAcceptAsync");

        Thread.sleep(2000);
    }
}
