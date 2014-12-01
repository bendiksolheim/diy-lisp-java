package com.diylisp.model;

public class Number extends Atom {

    private int value;

    public Number(int value) {
        this.value = value;
    }

    public Number(String value) {
        this.value = Integer.parseInt(value);
    }

    public static boolean isNumber(String value) {
        return value.matches("-?\\d+$");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Number number = (Number) o;

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
}
