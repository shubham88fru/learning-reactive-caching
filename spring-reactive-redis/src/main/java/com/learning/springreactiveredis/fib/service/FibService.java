package com.learning.springreactiveredis.fib.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class FibService {

    // Usually on GET methods.
    @Cacheable(value = "math:fib", key = "#index")
    public int getFib(int index) {
        System.out.println("Calculating fib(" + index + ")");
        return fib(index);

    }

    // usually evict cache on PUT / POST / PATCH / DELETE methods.
    @CacheEvict(value = "math:fib", key = "#index")
    public void clearCache(int index) {
        System.out.println("Clearing fib(" + index + ")");
    }

    private int fib(int n) {
        if (n <= 1) return n;

        return fib(n-1) + fib(n-2);
    }
}
