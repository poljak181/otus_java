package ru.otus.homework06.dataprovider;

import ru.otus.homework06.cache.api.CacheEngine;
import ru.otus.homework06.cache.core.CacheEngineImpl;
import ru.otus.homework06.domain.CarParameters;

public class CarParametersProvider {
    CacheEngine<String, CarParameters> cacheEngine = null;
    boolean usingCache;

    public CarParametersProvider(boolean useCache) {
        this.usingCache = useCache;
        if (useCache) {
            cacheEngine = new CacheEngineImpl<>(0, 0, true);
        }
    }

    public boolean isUsingCache() {
        return usingCache;
    }

    private CarParameters createCarParameters(String modelName) {
        var carParametersFromDb = CarParametersFromDbGetter.getCarParametersByModelName(modelName);
        if (carParametersFromDb != null) {
            return new CarParameters(modelName, carParametersFromDb.getParameters());
        } else {
            return null;
        }
    }

    public CarParameters getCarParameters(String modelName) {
        if (usingCache) {
            var cachedCarParameters = cacheEngine.get(modelName);
            if (cachedCarParameters == null) {
                final var newCarParameters = createCarParameters(modelName);
                cacheEngine.put(modelName, newCarParameters);
                return newCarParameters;
            } else {
                return cachedCarParameters;
            }
        } else {
            return createCarParameters(modelName);
        }
    }

    public void dispose() {
        if (usingCache) {
            cacheEngine.dispose();
        }
    }
}
