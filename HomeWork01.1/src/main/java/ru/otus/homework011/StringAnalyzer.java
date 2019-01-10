package ru.otus.homework011;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

public class StringAnalyzer {
    public static void printCharacterOccurrence(String string) {
        Multiset<Character> multiset = HashMultiset.create();
        for (int i = 0; i < string.length(); i++) {
            multiset.add(string.charAt(i));
        }
        Character prevCh = null;
        for (Character ch : multiset) {
            if (prevCh != ch) {
                System.out.println("The number of occurrences of '" + ch + "': " + multiset.count(ch));
                prevCh = ch;
            }
        }
    }
}
