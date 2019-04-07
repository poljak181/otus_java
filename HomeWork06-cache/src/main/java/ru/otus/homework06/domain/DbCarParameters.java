package ru.otus.homework06.domain;

public class DbCarParameters {
    final public static int CARMODEL_MAX_NUMBER = 70;
    private String modelName;
    private byte[] parameters = new byte[1024*1024];

    public DbCarParameters(String modelName) {
        this.modelName = modelName;
    }

    public String getModelName() {
        return modelName;
    }

    public byte[] getParameters() {
        return parameters;
    }
}
