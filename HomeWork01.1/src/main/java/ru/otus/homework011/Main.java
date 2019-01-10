package ru.otus.homework011;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("There are no input arguments");
            return;
        }

        StringBuilder builder = new StringBuilder(args[0]);
        for (int i = 1; i < args.length; i++) {
            builder.append(" " + args[i]);
        }

        StringAnalyzer.printCharacterOccurrence(builder.toString());
    }
}
