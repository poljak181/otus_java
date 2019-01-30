package ru.otus.homework021;

import java.util.Collection;

public class SizeMeasurer {
    public static long getUsedMemory() throws InterruptedException {
        System.gc();
        Thread.sleep(10);
        final var runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    static public void calculateSize(Factory factory, int arraySize) throws InterruptedException {
        final int size = 10_000_000;
        final var usedMemoryAtStart = getUsedMemory();
        var array = new Object[size];
        final var usedMemoryAfterArrayCreated = getUsedMemory();

        for (int i = 0; i < array.length; i++) {
            array[i] = factory.createElement(arraySize);
        }

        final var usedMemoryAfterArrayFilled = getUsedMemory();

        final var refSize = (usedMemoryAfterArrayCreated - usedMemoryAtStart)/array.length;
        final var objectSize = (usedMemoryAfterArrayFilled - usedMemoryAfterArrayCreated)/array.length;

        final var isArray = array[0].getClass().isArray();
        final var isCollection = array[0] instanceof Collection<?>;
        final String typeName = array[0].getClass().getTypeName();

        if (isArray || isCollection) {
            System.out.println("Size of " + typeName + " with " + arraySize + " elements is: " + objectSize);
        } else  {
            System.out.println("Size of " + typeName + ": " + objectSize);
        }
    }
}
