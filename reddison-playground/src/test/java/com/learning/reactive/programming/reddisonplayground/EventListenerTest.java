package com.learning.reactive.programming.reddisonplayground;

import org.junit.jupiter.api.Test;
import org.redisson.api.DeletedObjectListener;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.api.RBucketReactive;
import org.redisson.client.codec.StringCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;

public class EventListenerTest extends BaseTest {

    @Test
    public void expiredEventTest() {
        RBucketReactive<String> bucket = redissonClient
                .getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("shubham", 10, TimeUnit.SECONDS);
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();

        //Following command must also be run on redis cli, to get a notification
        //`config set notify-keyspace-events AKE`
        Mono<Void> expiredEvent = bucket
                .addListener((ExpiredObjectListener) System.out::println)
                .then();

        StepVerifier.create(set.concatWith(get).concatWith(expiredEvent))
                .verifyComplete();

        sleep(11000);
    }


    @Test
    public void deletedEventTest() {
        RBucketReactive<String> bucket = redissonClient
                .getBucket("user:1:name", StringCodec.INSTANCE);
        Mono<Void> set = bucket.set("shubham");
        Mono<Void> get = bucket.get()
                .doOnNext(System.out::println)
                .then();

        //Following command must also be run on redis cli, to get a notification
        //`config set notify-keyspace-events AKE`
        Mono<Void> deletedEvent = bucket
                .addListener((DeletedObjectListener) System.out::println)
                .then();

        StepVerifier.create(set.concatWith(get).concatWith(deletedEvent))
                .verifyComplete();

        sleep(60000);
    }
}
