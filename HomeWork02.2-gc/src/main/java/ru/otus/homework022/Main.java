package ru.otus.homework022;

import java.lang.management.ManagementFactory;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting pid: " + ManagementFactory.getRuntimeMXBean().getName());

        final var measurer = new Measurer();
        measurer.prepare();

        measurer.measure(Object::new,"Object");
        measurer.clean();
    }
}
