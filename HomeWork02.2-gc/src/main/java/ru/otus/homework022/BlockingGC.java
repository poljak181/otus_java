package ru.otus.homework022;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.ListenerNotFoundException;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class BlockingGC {
    private static final int GC_COUNT = 1;
    private final CountDownLatch doneSignal = new CountDownLatch(GC_COUNT);
    private List<Runnable> registration = new ArrayList<>();

    public BlockingGC() {
        installGCMonitoring();
    }

    static void collect() {
        BlockingGC blockingGC = new BlockingGC();
        try {
            System.gc();
            blockingGC.doneSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            blockingGC.registration.forEach(Runnable::run);
        }
    }

    private void installGCMonitoring() {
        final var gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        for (var gcBean : gcBeans) {
            System.out.println("GC name:" + gcBean.getName());

            var emitter = (NotificationEmitter)gcBean;

            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                        final var info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                        final long duration = info.getGcInfo().getDuration();
                        String gcType = info.getGcAction();

                        System.out.println(gcType + ": - "
                                + info.getGcInfo().getId() + ", "
                                + info.getGcName()
                                + " (from " + info.getGcCause() + ")" + duration + " milliseconds");

                    }

                    var info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());

                    if (info.getGcCause().equals("System.gc()")) {
                        doneSignal.countDown();
                    }

                    System.out.println("Thread name: " + Thread.currentThread().getName());
                }
            };

            emitter.addNotificationListener(listener, null, null);

            registration.add(() -> {
                try {
                    emitter.removeNotificationListener(listener);
                } catch (ListenerNotFoundException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
