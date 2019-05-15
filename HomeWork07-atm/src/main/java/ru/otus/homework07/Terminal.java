package ru.otus.homework07;

import ru.otus.homework07.atm.Atm;
import ru.otus.homework07.atm.Banknote;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Terminal {
    private Atm atm = null;
    private Scanner scanner = null;

    public Terminal() {
        scanner = new Scanner(System.in);
    }

    public void startSession(Atm atm) {
        this.atm = atm;
        printMenu();

        while (scanner.hasNext()) {

            try {
                int code = Integer.valueOf(scanner.nextLine());

                switch (code) {
                    case 1:
                        if (!atm.putMoney(getBanknotesToPut())) {
                            System.out.println("Can't put this sum. Maybe atm is full. Try to put another sum");
                        }
                        break;
                    case 2:
                        final int sum = getSumOfMoney();
                        var returnedBanknotes = atm.getMoney(sum);
                        if (returnedBanknotes != null) {
                            System.out.println("Returned banknotes:");
                            for (var banknote : returnedBanknotes.entrySet()) {
                                System.out.println("Banknote: " + banknote.getKey() + ", count: " + banknote.getValue());
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
                        return;
                    default:
                        break;
                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            printMenu();
        }

        this.atm = null;
    }

    private void printMenu() {
        System.out.println("--------------------------------");
        System.out.println("1 - put money");
        System.out.println("2 - get money");
        System.out.println("3 - fill with money");
        System.out.println("4 - print balance");
        System.out.println("0 - exit");
    }

    private int getSumOfMoney() {
        System.out.println("Enter sum:");
        return Integer.valueOf(scanner.nextLine());
    }

    private void printBanknotes(Map<Integer, Banknote> map) {
        System.out.println("Select banknote: ");
        for (var banknote : map.entrySet()) {
            System.out.println(banknote.getKey() + " - " + banknote.getValue().name());
        }
        System.out.println("0 - Finish entering");
    }

    private int getBanknotesNumber() {
        System.out.println("Enter number of selected banknote:");
        return Integer.valueOf(scanner.nextLine());
    }

    private void printEneteredBanknotes(Map<Banknote, Integer> map) {
        System.out.println("Banknotes entered: ");
        for (var banknoteEntry : map.entrySet()) {
            System.out.println("Banknote: " + banknoteEntry.getKey() + ", count: " + banknoteEntry.getValue());
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
            printEneteredBanknotes(result);
            printBanknotes(map);

            final int code = Integer.valueOf(scanner.nextLine());
            if (code == 0) {
                break;
            } else {
                final Banknote selectedBanknote = map.get(code);
                if (selectedBanknote != null) {
                    final int number = getBanknotesNumber();
                    result.put(selectedBanknote, number);
                } else {
                    System.out.println("Invalid option selected");
                }
            }
        }

        return result;
    }
}
