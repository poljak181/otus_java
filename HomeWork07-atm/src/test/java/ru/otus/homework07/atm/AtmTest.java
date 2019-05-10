package ru.otus.homework07.atm;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class AtmTest {
    private Atm atm = null;

    @BeforeEach
    void init() {
        atm = new Atm();
    }

    @AfterEach
    void clear() {
        atm = null;
    }

    @Test
    void testFillMoneyCells() {
        assertEquals(0, atm.getBalance());
        atm.fillMoneyCells();
        final int maxBalance = Arrays.stream(Banknote.values()).mapToInt(i -> i.getValue()).sum()*Atm.BANKNOTE_MAX_COUNT;
        assertEquals(maxBalance, atm.getBalance());
    }

    @Test
    void testClearMoneyCells() {
        assertEquals(0, atm.getBalance());
        atm.fillMoneyCells();
        final int maxBalance = Arrays.stream(Banknote.values()).mapToInt(i -> i.getValue()).sum()*Atm.BANKNOTE_MAX_COUNT;
        assertEquals(maxBalance, atm.getBalance());
        atm.clearMoneyCells();
        assertEquals(0, atm.getBalance());
    }

    @Test
    void testGetMoney() {
        assertEquals(null, atm.getMoney(100));
        atm.fillMoneyCells();
        assertEquals(null, atm.getMoney(130));

        var result1 = atm.getMoney(500_500);
        assertEquals(100, result1.get(Banknote.RUR_5000));
        assertEquals(1, result1.get(Banknote.RUR_500));

        var result2 = atm.getMoney(5000);
        assertEquals(2, result2.get(Banknote.RUR_2000));
        assertEquals(1, result2.get(Banknote.RUR_1000));
    }

    @Test
    void testPutMoney() {
        var moneyPack = new HashMap<Banknote, Integer>();
        moneyPack.put(Banknote.RUR_1000, 3);
        moneyPack.put(Banknote.RUR_100, 3);
        moneyPack.put(Banknote.RUR_50, 3);
        atm.putMoney(moneyPack);

        assertEquals(3450, atm.getBalance());

        var result = atm.getMoney(3450);

        assertEquals(3, result.get(Banknote.RUR_1000));
        assertEquals(3, result.get(Banknote.RUR_100));
        assertEquals(3, result.get(Banknote.RUR_50));
    }

    @Test
    void testGetBalance() {
        Banknote[] banknotes = {Banknote.RUR_5000, Banknote.RUR_2000, Banknote.RUR_1000, Banknote.RUR_500, Banknote.RUR_200, Banknote.RUR_100, Banknote.RUR_50};
        var moneyPack = new HashMap<Banknote, Integer>();
        for (var banknote : banknotes) {
            moneyPack.put(banknote, 1);
        }

        atm.putMoney(moneyPack);
        final int balance = Arrays.stream(banknotes).mapToInt(i -> i.getValue()).sum();
        assertEquals(balance, atm.getBalance());
    }
}