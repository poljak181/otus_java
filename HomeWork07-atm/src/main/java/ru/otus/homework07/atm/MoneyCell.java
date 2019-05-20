package ru.otus.homework07.atm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoneyCell {
    private static final Logger LOG = LoggerFactory.getLogger(MoneyCell.class);

    private int banknoteCount = 0;
    final private Banknote banknote;
    final private int banknoteMaxCount;

    MoneyCell(Banknote banknote, int banknoteMaxCount) {
        this.banknote = banknote;
        this.banknoteMaxCount = banknoteMaxCount;
    }

    public int put(int count) {
        if (banknoteCount + count > banknoteMaxCount) {
            LOG.warn("Money cell can't fit {} banknotes. Current number: {}, max number: {}"
                    , count, banknoteCount, banknoteMaxCount);
            return 0;
        }
        banknoteCount += count;
        LOG.trace("Put {} of {} in money cell", count, banknote.name());
        return count;
    }

    public int tryGet(int count) {
        if (banknoteCount < count) {
            LOG.trace("MoneyCell with {} has only {} banknotes but {} was requested", banknote.name(), banknoteCount, count);
            return 0;
        }

        banknoteCount -= count;
        LOG.trace("Get {} of {} in money cell", count, banknote.name());
        return count;
    }

    public int getBanknoteCount() {
        return banknoteCount;
    }

    public int getBalance() {
        return banknoteCount* banknote.getValue();
    }

    public int getBanknoteMaxCount() {
        return banknoteMaxCount;
    }

    public void clear() {
        banknoteCount = 0;
    }

    public int getFreeSpace() {
        return banknoteMaxCount - banknoteCount;
    }

    public int getAvailableBanknotesCount(int requestedSum) {
        final int needBanknotes = requestedSum / banknote.getValue();
        return needBanknotes <= banknoteCount ? needBanknotes : banknoteCount;
    }
}
