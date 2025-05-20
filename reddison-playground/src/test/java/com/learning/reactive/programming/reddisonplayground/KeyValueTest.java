package com.learning.reactive.programming.reddisonplayground;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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
}
