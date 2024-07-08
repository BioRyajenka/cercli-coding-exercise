package com.example.util

import com.github.benmanes.caffeine.cache.Caffeine
import java.time.Clock
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.toJavaDuration

class SingleValueCache<T>(expireAfterWrite: Duration, clock: Clock, get: () -> T) {
    private val fakeKey = Any()
    private val cache = Caffeine.newBuilder()
        .expireAfterWrite(expireAfterWrite.toJavaDuration())
        .ticker { TimeUnit.MILLISECONDS.toNanos(clock.millis()) }
        .build { _: Any -> get() }

    fun get(): T = cache.get(fakeKey)
}
