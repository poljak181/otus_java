package ru.otus.homework031;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        final int size = 20;
        List<Integer> list = new MyArrayList<Integer>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        for (int i = 0; i < size; i++) {
            System.out.println(list.get(i));
        }
    }
}
