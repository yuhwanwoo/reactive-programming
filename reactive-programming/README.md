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
