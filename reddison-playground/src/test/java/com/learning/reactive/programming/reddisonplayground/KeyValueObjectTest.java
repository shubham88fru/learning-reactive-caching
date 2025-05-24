package com.learning.reactive.programming.reddisonplayground;

import com.learning.reactive.programming.reddisonplayground.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RBucketReactive;
import org.redisson.codec.JsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

public class KeyValueObjectTest extends BaseTest {

    @Test
    public void keyValueObjectTest() {
        Student student = new Student("Shubham", 28, "Plano", List.of(1, 2));
        RBucketReactive<Student> bucket = redissonClient
                .getBucket("student:1", JsonJacksonCodec.INSTANCE);
        Mono<Void> setMono = bucket.set(student);
        Mono<Void> getMono = bucket.get()
                .doOnNext(System.out::println)
                .then();

        StepVerifier.create(setMono.concatWith(getMono))
                .verifyComplete();

    }
}
