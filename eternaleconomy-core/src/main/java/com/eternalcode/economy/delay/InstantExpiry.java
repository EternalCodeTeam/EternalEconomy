package com.eternalcode.economy.delay;

import com.github.benmanes.caffeine.cache.Expiry;
import java.time.Duration;
import java.time.Instant;

class InstantExpiry<T> implements Expiry<T, Instant> {

    private static long timeToExpire(Instant expireTime) {
        Duration toExpire = Duration.between(Instant.now(), expireTime);
        if (toExpire.isNegative()) {
            return 0;
        }

        long nanos = toExpire.toNanos();
        if (nanos == 0) {
            return 1;
        }

        return nanos;
    }

    @Override
    public long expireAfterCreate(T key, Instant expireTime, long currentTime) {
        return timeToExpire(expireTime);
    }

    @Override
    public long expireAfterUpdate(T key, Instant newExpireTime, long currentTime, long currentDuration) {
        return timeToExpire(newExpireTime);
    }

    @Override
    public long expireAfterRead(T key, Instant value, long currentTime, long currentDuration) {
        return currentDuration;
    }
}
