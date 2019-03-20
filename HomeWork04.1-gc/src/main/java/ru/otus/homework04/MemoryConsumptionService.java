package ru.otus.homework04;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryConsumptionService {
    final private static long TIME_TO_SLEEP_IN_MSECS = 250;
    final private static int COUNT_TO_ADD = 110_000;
    final private static int COUNT_TO_REMOVE = COUNT_TO_ADD/2;

    private List<Double> list = new ArrayList<>();

    public void run() throws InterruptedException {
        System.out.println("Starting allocate memory...");
        while (true) {
            list.addAll(Collections.nCopies(COUNT_TO_ADD, Double.valueOf(1.1)));
            list.subList(list.size() - 1 - COUNT_TO_REMOVE, list.size() - 1).clear();

            Thread.sleep(TIME_TO_SLEEP_IN_MSECS);
        }
    }
}
