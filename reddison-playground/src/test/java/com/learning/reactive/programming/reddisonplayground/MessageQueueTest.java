package com.learning.reactive.programming.reddisonplayground;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RBlockingDequeReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class MessageQueueTest extends BaseTest {

    private RBlockingDequeReactive<Long> messageQueue;

    @BeforeAll
    public void setupQueue() {
        messageQueue =
                redissonClient.getBlockingDeque("message-queue", LongCodec.INSTANCE);


    }

    @Test
    public void consumer1() {
        messageQueue.takeElements()
                .doOnNext(i -> System.out.println("Consumer 1: " + i))
                .doOnError(System.out::println)
                .subscribe();

        sleep(600_000);
    }

    @Test
    public void consumer2() {
        messageQueue.takeElements()
                .doOnNext(i -> System.out.println("Consumer 2: " + i))
                .doOnError(System.out::println)
                .subscribe();

        sleep(600_000);
    }

    @Test
    public void producer() {
        Mono<Void> mono = Flux.range(1, 100)
                .delayElements(Duration.ofMillis(500))
                .doOnNext(i -> System.out.println("Going to add " + i))
                .flatMap(i -> messageQueue.add(Long.valueOf(i)))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }
}
