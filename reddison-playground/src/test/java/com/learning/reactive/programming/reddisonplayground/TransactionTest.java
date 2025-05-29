package com.learning.reactive.programming.reddisonplayground;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.api.RTransaction;
import org.redisson.api.RTransactionReactive;
import org.redisson.api.TransactionOptions;
import org.redisson.client.codec.LongCodec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class TransactionTest extends BaseTest {

    private RBucketReactive<Long> user1Balance;
    private RBucketReactive<Long> user2Balance;

    @BeforeAll
    public void accountSetup() {
        user1Balance =
                redissonClient.getBucket("user:1:balance", LongCodec.INSTANCE);

        user2Balance =
                redissonClient.getBucket("user:2:balance", LongCodec.INSTANCE);

        Mono<Void> mono = user1Balance.set(100L)
                        .then(user2Balance.set(0L))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }


    @AfterAll
    public void accountBalanceStatus() {
        Mono<Void> mono = Flux.zip(user1Balance.get(), user2Balance.get())
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }


    @Test
    public void nonTransactionTest() {

        transfer(user1Balance, user2Balance, 50)
                .thenReturn(0) //generate error
                .map(i -> 5/i)
                .doOnError(System.err::println)
                .subscribe();

        sleep(1000);
    }

    @Test
    public void transactionTest() {
        RTransactionReactive transaction = redissonClient
                .createTransaction(TransactionOptions.defaults());

        RBucketReactive<Long> user1Balance = transaction
                .getBucket("user:1:balance", LongCodec.INSTANCE);

        RBucketReactive<Long> user2Balance = transaction
                .getBucket("user:2:balance", LongCodec.INSTANCE);

        transfer(user1Balance, user2Balance, 50)
                .thenReturn(0) //generate error
                .map(i -> 5/i)
                .then(transaction.commit())
                .doOnError(System.err::println)
                .onErrorResume(ex -> transaction.rollback())
                .subscribe();

        sleep(1000);
    }

    private Mono<Void> transfer(RBucketReactive<Long> from, RBucketReactive<Long> to, int amount) {
        return  Flux.zip(from.get(), to.get())
                .filter(t -> t.getT1() >= amount)
                .flatMap(t -> from.set(t.getT1() - amount).thenReturn(t))
                .flatMap(t -> to.set(t.getT2() + amount).thenReturn(t))
                .then();
    }
}
