package com.learning.reactive.programming.reddisonplayground;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;

public class KeyValueTest extends BaseTest{

    @Test
    public void keyValueAccessTest() {
        RBucketReactive<String> bucket = redissonClient
                .getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("shubham");
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }


    @Test
    public void keyValueExpiryTest() {
        RBucketReactive<String> bucket = redissonClient
                .getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("shubham", 10, TimeUnit.SECONDS);
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(set.concatWith(get))
                .verifyComplete();
    }

    @Test
    public void keyValueExtendExpiryTest() {
        RBucketReactive<String> bucket = redissonClient
                .getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("shubham", 10, TimeUnit.SECONDS);
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(set.concatWith(get))
                .verifyComplete();

        sleep(5000);

        Mono<Boolean> mono = bucket.expire(60, TimeUnit.SECONDS);
        StepVerifier.create(mono)
                .expectNext(true)
                .verifyComplete();

        Mono<Void> ttl = bucket.remainTimeToLive()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(ttl)
                .verifyComplete();
    }
}
