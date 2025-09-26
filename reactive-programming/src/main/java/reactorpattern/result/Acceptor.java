package reactorpattern.result;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/*
   2. `Acceptor.java`:
       * EventHandler를 구현합니다.
       * 새로운 클라이언트 연결 요청(SelectionKey.OP_ACCEPT 이벤트)을 수락하는 역할을 합니다.
       * 클라이언트 연결이 수락되면, 해당 클라이언트 소켓을 위한 HttpEventHandler (또는 TcpEventHandler) 인스턴스를 생성하고, 이 핸들러를 Selector에 등록하여 클라이언트로부터의 읽기
         이벤트(SelectionKey.OP_READ)를 처리하도록 합니다.
 */
@Slf4j
@RequiredArgsConstructor
public class Acceptor implements EventHandler {
    private final Selector selector;
    private final ServerSocketChannel serverSocketChannel;

    @SneakyThrows
    @Override
    public void handle() {
        SocketChannel clientSocket = serverSocketChannel.accept();
        new HttpEventHandler(selector, clientSocket);
    }
}
