package functionalprogramming;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletionStage;

@Slf4j
public class CompletionStageExam03 {
    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");
        CompletionStage<Integer> stage = Helper.runningStage();

        // future를 실행한 callee에서 콜백 실행
        stage.thenAccept(i -> {
            log.info("{} in thenAccept", i);
        }).thenAccept(i -> {
            log.info("{} in thenAccept2", i);
        });
        log.info("after thenAccept");

        Thread.sleep(2000);
    }
}
