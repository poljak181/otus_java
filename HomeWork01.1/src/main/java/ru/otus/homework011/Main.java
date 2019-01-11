package ru.otus.homework011;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("There are no input arguments");
            return;
        }

        if (args.length > 1) {
            System.out.println("Only one argument expected");
            return;
        }

        StringAnalyzer.printCharacterOccurrence(args[0]);
    }
}
