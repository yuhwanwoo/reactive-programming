package functionalprogramming;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Exam03 {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        Future<Integer> future = FutureHelper.getFutureCompleteAfter1s();
        Integer result = future.get(1500, TimeUnit.MILLISECONDS);
        assert result.equals(1);

        Future<Integer> futureToTimeout = FutureHelper.getFutureCompleteAfter1s();
        Exception exception = null;
        try {
            future.get(500, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            exception = e;
        }

        assert exception != null;


    }
}
