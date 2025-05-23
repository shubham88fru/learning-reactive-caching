package com.learning.reactive.programming.reddisonplayground;

import com.learning.reactive.programming.reddisonplayground.config.RedissonConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.redisson.api.RedissonReactiveClient;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseTest {

    private RedissonConfig redissonConfig = new RedissonConfig();
    protected RedissonReactiveClient redissonClient;

    @BeforeAll
    public void setClient() {
        redissonClient = redissonConfig.getReactiveClient();
    }

    @AfterAll
    public void shutdown() {
        redissonClient.shutdown();
    }

    protected void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
