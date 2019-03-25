package ru.otus.homework05.test;

import ru.otus.homework05.testframework.api.*;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class HashMapTest {
    private HashMap<String, Integer> map = new HashMap();

    HashMapTest() {
        System.out.println("---Constructor---");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Before all");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("After all");
    }

    @BeforeEach
    void beforeEach1() {
        System.out.println("Before each1");
    }

    @BeforeEach
    void beforeEach2() {
        System.out.println("Before each2");
    }

    @AfterEach
    void afterEach() {
        System.out.println("After each");
    }

    @Test
    void testPut() {
        System.out.println("Test put");
        String key = "MainAnswer";
        map.put(key, 33);
        assertEquals(42, map.get(key));
    }

    @Test
    void testRemove() {
        System.out.println("Test remove");
    }

    @Test
    void testClear() {
        System.out.println("Test clear");
    }

    @Test
    void testSize() {
        System.out.println("Test size");
    }
}
