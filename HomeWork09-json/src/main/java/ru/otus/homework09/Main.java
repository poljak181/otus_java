package ru.otus.homework09;

import javax.json.Json;
import javax.json.JsonWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

// ДЗ-09: JSON object writer
// Напишите свой json object writer (object to JSON string)
// аналогичный gson на основе javax.json или simple-json и Reflection.
// Поддержите массивы объектов и примитивных типов, и коллекции из стандартный библиотерки.

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        var objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("event", "firework");
        objectBuilder.add("start", "27.05.2019 00:00");

        var object = objectBuilder.build();

        OutputStream outputStream = new FileOutputStream("javax.json");
        try (JsonWriter writer = Json.createWriter(outputStream)) {
            writer.writeObject(object);
        }
    }
}
