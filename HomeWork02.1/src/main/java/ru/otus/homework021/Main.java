package ru.otus.homework021;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String... args) {
        System.out.println("Size measurer\n");

        final var longArrayListFactory = new Factory() {
            @Override
            public List<Long> createElement(int arraySize) {
                var list = new ArrayList<Long>();
                for (int i = 0; i < arraySize; i++) {
                    list.add(1000L*i);
                }
                return list;
            }
        };

        final var longArrayListSameObjectsFactory = new Factory() {
            @Override
            public List<Long> createElement(int arraySize) {
                var list = new ArrayList<Long>();
                for (int i = 0; i < arraySize; i++) {
                    list.add(100L);
                }
                return list;
            }
        };

        try {
            SizeMeasurer.calculateSize((size) -> new Object(), 0);
            SizeMeasurer.calculateSize((size) -> new Long(20L), 0);
            SizeMeasurer.calculateSize((size) -> new String(), 0);
            SizeMeasurer.calculateSize((size) -> new MyClass(), 0);

            SizeMeasurer.calculateSize((size) -> new byte[size], 0);
            SizeMeasurer.calculateSize((size) -> new byte[size], 1);
            SizeMeasurer.calculateSize((size) -> new byte[size], 8);
            SizeMeasurer.calculateSize((size) -> new byte[size], 9);

            SizeMeasurer.calculateSize((size) -> new int[size], 0);
            SizeMeasurer.calculateSize((size) -> new int[size], 1);
            SizeMeasurer.calculateSize((size) -> new int[size], 2);
            SizeMeasurer.calculateSize((size) -> new int[size], 3);
            SizeMeasurer.calculateSize((size) -> new int[size], 4);

            SizeMeasurer.calculateSize((size) -> new long[size], 0);
            SizeMeasurer.calculateSize((size) -> new long[size], 1);
            SizeMeasurer.calculateSize((size) -> new long[size], 2);
            SizeMeasurer.calculateSize((size) -> new long[size], 3);
            SizeMeasurer.calculateSize((size) -> new long[size], 4);

            System.out.println("\nArrayList<Long>");
            SizeMeasurer.calculateSize(longArrayListFactory, 0);
            SizeMeasurer.calculateSize(longArrayListFactory, 1);
            SizeMeasurer.calculateSize(longArrayListFactory, 2);
            SizeMeasurer.calculateSize(longArrayListFactory, 3);
            SizeMeasurer.calculateSize(longArrayListFactory, 4);

            System.out.println("\nArrayList<Long> with same objects");
            SizeMeasurer.calculateSize(longArrayListSameObjectsFactory, 0);
            SizeMeasurer.calculateSize(longArrayListSameObjectsFactory, 1);
            SizeMeasurer.calculateSize(longArrayListSameObjectsFactory, 10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
