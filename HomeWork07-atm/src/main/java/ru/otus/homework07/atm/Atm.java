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
            if (moneyCell != null) {
                moneyCell.clear();
            }
        }
    }

    private Map<Banknote, Integer> takeBanknotesFromMoneyCells(int requestedSum) {
        Map<Banknote, Integer> banknotesToReturn = new LinkedHashMap<>(); // store order of adding

        final var sortedByValue = Banknote.getSortedByValue();
        final int size = sortedByValue.size();
        int startIndex = sortedByValue.size() - 1;
        for (int i = 0; i < size; ++i) {
            int restOfRequestedSum = requestedSum;
            for (int j = startIndex; j >= 0; --j) {
                final var banknote = sortedByValue.get(j);
                final var moneyCell = getMoneyCell(banknote);

                if (moneyCell == null || moneyCell.getBanknoteCount() == 0 || banknote.getValue() > requestedSum) {
                    continue;
                }

                final int availableBanknotesCount = moneyCell.getAvailableBanknotesCount(restOfRequestedSum);

                if (availableBanknotesCount > 0) {
                    restOfRequestedSum -= availableBanknotesCount * banknote.getValue();
                    banknotesToReturn.put(banknote, availableBanknotesCount);
                }

                if (restOfRequestedSum == 0) {
                    for (var banknoteToReturn : banknotesToReturn.entrySet()) {
                        tryGet(banknoteToReturn.getKey(), banknoteToReturn.getValue());
                    }
                    return banknotesToReturn;
                }
            }
            --startIndex;
            banknotesToReturn.clear();
        }
        return null;
    }

    public Map<Banknote, Integer> getMoney(int requestedSum) {
        final int balance = getBalance();
        if (requestedSum > balance) {
            System.out.println("Not enough money in ATM. Request: " + requestedSum + ", existing sum: " + balance);
            LOG.trace("Not enough money in ATM. Request: {}, existing sum: {}", requestedSum, balance);
            return null;
        }

        final var minValue = Banknote.getMinimalBanknote().getValue();
        if (requestedSum % minValue != 0) {
            System.out.println("Requested sum of money must be multiple of " + minValue);
            LOG.trace("Requested sum of money must be multiple of {}", minValue);
            return null;
        }

        var takenBanknotes = takeBanknotesFromMoneyCells(requestedSum);
        if (takenBanknotes == null) {
            System.out.println("This sum: " + requestedSum + " can't be issued");
            LOG.trace("This sum: {} can't be issued", requestedSum);
        }

        return takenBanknotes;
    }

    private boolean canPlaceAllBanknotes(Map<Banknote, Integer> banknotes) {
        for (var banknoteToPut : banknotes.entrySet()) {
            MoneyCell moneyCell = getMoneyCell(banknoteToPut.getKey());
            if (moneyCell == null
                    || moneyCell.getFreeSpace() < banknoteToPut.getValue()) {
                return false;
            }
        }

        return true;
    }

    public boolean putMoney(Map<Banknote, Integer> banknotes) {
        if (banknotes.isEmpty()) {
            LOG.trace("Nothing to put");
            return false;
        }

        if (!canPlaceAllBanknotes(banknotes)) {
            LOG.trace("Can't place all banknotes");
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
        return moneyCellHashMap.get(banknote);
    }

    private int put(Banknote banknote, int count) {
        MoneyCell moneyCell = getMoneyCell(banknote);
        return moneyCell != null ? moneyCell.put(count) : 0;
    }

    private int tryGet(Banknote banknote, int count) {
        MoneyCell moneyCell = getMoneyCell(banknote);
        return moneyCell != null ? moneyCell.tryGet(count) : 0;
    }
}
