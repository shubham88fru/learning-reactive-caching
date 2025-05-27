package com.learning.reactive.programming.reddisonplayground;

import org.junit.jupiter.api.Test;
import org.redisson.api.RTopicReactive;
import org.redisson.client.codec.StringCodec;

public class PubSubTest extends BaseTest {

    @Test
    public void subscriber1() {
        RTopicReactive topic = redissonClient.getTopic("slack-room", StringCodec.INSTANCE);
        topic.getMessages(String.class)
                .doOnError(System.out::println)
                .doOnError(System.out::println)
                .subscribe();

        sleep(600_000);
    }

    @Test
    public void subscriber2() {
        RTopicReactive topic = redissonClient.getTopic("slack-room", StringCodec.INSTANCE);
        topic.getMessages(String.class)
                .doOnError(System.out::println)
                .doOnError(System.out::println)
                .subscribe();

        sleep(600_000);
    }
}
