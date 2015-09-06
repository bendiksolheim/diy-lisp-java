package com.diy.lisp.model;

import com.diy.lisp.exception.NotImplementedException;

public class Int extends Atom {

    private int value;

    @Override
    public AbstractSyntaxTree evaluate(Environment env) {
        throw new NotImplementedException("Not implemented yet. You'll do this part on your own");
    }

    /**
     * The code below is there for your convenience, and should not need
     * to be changed by you. Feel free to use it as you wish, though!
     */

    public Int(int value) {
        this.value = value;
    }

    public Int(String value) {
        this.value = Integer.parseInt(value);
    }

    public static Int number(int value) {
        return new Int(value);
    }

    public static Int number(String value) {
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
    public AbstractSyntaxTree copy() {
        return number(value);
    }
}
