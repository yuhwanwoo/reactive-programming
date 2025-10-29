package com.example.webflux.practice.webflux.repository;

import com.example.webflux.practice.webflux.common.UserEntity;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
public class UserReactorRepository {
    private final Map<String, UserEntity> userMap;

    public UserReactorRepository() {
        var user = new UserEntity(
                "1234",
                "taewoo",
                32,
                "1",
                "1q2w3e4r!"
        );

        userMap = Map.of("1234", user);
    }

    @SneakyThrows
    public Mono<UserEntity> findById(String userId) {
        return Mono.create(sink -> {
            log.info("UserRepository.findById: {}", userId);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            UserEntity user = userMap.get(userId);
            if (user == null) {
                sink.success();
            } else {
                sink.success(user);
            }
        });
    }
}
