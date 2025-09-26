package reactorpattern.result;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
  5. `Reactor.java`:
       * 리액터 패턴의 핵심 구성 요소입니다.
       * Selector와 ServerSocketChannel을 초기화하고, 지정된 포트에 바인딩하며 논블로킹 모드로 설정합니다.
       * Acceptor를 Selector에 OP_ACCEPT 이벤트로 등록합니다.
       * run() 메서드에서 메인 이벤트 루프를 실행합니다. 이 루프는 Selector가 이벤트를 감지할 때까지 대기하고 (selector.select()), 이벤트가 발생하면 selectedKeys를 순회하며 각 이벤트를 해당 EventHandler로
         dispatch()합니다.
       * dispatch(): SelectionKey에 연결된 EventHandler를 가져와 handle() 메서드를 호출합니다.
 */
@Slf4j
public class Reactor implements Runnable {
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final ServerSocketChannel serverSocket;
    private final Selector selector;

    @SneakyThrows
    public Reactor(int port) {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", port));
        serverSocket.configureBlocking(false);

        var acceptor = new Acceptor(selector, serverSocket);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT).attach(acceptor);
    }

    @Override
    public void run() {
        executorService.submit(() -> {
            while (true) {
                selector.select();
                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();

                while (selectedKeys.hasNext()) {
                    SelectionKey key = selectedKeys.next();
                    selectedKeys.remove();

                    dispatch(key);
                }
            }
        });
    }

    private void dispatch(SelectionKey selectionKey) {
        EventHandler eventHandler = (EventHandler) selectionKey.attachment();

        if (selectionKey.isReadable() || selectionKey.isAcceptable()) {
            eventHandler.handle();
        }
    }
}
