package reactorpattern.result.client;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
   7. `JavaIOMultiClient.java`:
       * 리액터 서버에 연결하여 테스트하는 클라이언트 애플리케이션입니다.
       * java.net.Socket을 사용하여 블로킹 I/O 방식으로 서버에 연결합니다.
       * CompletableFuture와 ExecutorService를 활용하여 1000개의 동시 클라이언트 연결을 생성하고, 각 클라이언트는 서버에 메시지를 보내고 응답을 받습니다.
       * 모든 클라이언트 요청이 완료되는 데 걸리는 시간을 측정합니다.
 */
@Slf4j
public class JavaIOMultiClient {
    private static ExecutorService executorService = Executors.newFixedThreadPool(50);

    @SneakyThrows
    public static void main(String[] args) {
        log.info("start main");

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            var future = CompletableFuture.runAsync(() -> {
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress("localhost", 8080));

                    OutputStream out = socket.getOutputStream();
                    String requestBody = "This is client";
                    out.write(requestBody.getBytes());
                    out.flush();

                    InputStream in = socket.getInputStream();
                    byte[] responseBytes = new byte[1024];
                    in.read(responseBytes);
                    log.info("result: {}", new String(responseBytes).trim());
                } catch (Exception e) {}
            }, executorService);

            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executorService.shutdown();
        log.info("end main");
        long end = System.currentTimeMillis();
        log.info("duration: {}", (end - start) / 1000.0);
    }
}
