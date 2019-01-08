package ru.otus.homework011;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class Main {
    public static void main(String[] args) {
        Multiset<Character> multiset = HashMultiset.create();

        for (int i = 0; i < args.length; i++) {
            for (int j = 0; j < args[i].length(); j++) {
                multiset.add(args[i].charAt(j));
            }
        }

        Character prevCh = null;
        for (Character ch : multiset) {
            if (prevCh != ch) {
                System.out.println(ch + " count:" + multiset.count(ch));
                prevCh = ch;
            }
        }
    }
}
