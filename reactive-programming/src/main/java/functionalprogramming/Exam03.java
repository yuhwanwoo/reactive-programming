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

        Future<Integer> future1 = FutureHelper.getFuture();
        boolean successToCancel = future1.cancel(true);
        assert future1.isCancelled();
        assert future1.isDone();
        assert successToCancel;

        successToCancel = future1.cancel(true);
        assert future1.isCancelled();
        assert future1.isDone();
        assert !successToCancel;

        Future<Integer> futureWithException = FutureHelper.getFutureWithException();

        Exception exceptionToThrow = null;

        try {
            futureWithException.get();
        } catch (ExecutionException e) {
            exceptionToThrow = e;
        }

        /*
         * 예외 발생인지 진짜 완료인지 구분 못함
         */
        assert futureWithException.isDone();
        assert exceptionToThrow != null;
    }
}
