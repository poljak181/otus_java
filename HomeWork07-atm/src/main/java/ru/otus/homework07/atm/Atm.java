package ru.otus.homework07.atm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Atm {
    private static final Logger LOG = LoggerFactory.getLogger(Atm.class);
    private HashMap<Banknote, MoneyCell> moneyCellHashMap = new LinkedHashMap<>();
    public static final int BANKNOTE_MAX_COUNT = 100;

    // TODO: banknoteValue and maxCount as constructor arguments
    public Atm() {
        for (var banknoteValue : Banknote.getSortedByValue()) {
            moneyCellHashMap.put(banknoteValue, new MoneyCell(banknoteValue, BANKNOTE_MAX_COUNT));
        }
    }

    public void fillMoneyCells() {
        for (var moneyCell : moneyCellHashMap.values()) {
            final int banknotesToPut = moneyCell.getBanknoteMaxCount() - moneyCell.getBanknoteCount();
            moneyCell.put(banknotesToPut);
        }
    }

    public void clearMoneyCells() {
        for (var banknoteValue : Banknote.values()) {
            var moneyCell = getMoneyCell(banknoteValue);
            moneyCell.clear();
        }
    }

    public Map<Banknote, Integer> getMoney(int requestedSum) {
        final int balance = getBalance();
        if (requestedSum > balance) {
            System.out.println("Not enough money in ATM. Request: " + requestedSum + ", existing sum: " + balance);
            LOG.trace("Not enough money in ATM. Request: {}, existing sum: {}", requestedSum, balance);
            return null;
        }

        final var sortedByValue = Banknote.getSortedByValue();
        assert (sortedByValue.size() > 0);

        final var minValue = sortedByValue.get(0).getValue();
        if (requestedSum % minValue != 0) {
            System.out.println("Requested sum of money must be multiple of " + minValue);
            LOG.trace("Requested sum of money must be multiple of {}", minValue);
            return null;
        }

        int restOfRequestedSum = requestedSum;
        Map<Banknote, Integer> banknotesToReturn = new LinkedHashMap<>(); // store order of adding

        for (int i = sortedByValue.size() - 1; i >= 0; i--) {
            final var banknote = sortedByValue.get(i);
            final int currentValue = banknote.getValue();
            final var moneyCell = getMoneyCell(banknote);
            final int banknotesInCell = moneyCell.getBanknoteCount();

            final int needBanknotes = restOfRequestedSum / currentValue;
            final int banknotesToAddInReturningResult = needBanknotes <= banknotesInCell ? needBanknotes : banknotesInCell;
            if (banknotesToAddInReturningResult <= 0) {
                continue;
            }

            restOfRequestedSum -= banknotesToAddInReturningResult * currentValue;
            banknotesToReturn.put(banknote, banknotesToAddInReturningResult);

            if (restOfRequestedSum == 0) {
                for (var banknoteToReturn : banknotesToReturn.entrySet()) {
                    tryGet(banknoteToReturn.getKey(), banknoteToReturn.getValue());
                }
                return banknotesToReturn;
            }
        }

        System.out.println("This sum: " + requestedSum + " can't be issued");
        LOG.trace("This sum: {} can't be issued", requestedSum);
        return null;
    }

    public boolean putMoney(Map<Banknote, Integer> banknotes) {
        if (banknotes.isEmpty()) {
            LOG.trace("Nothing to put");
            return false;
        }

        for (var banknoteToPut : banknotes.entrySet()) {
            final int banknoteCount = banknoteToPut.getValue();
            if (banknoteCount > 0) {
                if (put(banknoteToPut.getKey(), banknoteCount) == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getBalance() {
        return moneyCellHashMap.values().stream().mapToInt(i -> i.getBalance()).sum();
    }

    public void printBalance() {
        System.out.println("        ATM balance:");
        int sum = 0;
        for (var entry : moneyCellHashMap.entrySet()) {
            System.out.println(entry.getKey().name() + ": " + entry.getValue().getBanknoteCount());
            sum += entry.getValue().getBanknoteCount()*entry.getKey().getValue();
        }
        System.out.println("        Full sum: " + sum);
        LOG.trace("balance requested: {}", sum);
    }

    private MoneyCell getMoneyCell(Banknote banknote) {
        var moneyCell = moneyCellHashMap.get(banknote);
        if (moneyCell == null) {
            throw new IllegalArgumentException("Not supported banknote value: " + banknote.name());
        }
        return moneyCell;
    }

    private int put(Banknote banknote, int count) {
        MoneyCell moneyCell = getMoneyCell(banknote);
        return moneyCell.put(count);
    }

    private int tryGet(Banknote banknote, int count) {
        MoneyCell moneyCell = getMoneyCell(banknote);
        return moneyCell.tryGet(count);
    }
}
