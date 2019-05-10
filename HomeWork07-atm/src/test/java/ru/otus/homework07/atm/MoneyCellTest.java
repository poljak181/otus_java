package ru.otus.homework07.atm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoneyCellTest {
    private MoneyCell moneyCell = null;

    @BeforeEach
    void init() {
        moneyCell = new MoneyCell(Banknote.RUR_50, 10);
    }

    @AfterEach
    void clear() {
        moneyCell = null;
    }

    @Test
    void testPut() {
        assertEquals(0, moneyCell.put(11));
        assertEquals(4, moneyCell.put(4));
        assertEquals(4, moneyCell.getBanknoteCount());
    }

    @Test
    void testTryGet() {
        assertEquals(0, moneyCell.tryGet(4));
        moneyCell.put(3);
        assertEquals(0, moneyCell.tryGet(4));
        assertEquals(3, moneyCell.tryGet(3));
        assertEquals(0, moneyCell.getBanknoteCount());
    }

    @Test
    void testGetBanknoteCount() {
        assertEquals(0, moneyCell.getBanknoteCount());
        moneyCell.put(4);
        assertEquals(4, moneyCell.getBanknoteCount());
    }

    @Test
    void testGetBalance() {
        assertEquals(0, moneyCell.getBalance());
        moneyCell.put(5);
        assertEquals(250, moneyCell.getBalance());
    }

    @Test
    void testGetBanknoteMaxCount() {
        assertEquals(10, moneyCell.getBanknoteMaxCount());
    }

    @Test
    void testClear() {
        moneyCell.put(4);
        moneyCell.put(2);
        moneyCell.clear();
        assertEquals(0, moneyCell.getBanknoteCount());
        assertEquals(10, moneyCell.getBanknoteMaxCount());
        assertEquals(0, moneyCell.getBalance());
    }
}