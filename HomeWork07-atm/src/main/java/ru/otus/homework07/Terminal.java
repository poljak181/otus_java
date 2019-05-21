package ru.otus.homework07;

import ru.otus.homework07.ioservices.IOService;
import ru.otus.homework07.atm.Atm;
import ru.otus.homework07.atm.Banknote;

import java.util.HashMap;
import java.util.Map;

public class Terminal {
    private Atm atm = null;
    private IOService ioService;

    public Terminal(IOService ioService) {
        this.ioService = ioService;
    }

    public void startSession(Atm atm) {
        this.atm = atm;

        boolean exit = false;
        while (!exit) {
            printMenu();
            try {
                int code = Integer.valueOf(ioService.readln());

                switch (code) {
                    case 1:
                        if (!atm.putMoney(getBanknotesToPut())) {
                            ioService.writeln("Can't put this sum. Maybe atm is full. Try to put another sum");
                        }
                        break;
                    case 2:
                        final int sum = getSumOfMoney();
                        var returnedBanknotes = atm.getMoney(sum);
                        if (returnedBanknotes != null) {
                            ioService.writeln("Returned banknotes:");
                            for (var banknote : returnedBanknotes.entrySet()) {
                                ioService.writeln("Banknote: " + banknote.getKey() + ", count: " + banknote.getValue());
                            }
                        }
                        break;
                    case 3:
                        atm.fillMoneyCells();
                        break;
                    case 4:
                        atm.printBalance();
                        break;
                    case 0:
                        exit = true;
                    default:
                        break;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        this.atm = null;
    }

    private void printMenu() {
        ioService.writeln("--------------------------------");
        ioService.writeln("1 - put money");
        ioService.writeln("2 - get money");
        ioService.writeln("3 - fill with money");
        ioService.writeln("4 - print balance");
        ioService.writeln("0 - exit");
    }

    private int getSumOfMoney() {
        return Integer.valueOf(ioService.readln("Enter sum: "));
    }

    private void printBanknotes(Map<Integer, Banknote> map) {
        ioService.writeln("Select banknote: ");
        for (var banknote : map.entrySet()) {
            ioService.writeln(banknote.getKey() + " - " + banknote.getValue().name());
        }
        ioService.writeln("0 - Finish entering");
    }

    private int getBanknotesNumber() {
        return Integer.valueOf(ioService.readln("Enter number of selected banknote: "));
    }

    private void printEnteredBanknotes(Map<Banknote, Integer> map) {
        ioService.writeln("Banknotes entered: ");
        for (var banknoteEntry : map.entrySet()) {
            ioService.writeln("Banknote: " + banknoteEntry.getKey() + ", count: " + banknoteEntry.getValue());
        }
    }

    private Map<Banknote, Integer> getBanknotesToPut() {
        int banknoteIntKey = 0;
        Map<Integer, Banknote> map = new HashMap<>();
        for (var banknote : Banknote.getSortedByValue()) {
            map.put(++banknoteIntKey, banknote);
        }

        Map<Banknote, Integer> result = new HashMap<>();
        while (true) {
            printEnteredBanknotes(result);
            printBanknotes(map);

            final int code = Integer.valueOf(ioService.readln());
            if (code == 0) {
                break;
            } else {
                final Banknote selectedBanknote = map.get(code);
                if (selectedBanknote != null) {
                    final int number = getBanknotesNumber();
                    result.put(selectedBanknote, number);
                } else {
                    ioService.writeln("Invalid option selected");
                }
            }
        }

        return result;
    }
}
