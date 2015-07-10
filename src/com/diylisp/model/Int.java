package com.diylisp.model;

import com.diylisp.types.Environment;

import static com.diylisp.model.Bool.bool;

public class Int extends Atom {

    private int value;

    public Int(int value) {
        this.value = value;
    }

    public Int(String value) {
        this.value = Integer.parseInt(value);
    }

    public static Int number(int value) {
        return new Int(value);
    }

    public static boolean isNumber(String value) {
        return value.matches("-?\\d+$");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Int number = (Int) o;

        if (value != number.value) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public AbstractSyntaxTree evaluate(Environment env) {
        return this;
    }

    public Bool largerThan(Int n) {
        return bool(value > n.value);
    }

    public Int plus(Int n) {
        return number(value + n.value);
    }

    public Int minus(Int n) {
        return number(value - n.value);
    }

    public Int divide(Int n) {
        return number(value / n.value);
    }

    public Int multiply(Int n) {
        return number(value * n.value);
    }

    public Int mod(Int n) {
        return number(value % n.value);
    }
}
