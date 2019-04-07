package ru.otus.homework06;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework06.dataprovider.CarParametersFromDbGetter;
import ru.otus.homework06.dataprovider.CarParametersProvider;
import ru.otus.homework06.domain.DbCarParameters;

import java.util.Random;

// -Xms256m -Xmx256m
public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static class CarModelNameGenerator {
        private static final Random random = new Random();
        public static String getName() {
            return CarParametersFromDbGetter.MODEL_NAME + random.nextInt(DbCarParameters.CARMODEL_MAX_NUMBER);
        }
    }

    private static void performRequests(boolean useCache, int requestNumber) throws InterruptedException {
        LOG.info("Create provider...");
        LOG.info("Using cache: {}", useCache ? "Yes" : "No");
        final CarParametersProvider provider = new CarParametersProvider(useCache);

        long startCountTime = System.currentTimeMillis();
        LOG.info("Start performing of {} requests...", requestNumber);
        for (int i = 0; i < requestNumber; i++) {
            Thread.sleep(30);
            final var parameters = provider.getCarParameters(CarModelNameGenerator.getName());
        }
        LOG.info("Spent time: {}s", (System.currentTimeMillis() - startCountTime)/1000.);
        provider.dispose();
    }

    public static void main(String[] args) throws InterruptedException {
        LOG.info("Starting program...");
        LOG.trace("Main thread id: {}", Thread.currentThread().getId());

        final int requestsNumber = 500;
        performRequests(false, requestsNumber);
        performRequests(true, requestsNumber);
    }
}
