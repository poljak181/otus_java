package ru.otus.homework04;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 -Xms512m
 -Xmx512m

 -XX:+UseSerialGC
 -XX:+UseParallelGC
 -XX:+UseConcMarkSweepGC
 -XX:+UseG1GC
*/

public class Main {
    final private static long MSECS_IN_MINUTE = 60_000;
    final private static long TIME_TO_SLEEP_IN_MSECS = 250;
    final private static int COUNT_TO_ADD = 110_000;
    final private static int COUNT_TO_REMOVE = COUNT_TO_ADD/2;

    static class Stats {
        public Stats() {
            this.counter = 0;
            this.duration = 0;
        }

        public int counter;
        public long duration;
    }

    private static long startTime = 0;
    private static long timeToPrintStat = 0;
    private static HashMap<String, Stats> stats = new HashMap<>();
    private static HashMap<String, Stats> fullStat = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Installing listeners to GC events...");
        installGCMonitoring();
        System.out.println("Starting allocate memory...");

        startTime = System.currentTimeMillis();
        timeToPrintStat = startTime + MSECS_IN_MINUTE;
        System.out.println("Start allocating at " + getTime(startTime, "HH:mm:ss"));

        final var list = new ArrayList<Double>();
        try {
            while (true) {
                list.addAll(Collections.nCopies(COUNT_TO_ADD, Double.valueOf(1.1)));
                list.subList(list.size() - 1 - COUNT_TO_REMOVE, list.size() - 1).clear();

                final var curTime = System.currentTimeMillis();
                if (curTime > timeToPrintStat) {
                    System.out.println("Current time: " + getTime(curTime, "HH:mm:ss"));
                    printStat(stats);

                    timeToPrintStat += MSECS_IN_MINUTE;
                    for (var name : stats.keySet()) {
                        var stat = stats.get(name);
                        stat.counter = 0;
                        stat.duration = 0;
                    }
                }

                Thread.sleep(TIME_TO_SLEEP_IN_MSECS);
            }
        } catch (OutOfMemoryError e) {
            final var curTime = System.currentTimeMillis();
            System.out.println("All working time: " + getTime(curTime - startTime, "mm:ss"));
            printStat(fullStat);
        }
    }

    private static String getTime(long time, String timeFormat) {
        final SimpleDateFormat format = new SimpleDateFormat(timeFormat);
        return format.format(new Date(time));
    }

    private static void printStat(HashMap<String, Stats> statistic) {
        System.out.println("==========================================================================================");
        for (var name : statistic.keySet()) {
            var stat = statistic.get(name);
            System.out.println("name: " + name + ", count: " + stat.counter + ", duration: " + stat.duration + " msecs");
        }
        System.out.println("==========================================================================================");
    }

    private static void addToStat(HashMap<String, Stats> statistic, String name, long duration) {
        var stat = statistic.get(name);
        if (stat != null) {
            stat.counter++;
            stat.duration += duration;
        }
    }

    private static void installGCMonitoring() {
        final var gcbeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            final String name = gcbean.getName();
            System.out.println("GCBean name: " + name);

            stats.put(name, new Stats());
            fullStat.put(name, new Stats());

            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    var info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

                    final long duration = info.getGcInfo().getDuration();
                    final String gcType = info.getGcAction();
                    final var gcId = info.getGcInfo().getId();
                    final var gcName = info.getGcName();
                    final var gcCause = info.getGcCause();
                    final var start = info.getGcInfo().getStartTime();
                    final var end = info.getGcInfo().getEndTime();
                    final var memoryBefore = info.getGcInfo().getMemoryUsageBeforeGc();
                    final var memoryAfter = info.getGcInfo().getMemoryUsageAfterGc();

                    final var curTime = System.currentTimeMillis();
                    System.out.println("--------------------------Current time: "
                            + getTime(curTime, "HH:mm:ss")
                            + "---------------------------");

                    System.out.println("gcType: " + gcType + "\n"
                            + " gcName: "  + gcName + "\n"
                            + " gcId: " + gcId + "\n"
                            + " cause: " + gcCause + "\n"
                            + " start: " + start + "\n"
                            + " end: " + end + "\n"
                            + " memory before: " + memoryBefore + "\n"
                            + " memory after: " + memoryAfter + "\n"
                            + "duration: " + duration + " milliseconds");

                    addToStat(stats, gcName, duration);
                    addToStat(fullStat, gcName, duration);
                }
            };

            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            emitter.addNotificationListener(listener, null, null);
        }
    }
}

/*
Serial
полное время работы: 03:40
по минутам
==========================================================================================
name: Copy,             count: 2, duration: 90 msecs
name: MarkSweepCompact, count: 0, duration: 0 msecs
==========================================================================================
name: Copy,             count: 3, duration: 678 msecs
name: MarkSweepCompact, count: 0, duration: 0 msecs
==========================================================================================
name: Copy,             count: 1, duration: 0 msecs
name: MarkSweepCompact, count: 1, duration: 134 msecs
==========================================================================================

После OutOfMemory
==========================================================================================
name: Copy,             count: 7, duration: 768 msecs
name: MarkSweepCompact, count: 3, duration: 542 msecs
==========================================================================================

Выводы: Запускается редко, видимо потому, что область Eden достаточно велика, а сборка мусора начинается только
когда в Eden закончилась свободная память.
Время, которое занимает сборка мусора (minor GC) невелико, т.к. мал размер кучи.
Перед тем как упасть с исключением была попытка запустить (major GC). Но, т.к. в old generation
находятся исключительно живые объекты major GC памяти не освободила и приложение завершилось


Parallel
полное время работы: 03:41
по минутам
==========================================================================================
name: PS MarkSweep, count: 0, duration: 0 msecs
name: PS Scavenge,  count: 2, duration: 95 msecs
==========================================================================================
name: PS MarkSweep, count: 0, duration: 0 msecs
name: PS Scavenge,  count: 1, duration: 325 msecs
==========================================================================================
name: PS MarkSweep, count: 1, duration: 257 msecs
name: PS Scavenge,  count: 2, duration: 937 msecs
==========================================================================================

После OutOfMemory
==========================================================================================
name: PS MarkSweep, count: 3, duration: 875 msecs
name: PS Scavenge,  count: 6, duration: 1358 msecs
==========================================================================================

Выводы: На данном примере ведёт себя практически так же как и SerialGC.
Увидеть преимущества параллельного переноса из young gen в old gen или же параллельного уплотнения
в old generation не удалось. Так же на данном примере не удалось заметить проблем с фрагментацией
кучи.


ConcMarkSweep
полное время работы: 03:40
по минутам
==========================================================================================
name: ParNew,              count: 2, duration: 125 msecs
name: ConcurrentMarkSweep, count: 0, duration: 0 msecs
==========================================================================================
name: ParNew,              count: 3, duration: 495 msecs
name: ConcurrentMarkSweep, count: 3, duration: 13772 msecs
==========================================================================================
name: ParNew,              count: 1, duration: 89 msecs
name: ConcurrentMarkSweep, count: 9, duration: 41734 msecs
==========================================================================================

После OutOfMemory
==========================================================================================
name: ParNew,              count: 7, duration: 770 msecs
name: ConcurrentMarkSweep, count: 19, duration: 87100 msecs
==========================================================================================

Выводы: Данный GC проводит очень много времени в ConcurrentMarkSweep (major GC), но это не сильно
сказывается на работе программы, т.к работа большей частью должна выполняться параллельно.
Удалось выяснить, что запуск major GC происходит не дожидаясь заполнения old generation.

G1
полное время работы: 05:35
по минутам
==========================================================================================
name: G1 Young Generation, count: 5, duration: 386 msecs
name: G1 Old Generation,   count: 0, duration: 0 msecs
==========================================================================================
name: G1 Young Generation, count: 6, duration: 1069 msecs
name: G1 Old Generation,   count: 0, duration: 0 msecs
==========================================================================================
name: G1 Young Generation, count: 7, duration: 1385 msecs
name: G1 Old Generation,   count: 1, duration: 58 msecs
==========================================================================================
name: G1 Young Generation, count: 6, duration: 610 msecs
name: G1 Old Generation,   count: 0, duration: 0 msecs
==========================================================================================
name: G1 Young Generation, count: 5, duration: 819 msecs
name: G1 Old Generation,   count: 0, duration: 0 msecs
==========================================================================================

После OutOfMemory
==========================================================================================
name: G1 Young Generation, count: 34, duration: 5321 msecs
name: G1 Old Generation,   count: 3,  duration: 296 msecs
==========================================================================================

Выводы: Данный GC часто запускает сборку мусора для Young Generation, но выполняется она быстро,
т.к. области для young generation должны быть небольшого размера.
Вообще, получилось, что из за особенного подхода (отличного от других сборщиков мусора) к делению
памяти на регионы программа дольше всего проработала именно с данным сборщиком мусора, прежде
чем упасть с OutOfMemory исключением.
Получается, что для данной программы и размере кучи 512m этот сборщик мусора оказался оптимальным с точки зрения
расхода и управления памятью

*/