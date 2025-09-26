package reactorpattern.result;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
   6. `TcpEventHandler.java`:
       * HttpEventHandler와 유사하게 EventHandler를 구현하지만, 일반적인 TCP 통신을 처리합니다.
       * 클라이언트 소켓을 Selector에 OP_READ 이벤트로 등록합니다.
       * handle() 메서드에서 클라이언트로부터의 요청을 읽고 (handleRequest()), 로그를 남긴 후 응답을 전송합니다 (sendResponse()).
       * handleRequest(): SocketChannel에서 원시 바이트를 읽어 문자열로 변환합니다.
       * sendResponse(): "received: [요청 본문]" 형태의 간단한 메시지를 비동기적으로 클라이언트에 다시 보냅니다.
 */
@Slf4j
public class TcpEventHandler implements EventHandler {
    private final ExecutorService executorService = Executors.newFixedThreadPool(50);
    private final SocketChannel clientSocket;

    @SneakyThrows
    public TcpEventHandler(Selector selector, SocketChannel clientSocket) {
        this.clientSocket = clientSocket;
        this.clientSocket.configureBlocking(false);
        this.clientSocket.register(selector, SelectionKey.OP_READ).attach(this);
    }

    @Override
    public void handle() {
        String requestBody = handleRequest(this.clientSocket);
        log.info("requestBody: {}", requestBody);
        sendResponse(this.clientSocket, requestBody);
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
    private String handleRequest(SocketChannel clientSocket) {
        ByteBuffer requestByteBuffer = ByteBuffer.allocateDirect(1024);
        clientSocket.read(requestByteBuffer);

        requestByteBuffer.flip();
        String requestBody = StandardCharsets.UTF_8.decode(requestByteBuffer).toString();
        log.info("request: {}", requestBody);

        return requestBody;
    }

    @SneakyThrows
    private void sendResponse(SocketChannel clientSocket, String requestBody) {
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(10);

                String content = "received: " + requestBody;
                ByteBuffer responeByteBuffer = ByteBuffer.wrap(content.getBytes());
                clientSocket.write(responeByteBuffer);
                clientSocket.close();
            } catch (Exception e) { }
        }, executorService);
    }
}
