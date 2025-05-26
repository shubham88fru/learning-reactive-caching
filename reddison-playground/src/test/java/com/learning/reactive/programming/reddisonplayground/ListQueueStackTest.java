package com.learning.reactive.programming.reddisonplayground;

import org.junit.jupiter.api.Test;
import org.redisson.api.RListReactive;
import org.redisson.api.RQueueReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.LongStream;

//wasn't working properly.
public class ListQueueStackTest extends BaseTest {

    @Test
    public void listTest() {
        RListReactive<Long> list = redissonClient.getList("number-input", LongCodec.INSTANCE);

        List<Long> longList = LongStream.rangeClosed(1, 10)
                                .boxed()
                        .toList();

        StepVerifier.create(list.addAll(longList).then())
                .verifyComplete();

//        StepVerifier.create(list.size())
////                .expectNext(10)
//                .verifyComplete();
    }


    @Test
    public void queueTest() {
        RQueueReactive<Long> queue = redissonClient.getQueue("number-input", LongCodec.INSTANCE);

        Mono<Void> queuePoll = queue.poll()
                .repeat(3)
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(queuePoll)
                .verifyComplete();

//        StepVerifier.create(queue.size())
////                .expectNext(6)
//                .verifyComplete();
    }

}
