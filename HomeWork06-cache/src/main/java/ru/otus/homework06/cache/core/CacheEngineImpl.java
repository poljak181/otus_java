package ru.otus.homework06.cache.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework06.cache.api.CacheEngine;

import java.lang.ref.SoftReference;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {
    private static final Logger LOG = LoggerFactory.getLogger(CacheEngineImpl.class);
    private static final long TIME_THRESHOLD_MS = 5;

    private ConcurrentHashMap<K, SoftReference<CacheElement<K, V>>> map = new ConcurrentHashMap<>();
    private AtomicInteger hitCount = new AtomicInteger(0);
    private AtomicInteger missCount = new AtomicInteger(0);
    private Timer timer = new Timer();

    private final long lifeTimeMs;
    private final long idleTimeMs;
    private final boolean isEternal;

    public CacheEngineImpl(long lifeTimeMs, long idleTimeMs, boolean isEternal) {
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
        this.isEternal = lifeTimeMs == 0 && idleTimeMs == 0 || isEternal;

        LOG.info("Cache engine created. LifeTime = {}ms, IdleTime = {}ms, IsEternal = {}",
                lifeTimeMs, idleTimeMs, isEternal);
    }

    @Override
    public void put(K key, V value) {
        map.put(key, new SoftReference<>(new CacheElement(key, value)));
        LOG.trace("Put element in cache, key: {}, size: {}", key, map.size());

        if (isEternal) {
            return;
        }

        if (lifeTimeMs != 0) {
            var lifeTimerTask = getTimerTask(key, kvCacheElement -> kvCacheElement.getCreationTime() + lifeTimeMs, "checkLifeTime");
            timer.schedule(lifeTimerTask, lifeTimeMs);
        }

        if (idleTimeMs != 0) {
            var idleTimerTask = getTimerTask(key, kvCacheElement -> kvCacheElement.getLastAccessTime() + idleTimeMs, "checkIdleTime");
            timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);
        }
    }

    @Override
    public V get(K key) {
        final var result = map.get(key);
        if (result == null || result.get() == null) {
            missCount.incrementAndGet();
            LOG.trace("Miss element with key: {} (hit count: {}, miss count: {})", key, hitCount.get(), missCount.get());
            return null;
        } else {
            hitCount.incrementAndGet();
            result.get().setAccessed();
            LOG.trace("Hit element with key: {} (hit count: {}, miss count: {})", key, hitCount.get(), missCount.get());
            return result.get().getValue();
        }
    }

    @Override
    public int getHitCount() {
        return hitCount.get();
    }

    @Override
    public int getMissCount() {
        return missCount.get();
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    @Override
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    @Override
    public void clear() {
        map.clear();
        hitCount.set(0);
        missCount.set(0);
    }

    private TimerTask getTimerTask(K key, Function<CacheElement<K, V>, Long> timeFunction, String taskName) {
        return new TimerTask() {
            @Override
            public void run() {
                LOG.trace("Start task {}, thread id: {}",  taskName, Thread.currentThread().getId());
                var element = map.get(key);
                if (element == null ||
                        (element.get() != null && isT1BeforeT2(timeFunction.apply(element.get()), System.currentTimeMillis()))) {
                    map.remove(key);
                    LOG.trace("{}: remove element", taskName);
                    this.cancel();
                }
            }
        };
    }

    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + TIME_THRESHOLD_MS;
    }
}
