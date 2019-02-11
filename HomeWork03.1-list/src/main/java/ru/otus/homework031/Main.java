package ru.otus.homework031;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Main {
    private static <T> MyArrayList<T> prepareList(T... t) {
        var list = new MyArrayList<T>();
        for (var e : t)
            list.add(e);
        return list;
    }

    private static void testAdd() {
        var list = new MyArrayList<Integer>();
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
        System.out.println("Result of adding null: " + list.add(null));
    }

    private static void testAddIndex() {
        var list = new MyArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        list.add(4, 111);
    }

    private static void testRemoveObject() {
        var list = prepareList(1, 2, 3, 4, 5, 1, 2, 3, 4, 5);
        list.remove(Integer.valueOf(3));

        System.out.println("Removed object: " + list.remove(7) + ", size: " + list.size());
        System.out.println("Removed object: " + list.remove(0) + ", size: " + list.size());
    }

    private static void testIterator() {
        var list = prepareList(1, 2, 3, 4, 5);

        final var iter = list.iterator();

        while (iter.hasNext()) {
            System.out.println(iter.next());
        }
    }

    private static void testRemoveAll() {
        var list = prepareList(1, 2, 3, 4, 5);

        Collection<Integer> c = new ArrayList<>();
        c.add(1);
        c.add(4);
        c.add(5);

        list.removeAll(c);
    }

    private static void testToArray() {
        var list = prepareList(1, 2, 3, 4, 5);

        var arr = list.toArray();
        System.out.println("New array length: " + arr.length);
        System.out.println("Type of array: " + arr.getClass().getTypeName());

        arr[2] = 777;
        list.add(6);
        System.out.println("new array   : " + Arrays.toString(arr));
    }

    private static void testGenericToArray() {
        var list = prepareList(1, 2, 3, 4, 5);
        var intArr1 = new Integer[]{3, 2, 1};
        intArr1 = list.toArray(intArr1);
        System.out.println("Arr1: " + Arrays.toString(intArr1));

        var intArr2 = new Integer[]{3, 2, 1, 5, 6, 7, 8, 9, 10};
        intArr2 = list.toArray(intArr2);
        System.out.println("Arr2: " + Arrays.toString(intArr2));
    }

    private static void testIndexOfObject() {
        final var list = prepareList(1, 3, 5, 7, 9);

        System.out.println("IndexOf(5)" + list.indexOf(5));
        System.out.println("IndexOf(0)" + list.indexOf(0));
        System.out.println("IndexOf(1)" + list.indexOf(1));

        try {
            System.out.println("IndexOf(1)" + list.indexOf(null));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testLastIndexOfObject() {
        final var list = prepareList(1, 6, 5, 6, 4, 4, 4, 6);
        System.out.println("size:" + list.size());
        System.out.println("IndexOf(1)" + list.lastIndexOf(1));
        System.out.println("IndexOf(6)" + list.lastIndexOf(6));
    }

    private static void testSet() {
        final var list = prepareList(1, 2, 3, 4, 5);
        list.set(4, 42);
        System.out.println(Arrays.toString(list.toArray()));
    }

    private static void testArrayList() {
        final var list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
        System.out.println("ArrayList contains 1, 1, 1: " + list.containsAll(Arrays.asList(1, 1, 1)));
    }

    private static void testAddAllCollection() {
        var list = prepareList(1, 2, 3, 4, 5);
        Collection<Integer> c = Arrays.asList(6, 7, 8);
//        list.addAll(0, c);
//        list.addAll(c.size(), c);
//        list.addAll(2, c);
        list.addAll(c);
        list.print();
    }

    private static void testRetainAll() {
        var list = prepareList(1, 2, 4, 4, 4, 5, 3, 1, 1, 1);
//        var list = new ArrayList<>(Arrays.asList(1, 2, 4, 4, 4, 5, 3, 1, 1, 1));
        Collection<Integer> c = Arrays.asList(4, 2, 1);
        list.retainAll(c);

        list.print();
        list.forEach(System.out::println);
    }

    private static void testListIterator() {

    }

    private static void testListIteratorWithIndex() {

    }

    public static void main(String[] args) {
//        testAdd();
//        testAddIndex();
//        testRemoveObject();
//        testIterator();
//        testRemoveAll();
//        testToArray();
//        testGenericToArray();
//        testIndexOfObject();
//        testLastIndexOfObject();
//        testSet();
//        testArrayList();
//        testAddAllCollection();
//        testRetainAll();

    }
}
