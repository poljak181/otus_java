package ru.otus.homework022;

import java.util.function.Supplier;

public class Measurer {
    private Object[] array;
    private final int size;

    public Measurer() {
        size = 10_000_000;
    }

    public Measurer(int size) {
        this.size = size;
        array = new Object[size];
    }

    public void prepare() {
        final var memPerArray = getMemChanges(() -> {
            array = new Object[size];
        });

        System.out.println("Reference size: " + memPerArray/size);
    }

    public void measure(Supplier<Object> supplier, String name) {
        final var memChanges = getMemChanges(() -> {
            for (int i = 0; i < size; i++) {
                array[i] = supplier.get();
            }
        });

        System.out.println(name + " size: " + Math.round((double) memChanges/size));
    }

    public void clean() {
        array = new Object[size];
        BlockingGC.collect();
    }

    private static long getMemChanges(Runnable create) {
        final var runtime = Runtime.getRuntime();
        BlockingGC.collect();
        final long memBefore = runtime.totalMemory() - runtime.freeMemory();
        create.run();
        BlockingGC.collect();
        final long memAfter = runtime.totalMemory() - runtime.freeMemory();

        return memAfter - memBefore;
    }
}
