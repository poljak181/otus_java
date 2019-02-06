package ru.otus.homework031;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static <T> List<T> prepareList(T... t) {
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

    public static void main(String[] args) {

        testAdd();
        testAddIndex();

//        final var list1 = prepareList(1, 2, 3, 4, 5, 6, null);
//        System.out.println("List size: " + list1.size());
//        System.out.println("List contains null: " + list1.contains(null));
//        System.out.println("List contains 3: " + list1.contains(3));
//        System.out.println("List contains 3L: " + list1.contains(3L));

//        var list = new ArrayList<Integer>();

//        Long i = Long.valueOf(10L);
//        System.out.println("Equals: " + i.equals(null));

//        final int size = 20;
//        List<Integer> list = new MyArrayList<Integer>();
//        for (int i = 0; i < size; i++) {
//            list.add(i);
//        }
//
//        list.remove(7);
//        list.remove(0);
//        list.remove(20);
//
//        list.remove(Integer.valueOf(2));
//
//        for (int i = 0; i < list.size(); i++) {
//            System.out.println(list.get(i));
//        }
    }
}
