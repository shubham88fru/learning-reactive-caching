package com.learning.reactive.programming.reddisonplayground;

import com.learning.reactive.programming.reddisonplayground.assignment.Category;
import com.learning.reactive.programming.reddisonplayground.assignment.PriorityQueue;
import com.learning.reactive.programming.reddisonplayground.assignment.UserOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class PriorityQueueTest extends BaseTest {

    private PriorityQueue priorityQueue;

    @BeforeAll
    public void setupQueue() {
        RScoredSortedSetReactive<UserOrder> sortedSet = redissonClient
                .getScoredSortedSet("user:order:queue",
                new TypedJsonJacksonCodec(UserOrder.class));

        priorityQueue = new PriorityQueue(sortedSet);
    }

    @Test
    public void producer() {
        UserOrder u1 = new UserOrder(1, Category.GUEST);
        UserOrder u2 = new UserOrder(2, Category.STANDARD);
        UserOrder u3 = new UserOrder(3, Category.PRIME);
        UserOrder u4 = new UserOrder(4, Category.STANDARD);
        UserOrder u5 = new UserOrder(5, Category.GUEST);

        Mono<Void> mono = Flux.just(u1, u2, u3, u4, u5)
                .flatMap(priorityQueue::add)
                .then();

        StepVerifier.create(mono)
                .verifyComplete();

    }


    @Test
    public void consumer() {

        priorityQueue.takeItems()
                .delayElements(Duration.ofMillis(500))
                .doOnNext(System.out::println)
                .subscribe();

        sleep(600_000);

    }
}
