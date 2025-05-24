package com.learning.reactive.programming.reddisonplayground;

import com.learning.reactive.programming.reddisonplayground.dto.Student;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMapReactive;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

public class MapTest extends BaseTest {

    @Test
    public void mapTest() {
        RMapReactive<String, String> map =
                redissonClient.getMap("user:1", StringCodec.INSTANCE);
        Mono<String> name = map.put("name", "same");
        Mono<String> age = map.put("age", "10");
        Mono<String> city = map.put("city", "atlanta");

        StepVerifier.create(name.concatWith(age).concatWith(city).then())
                .verifyComplete();
    }

    @Test
    public void mapTest2() {
        RMapReactive<String, String> map =
                redissonClient.getMap("user:2", StringCodec.INSTANCE);
        Map<String, String> javaMap = Map.of(
                "name", "jake",
                "age", "30",
                "city", "miami"
        );
        StepVerifier.create(map.putAll(javaMap).then())
                .verifyComplete();
    }

    @Test
    public void mapTest3() {
        RMapReactive<Integer, Student> map =
                redissonClient.getMap("users",
                        new TypedJsonJacksonCodec(Integer.class, Student.class));
        Student student = new Student("sam", 10, "atlanta", List.of(1, 2, 3));
        Student student2 = new Student("jake", 30, "miami", List.of(10, 20, 30));

        Mono<Student> mono1 = map.put(1, student);
        Mono<Student> mono2 = map.put(2, student2);

        StepVerifier.create(mono1.concatWith(mono2).then())
                .verifyComplete();
    }
}
