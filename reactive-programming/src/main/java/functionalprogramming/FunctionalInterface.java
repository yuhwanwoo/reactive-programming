package functionalprogramming;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
/*
동기 : main은 getResult에 결과에 관심이 있다.
비동기 main은 gerResult 결과에 관심이 없다.
 */
@Slf4j
public class FunctionalInterface {

    public static void main(String[] args) {
        Consumer<Integer> consumer = getConsumer();
        consumer.accept(1);

        Consumer<Integer> consumerAsLambda = getConsumerAsLambda();
        consumerAsLambda.accept(1);

        handleConsumer(consumer);
    }

    public static Consumer<Integer> getConsumer() {
        Consumer<Integer> returnValue = new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                log.info("value in interface: {}", integer);
            }
        };
        return returnValue;
    }

    public static Consumer<Integer> getConsumerAsLambda() {
        return integer -> log.info("value in lambda: {}", integer);
    }

    public static void handleConsumer(Consumer<Integer> consumer) {
        log.info("handleConsumer");
        consumer.accept(1);
    }
}
