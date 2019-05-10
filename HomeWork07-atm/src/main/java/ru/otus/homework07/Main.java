package ru.otus.homework07;

// Написать эмулятор АТМ (банкомата).
// Объект класса АТМ должен уметь
// • принимать банкноты разных номиналов (на каждый номинал должна быть своя ячейка)
// • выдавать запрошенную сумму минимальным количеством банкнот или ошибку если сумму нельзя выдать
// • выдавать сумму остатка денежных средств

import ru.otus.homework07.atm.Atm;

public class Main {
    public static void main(String[] args) {
        Atm atm = new Atm();
        Terminal terminal = new Terminal();
        terminal.startSession(atm);
    }
}
