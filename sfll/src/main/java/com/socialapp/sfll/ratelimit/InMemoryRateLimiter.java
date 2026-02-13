package com.socialapp.sfll.ratelimit;


import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRateLimiter implements RateLimiter {

    private static class Bucket {
        int tokens;
        long lastRefill;
    }

    private final int capacity;
    private final int refillRate; // tokens per second

    private final ConcurrentHashMap<String, Bucket> buckets
            = new ConcurrentHashMap<>();

    public InMemoryRateLimiter(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
    }

    @Override
    public boolean allowRequest(String key) {

        // check blacklist, etc. here if needed
        // if present in blacklist, return false immediately
        Bucket bucket = buckets.computeIfAbsent(key, k -> {
            Bucket b = new Bucket();
            b.tokens = capacity;
            b.lastRefill = Instant.now().getEpochSecond();
            return b;
        });

        synchronized (bucket) {

            refill(bucket);

            if (bucket.tokens > 0) {
                bucket.tokens--;
                return true;
            }

            return false;
        }
    }

    private void refill(Bucket bucket) {

        long now = Instant.now().getEpochSecond();
        long elapsed = now - bucket.lastRefill;

        int refill = (int) (elapsed * refillRate);

        if (refill > 0) {

            bucket.tokens = Math.min(
                    capacity,
                    bucket.tokens + refill
            );

            bucket.lastRefill = now;
        }
    }
}
