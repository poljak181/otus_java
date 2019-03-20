package ru.otus.homework04;

/*
 -Xms512m
 -Xmx512m

 -XX:+UseSerialGC
 -XX:+UseParallelGC
 -XX:+UseConcMarkSweepGC
 -XX:+UseG1GC
*/

public class Main {
    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            System.out.println("Invalid arguments. Stopping application...");
            System.out.println("Specify name of file to store statistic.");
            return;
        }

        System.out.println("Store statistic in " + args[0]);

        GcStatisticService statisticService = new GcStatisticService(args[0]);
        Thread thread = new Thread(statisticService);
        thread.start();

        MemoryConsumptionService memoryConsumptionService = new MemoryConsumptionService();

        try {
            memoryConsumptionService.run();
        } catch (OutOfMemoryError e) {
            statisticService.stop();
            statisticService.writeFullStatistic();
        }
    }
}

/*
Выводы.

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