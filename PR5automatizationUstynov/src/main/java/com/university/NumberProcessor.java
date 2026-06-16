package com.university;

public class NumberProcessor {

    public int add(int a, int b) {
        return a + b;
    }

    public boolean isEven(int number) {
        return number % 2 == 0;
    }

    public int divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Ділення на нуль!");
        }
        return a / b;
    }
}