package com.learning.reactive.programming.reddisonplayground;

import com.learning.reactive.programming.reddisonplayground.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapCacheReactive;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MapCacheTest extends BaseTest {

    @Test
    public void mapCacheTest() {

        RMapCacheReactive<Integer, Student> mapCache = redissonClient.getMapCache("users:cache",
                new TypedJsonJacksonCodec(Integer.class, Student.class));


        Student student = new Student("sam", 10, "atlanta", List.of(1, 2, 3));
        Student student2 = new Student("jake", 30, "miami", List.of(10, 20, 30));

        Mono<Student> st1 = mapCache.put(1, student, 5, TimeUnit.SECONDS);
        Mono<Student> st2 = mapCache.put(2, student2, 10, TimeUnit.SECONDS);

        StepVerifier.create(st1.concatWith(st2).then())
                .verifyComplete();

        sleep(3000);

        mapCache.get(1).doOnNext(System.out::println).subscribe();
        mapCache.get(2).doOnNext(System.out::println).subscribe();

        sleep(3000);
        mapCache.get(1).doOnNext(System.out::println).subscribe();
        mapCache.get(2).doOnNext(System.out::println).subscribe();
    }
}
