package reactorpattern.result;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
   3. `HttpEventHandler.java`:
       * EventHandler를 구현합니다.
       * HTTP 요청을 읽고 HTTP 응답을 전송하는 역할을 합니다.
       * 생성자에서 클라이언트 소켓을 논블로킹 모드로 설정하고, Selector에 OP_READ 이벤트로 등록합니다.
       * handle() 메서드에서 HTTP 요청을 처리하고 (handleRequest()), MsgCodec를 사용하여 디코딩한 후, 응답을 전송합니다 (sendResponse()).
       * sendResponse()는 CompletableFuture와 ExecutorService를 사용하여 응답 전송을 비동기적으로 처리하며, 시뮬레이션된 지연(10ms)을 포함합니다.
 */
@Slf4j
public class HttpEventHandler implements EventHandler {
    private final ExecutorService executorService = Executors.newFixedThreadPool(50);
    private final SocketChannel clientSocket;
    private final MsgCodec msgCodec;

    @SneakyThrows
    public HttpEventHandler(Selector selector, SocketChannel clientSocket) {
        this.clientSocket = clientSocket;
        this.clientSocket.configureBlocking(false);
        this.clientSocket.register(selector, SelectionKey.OP_READ).attach(this);
        this.msgCodec = new MsgCodec();
    }

    @Override
    public void handle() {
        String requestBody = handleRequest();
        log.info("requestBody: {}", requestBody);
        sendResponse(requestBody);
    }

    /**
     * GET /?name=taweoo HTTP/1.1
     * Host: localhost:8080
     * Connection: Keep-Alive
     * User-Agent: Apache-HttpClient/4.5.14 (Java/17.0.6)
     * Accept-Encoding: br,deflate,gzip,x-gzip
     *
     */
    @SneakyThrows
    private String handleRequest() {
        ByteBuffer requestByteBuffer = ByteBuffer.allocateDirect(1024);
        this.clientSocket.read(requestByteBuffer);
        return msgCodec.decode(requestByteBuffer);
    }

    @SneakyThrows
    private void sendResponse(String requestBody) {
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(10);

                ByteBuffer responeByteBuffer = msgCodec.encode(requestBody);
                this.clientSocket.write(responeByteBuffer);
                this.clientSocket.close();
            } catch (Exception e) { }
        }, executorService);
    }
}
