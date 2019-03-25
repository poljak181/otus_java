package ru.otus.homework04;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class GcStatisticService implements Runnable {
    final private static long MSECS_IN_MINUTE = 60_000;

    private class Stats {
        public Stats() {
            this.counter = 0;
            this.duration = 0;
        }

        public int counter;
        public long duration;
    }

    private long startTime = 0;
    private long timeToPrintStat = 0;
    private ConcurrentHashMap<String, Stats> periodicStatistics = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Stats> fullStatistics = new ConcurrentHashMap<>();
    private String fileToStoreStatistic;
    private AtomicBoolean exit = new AtomicBoolean(true);

    GcStatisticService(String fileToStoreStatistic) {
        this.fileToStoreStatistic = fileToStoreStatistic;
        try (FileWriter writer = new FileWriter(fileToStoreStatistic)) {
            writer.write("GC statistic. Start at " + getTime(System.currentTimeMillis(), "HH:mm:ss") +" \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        installGcMonitoring();

        startTime = System.currentTimeMillis();
        timeToPrintStat = startTime + MSECS_IN_MINUTE;

        while (!exit.get()) {
            final var curTime = System.currentTimeMillis();
            if (curTime > timeToPrintStat) {
                writeStatistic("Statistic for last minute", periodicStatistics);

                timeToPrintStat += MSECS_IN_MINUTE;
                for (var name : periodicStatistics.keySet()) {
                    var stat = periodicStatistics.get(name);
                    stat.counter = 0;
                    stat.duration = 0;
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void writeFullStatistic() {
        final var curTime = System.currentTimeMillis();
        final String header = "All working time: " + getTime(curTime - startTime, "mm:ss");
        writeStatistic(header, fullStatistics);
    }

    public void stop() {
        exit.set(true);
    }

    private String getTime(long time, String timeFormat) {
        final SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        return format.format(new Date(time));
    }

    synchronized private void writeToFile(String header, String text) {
        try (FileWriter writer = new FileWriter(fileToStoreStatistic, true)) {
            writer.write(header + "\n" + text);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeStatistic(String header, ConcurrentHashMap<String, Stats> statistic) {
        StringBuilder builder = new StringBuilder();
        builder.append("==========================================================================================\n");
        builder.append("Current time: " + getTime(System.currentTimeMillis(), "HH:mm:ss") + "\n");
        for (var name : statistic.keySet()) {
            var stat = statistic.get(name);
            builder.append("name: " + name + ", count: " + stat.counter + ", duration: " + stat.duration + " msecs\n");
        }
        builder.append("==========================================================================================\n");
        writeToFile(header, builder.toString());
    }

    private void addInfoToStatistic(ConcurrentHashMap<String, Stats> statistic, String name, long duration) {
        var stat = statistic.get(name);
        if (stat != null) {
            stat.counter++;
            stat.duration += duration;
        }
    }

    private void installGcMonitoring() {
        System.out.println("Installing listeners for GC events...");

        final var gcbeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            final String name = gcbean.getName();
            System.out.println("Listener for GCBean " + name + " installed");

            periodicStatistics.put(name, new Stats());
            fullStatistics.put(name, new Stats());

            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    final var info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    final var gcName = info.getGcName();
                    final var duration = info.getGcInfo().getDuration();

                    StringBuilder builder = new StringBuilder();
                    builder.append("gcType: " + info.getGcAction() + "\n"
                            + "gcName: "  + gcName + "\n"
                            + "gcId: " + info.getGcInfo().getId() + "\n"
                            + "cause: " + info.getGcCause() + "\n"
                            + "memory before: " + info.getGcInfo().getMemoryUsageBeforeGc() + "\n"
                            + "memory after: " + info.getGcInfo().getMemoryUsageAfterGc() + "\n"
                            + "duration: " + duration + " milliseconds\n\n");

                    final String header = "--------------Current time: "
                            + getTime(System.currentTimeMillis(), "HH:mm:ss")
                            + "---------------";
                    writeToFile(header, builder.toString());

                    addInfoToStatistic(periodicStatistics, gcName, duration);
                    addInfoToStatistic(fullStatistics, gcName, duration);
                }
            };

            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            emitter.addNotificationListener(listener, null, null);
        }
    }
}
