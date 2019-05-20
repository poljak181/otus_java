package ru.otus.homework07.ioservices;

import java.util.Scanner;

public class ConsoleIOService implements IOService {
    private Scanner scanner;

    public ConsoleIOService() {
        scanner = new Scanner(System.in);
    }

    @Override
    public void writeln(String text) {
        System.out.println(text);
    }

    @Override
    public String readln(String text) {
        System.out.print(text);
        return scanner.nextLine();
    }

    @Override
    public String readln() {
        return scanner.nextLine();
    }
}
