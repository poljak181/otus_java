package ru.otus.homework06.domain;

import java.util.Arrays;

public class CarParameters {
    private String modelName;
    private byte[] parameters;

    public CarParameters(String modelName, byte[] bytes) {
        this.modelName = modelName;
        parameters = Arrays.copyOf(bytes, bytes.length);
    }

    public String getModelName() {
        return modelName;
    }

    public byte[] getParameters() {
        return parameters;
    }
}
