package reactorpattern.result;

/*
   1. `EventHandler.java`:
       * 모든 이벤트 핸들러가 구현해야 하는 인터페이스입니다. handle() 메서드를 정의하여 이벤트를 처리하는 공통 계약을 제공합니다.
 */
public interface EventHandler {
    void handle();
}
