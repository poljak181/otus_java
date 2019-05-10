package ru.otus.homework07.atm;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BanknoteTest {

    @Test
    void testGetValue() {
        final var banknote = Banknote.RUR_500;
        assertEquals(500, banknote.getValue());
    }

    @Test
    void testGetSortedByValue() {
        final var banknotes = new Banknote[]{
                Banknote.RUR_50,
                Banknote.RUR_100,
                Banknote.RUR_200,
                Banknote.RUR_500,
                Banknote.RUR_1000,
                Banknote.RUR_2000,
                Banknote.RUR_5000,
        };

        assertArrayEquals(banknotes, Banknote.getSortedByValue().toArray());
    }
}