package com.learning.reactive.programming.reddisonplayground.assignment;

import org.redisson.api.RScoredSortedSetReactive;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PriorityQueue {

    RScoredSortedSetReactive<UserOrder> queue;

    public PriorityQueue(RScoredSortedSetReactive<UserOrder> queue) {
        this.queue = queue;
    }

    public Mono<Void> add(UserOrder userOrder) {
        return queue.add(getScore(userOrder.getCategory()), userOrder)
                .then();
    }

    public Flux<UserOrder> takeItems() {
        return queue.takeFirstElements()
                .limitRate(1);
    }

    private double getScore(Category category) {
        return category.ordinal() + Double.parseDouble("0." + System.nanoTime());
    }
}
