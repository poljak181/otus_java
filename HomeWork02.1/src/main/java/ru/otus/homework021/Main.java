package ru.otus.homework021;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String... args) {
        System.out.println("Size measurer\n");

        final var objectFactory = new Factory() {
            @Override
            public Object createElement(int arraySize) {
                return new Object();
            }
        };

        final var longObjectFactory = new Factory() {
            @Override
            public Long createElement(int arraySize) {
                return new Long(20L);
            }
        };

        final var stringFactory = new Factory() {
            @Override
            public String createElement(int arraySize) {
                return new String();
            }
        };

        final var myClassFactory  = new Factory() {
            @Override
            public MyClass createElement(int arraySize) {
                return new MyClass();
            }
        };

        final var byteArrayFactory = new Factory() {
            @Override
            public byte[] createElement(int arraySize) {
                return new byte[arraySize];
            }
        };

        final var intArrayFactory = new Factory() {
            @Override
            public int[] createElement(int arraySize) {
                return new int[arraySize];
            }
        };

        final var longArrayFactory = new Factory() {
            @Override
            public long[] createElement(int arraySize) {
                return new long[arraySize];
            }
        };

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
            SizeMeasurer.calculateSize(objectFactory, 0);
            SizeMeasurer.calculateSize(longObjectFactory, 0);
            SizeMeasurer.calculateSize(stringFactory, 0);
            SizeMeasurer.calculateSize(myClassFactory, 0);

            SizeMeasurer.calculateSize(byteArrayFactory, 0);
            SizeMeasurer.calculateSize(byteArrayFactory, 1);
            SizeMeasurer.calculateSize(byteArrayFactory, 8);
            SizeMeasurer.calculateSize(byteArrayFactory, 9);

            SizeMeasurer.calculateSize(intArrayFactory, 0);
            SizeMeasurer.calculateSize(intArrayFactory, 1);
            SizeMeasurer.calculateSize(intArrayFactory, 2);
            SizeMeasurer.calculateSize(intArrayFactory, 3);
            SizeMeasurer.calculateSize(intArrayFactory, 4);

            SizeMeasurer.calculateSize(longArrayFactory, 0);
            SizeMeasurer.calculateSize(longArrayFactory, 1);
            SizeMeasurer.calculateSize(longArrayFactory, 2);
            SizeMeasurer.calculateSize(longArrayFactory, 3);
            SizeMeasurer.calculateSize(longArrayFactory, 4);

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
