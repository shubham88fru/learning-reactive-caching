package com.learning.reactive.programming.reddisonplayground;

import org.junit.jupiter.api.Test;
import org.redisson.api.*;
import org.redisson.client.codec.LongCodec;
import reactor.test.StepVerifier;

public class BatchTest extends BaseTest  {

    @Test
    public void batchTest() {
        RBatchReactive batch = redissonClient.createBatch(BatchOptions.defaults());
        RListReactive<Long> list = batch.getList("numbers-list", LongCodec.INSTANCE);
        RSetReactive<Long> set = batch.getSet("numbers-set", LongCodec.INSTANCE);

        for (long i=0; i<500_000; i++) {
            list.add(i);
            set.add(i);
        }

        StepVerifier.create(batch.execute().then())
                .verifyComplete();

    }
}
