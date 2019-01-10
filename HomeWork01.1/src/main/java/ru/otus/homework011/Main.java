package ru.otus.homework011;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("There are no input arguments");
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            builder.append(args[i] + " ");
        }

        StringAnalyzer.printCharacterOccurrence(builder.toString());
    }
}
