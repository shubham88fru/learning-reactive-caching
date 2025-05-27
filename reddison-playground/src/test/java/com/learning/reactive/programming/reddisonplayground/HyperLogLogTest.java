package com.learning.reactive.programming.reddisonplayground;

import org.junit.jupiter.api.Test;
import org.redisson.api.RHyperLogLogReactive;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class HyperLogLogTest extends BaseTest {

    @Test
    public void count() {
        RHyperLogLogReactive<Long> counter = redissonClient
                .getHyperLogLog("user:visits", LongCodec.INSTANCE);

        List<Long> list1 = LongStream.rangeClosed(1, 25000)
                .boxed()
                .collect(Collectors.toUnmodifiableList());

        List<Long> list2 = LongStream.rangeClosed(25001, 5000)
                .boxed()
                .collect(Collectors.toUnmodifiableList());


        List<Long> list3 = LongStream.rangeClosed(1, 75000)
                .boxed()
                .collect(Collectors.toUnmodifiableList());

        List<Long> list4 = LongStream.rangeClosed(50000, 100_000)
                .boxed()
                .collect(Collectors.toUnmodifiableList());

        Mono<Void> mono = Flux.just(list1, list2, list3, list4)
                        .flatMap(counter::addAll)
                                .then();

        StepVerifier.create(mono)
                .verifyComplete();

        counter.count()
                .doOnNext(System.out::println)
                .subscribe();
    }
}
