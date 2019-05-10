package ru.otus.homework07.atm;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public enum Banknote {
    RUR_50(50),
    RUR_100(100),
    RUR_200(200),
    RUR_500(500),
    RUR_1000(1000),
    RUR_2000(2000),
    RUR_5000(5000);

    private int value;

    Banknote(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static List<Banknote> getSortedByValue() {
        var result = Arrays.asList(Banknote.values());
        Collections.sort(result, Comparator.comparing(Banknote::getValue));
        return result;
    }
}
