package ru.otus.homework06.cache.api;

public interface CacheEngine<K, V> {
    void put(K key, V value);

    V get(K key);

    int getHitCount();

    int getMissCount();

    void dispose();

    boolean containsKey(K key);

    void clear();
}
