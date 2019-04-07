package ru.otus.homework06.dataprovider;

import ru.otus.homework06.domain.DbCarParameters;

import java.util.HashMap;

public class CarParametersFromDbGetter {
    public static final String MODEL_NAME = "ModelName_";

    static private HashMap<String, DbCarParameters> carParameters = new HashMap<>();

    static {
        for (int i = 0; i < DbCarParameters.CARMODEL_MAX_NUMBER; i++) {
            final String modelName = MODEL_NAME + i;
            carParameters.put(modelName, new DbCarParameters(modelName));
        }
    }

    static DbCarParameters getCarParametersByModelName(String modelName) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return carParameters.get(modelName);
    }
}
