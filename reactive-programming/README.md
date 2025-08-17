# I/O 관점에서의 Blocking/Non-blocking 
-> 어플리케이션과 커널사이의 일이기에 깊은 이야기 필요

## Blocking의 종류
* blcoking은 thread가 오랜 시간 일을 하거나 대기하는 경우 발생
* CPU-bound blocking : 오랜 시간 일을 한다
* IO-bound blocking : 오랜 시간 대기한다

### CPU-bound blocking
* thread가 대부분의 시간 CPU 점유
* 연산이 많은 경우
* 추가적인 코어를 투입

### IO-bound blocking
* thread가 대부분의 시간을 대기
* 파일 읽기/쓰기, network 요청 처리, 요청 전달 등
* IO-bound non-blockingdl rksmdgkek


### blocking의 전파
blocking한 함수를 하나라도 호출한다면 caller는 blocking이 된다.


### 동기 Blocking I/O
kernel이 응답을 돌려주기 전까지 applicaiton은 아무것도 하지 않는다
caller는 callee가 완료될 떄까지 대기 -> blocking
* recvfrom 을 호출
* blocking socket을 이용해서 read/write를 수행
* 쓰레드가 blocking 된다(wait queue에서 기다린다)

### 동기 Non-Blocking I/O
kernel이 응답을 돌려주기 전에 application은 다른 일을 수행
caller는 callee를 기다리지 않고 본인의 일을 한다 -> Non-blocking
* recvfrom을 주기적으로 호출
* non-blocking socket을 이용해서 read/write 수행
* 작업이 완료되지 않았다면 EAGAIN/EWOULDBLOCK 에러 반환

### 비동기 Non-blocking I/O
* aio_read 호출
* 작업이 완료되면 커널이 완료 시그널을 보내거나 callback 호출

### CompletableFuture 클래스
Future와 CompletionStage의 구현체
* Future
비동기적인 작업을 수행
해당 작업이 완료되면 결과를 반환하는 인터페이스

* CompletionStage
비동기적인 작업을 수행
해당 작업이 완료되면 결과를 처리하거나 다른 CompletionStage를 연결하는 인터페이스

### ExecutorService
쓰레드 풀을 이용하여 비동기적으로 작업을 실행하고 관리
별도의 쓰레드를 생성하고 관리하지 않아도 되므로, 코드를 간결하게 유지 가능
쓰레드 풀을 이용하여 자원을 효율적으로 관리

### ExecutorService 메소드
* execute : Runnable 인터페이스를 구현한 작업을 쓰레드 풀에서 비동기적으로 실행
* submit : Callable 인터페이스를 구현한 작업을 쓰레드풀에서 비동기적으로 실행하고, 해당 작업의 결과를 Future<T>객체로 반환
* shutdown : ExecutorService를 종료, 더이상 task를 받지 않는다.

### Executors - ExecutorService 생성
* newSingleThreadExecutor : 단일 쓰레드로 구성된 스레드 풀을 생성. 한 번에 하나의 작업만 실행
* newFixedThreadPool : 고정된 크기의 쓰레드 풀을 생성. 크기는 인자로 주어진 n과 동일
* newCachedThreadPool : 사용 가능한 쓰레드가 없다면 새로생성해서 작업을 처리하고, 있다면 재사용. 쓰레드가 일정 시간 사용되지 않으면 회수
* newScheduledThreadPool : 스케줄링 기능을 갖춘 고정 크기의 쓰레드 풀을 생성. 주기적이거나 지연이 발생하는 작업을 실행
* newWorkStealingPool : work steal 알고리즘을 사용하는 ForkJoinPool을 생성

### Future: get()
결과를 구할 떄까지 thread가 계속 block
future에서 무한 루프나 오랜 시간이 걸린다면 thread가 blocking 상태 유지

### Future: get(long timeout, TimeUnit unit)
결과를 구할 떄까지 timeout 동안 thread가 block
timeout이 넘어가도 응답이 반환되지 않으면 TimeoutException 발생

### CompletionStage 연산자 조합
50개 가까운 연산자들을 활용하여 비동기 task들을 실행하고 값을 변형하는 등 chaining을 이용한 조합 가능
에러를 처리하기 위한 콜백 제공
-> 비동기 non-blocking으로 작동 하려면 별도의 쓰레드가 필요한데
CompletionStage 자세하게는 CompletableFuture에서는 ForkJoinPool이라는 쓰레드풀을 사용하고 있다 -> newWorkStealingPool

### ForJoinPool - thread pool
* CompletableFuture는 내부적으로 비동기 함수들을 실행하기 위해 ForkJoinPool을 사용
* ForkJoinPool의 기본 size=할당된 cpu 코어 -1
* 데몬 쓰레드. main 쓰레드가 종료되면 즉각적으로 종료

### ForJoinPool - fork & join
* Task를 fork를 통해서 subtask로 나누고
* Thread pool에서 steal work 알고리즘을 이용해서 균등하게 처리해서
* join을 통해서 결과를 생성


### CompletionStage 연산자
* thenAccept [Async]
Consumer를 파라미터로 받는다
이전 task로부터 값을 받지만 값을 넘기지 않는다
다음 task에게 Null이 전달된다
값을 받아서 action만 수행하는 경우 유용

### thenAccept [Async]의 실행 쓰레드
done 상태에서 thenAccept는 caller(main)의 쓰레드에서 실행
done 상태의 completionStage에 thenAccept를 사용하는 경우, caller쓰레드를 block할 수 있다.
done 상태가 아닌 thenAccept는 callee의 쓰레드에서 실행
done 상태가 아닌 completionStage에 thenAccept를 사용하는 경우, callee쓰레드를 block할 수 있다.

### then*Async의 쓰레드풀 변경
모든 then*Async 연산자는 executor를 추가 인자로 받는다.
이를 통해서 다른 쓰레드풀로 task를 실행할 수 있다.

### thenApply [Async]
Function을 파라미터로 받는다
이전 task로 부터 T타입의 값을 받아서 가공하고 U타입의 값을 반환한다.
다음 task에게 반환했던 값이 전달된다
값을 변형해서 전달해야하는 경우 유용

### thenCompose [Async]
Function을 파라미터로 받는다
이전 task로부터 T타입의 값을 받아서 가공하고 U 타입의 CompletionStage를 반환한다
반환한 CompletionStage가 done상태가 되면 값을 다음 task에 전달한다
다른 Future를 반환해야하는 경우 유용

### thenRun [Async]
Runnable을 파라미터로 받는다
이전 task로부터 값을 받지 않고 값을 반환하지 않는다
다음 task에게 Null이 전달된다
future가 완료되었다는 이벤트를 기록할 때 유용

### exceptionally
Function을 파라미터로 받는다
이전 task에서 발생한 exception을 받아서 처리하고 값을 반환한다
다음 task에게 반환된 값을 전달한다
future 파이프에서 발생한 에러를 처리할 때 유용

## CompletableFuture의 complete 메소드
CompletableFuture가 완료되지 않았다면 주어진 값으로 채운다.
complete에 의해서 상태가 바뀌었다면 true, 아니라면 false를 반환한다.

## CompletableFuture의 allOf 메소드
여러 CompletableFuture를 모아서 하나의 CompletableFuture로 반환할 수 있다
모든 CompletableFuture가 완료되면 상태가 done으로 변경
Void를 반환하므로 각각의 값에 get으로 접근해야 한다.


## CompletableFuture의 anyOf 메소드
여러 CompletableFuture를 모악서 하나의 CompletableFuture로 변환할 수 있다
주어진 future중 하나라도 완료되면 상태가 done으로 변경
제일먼저 done 상태가 되는 future의 값을 반환

# Reactive programming
비동기 데이터 stream을 사용하는 패러다임

# Reactive streams 구현 라이브러리
### Project reactor
  Reactive streams 대부분 지원
Webflux 기반이 되는 라이브러리
pivotal 에서 개발
Spring react에서 사용
Mono와 Flux publisher 제공

* Project reactor - Flux
0..n개의 item을 전달
에러가 발생하면 error signal 전달하고 종료
모든 item을 전달했다면 complete signal 전달하고 종료
backPressure 지원

* Project reactor - Mono
0..1개의 item을 전달
에러가 발생하면 error signal 전달하고 종료
모든 item을 전달했다면 complete signal 전달하고 종료

* Mono와 Flux
Mono<T>:Optional<T>
없거나 혹은 하나의 값
Mono<Void>로 특정 사건이 완료되는 시점을 가리킬 수도 있다

Flux<T>: List<T>
무한하거나 유한한 여러 개의 값

Flux를 Mono로
-> Mono.from으로 Flux를 Mono로
-> 첫 번쨰 값만 전달

collectList() -> Flux의 값들을 collect 하고 complete 이벤트가 발생하는 시점에 모은 값들을 전달

Mono를 Flux로
-> flux() : Mono를 next 한 번 호출하고 onComplete를 호출하는 Flux로 변환

### RxJava
  독립적인편
reactive extensions -> 자바형태로 porting해서 제공

### Mutiny
  독립적인편
레드햇에서 만듬 하이버네이트 리액티브 지원하기 위해

  