package com.diylisp.model;

public class Bool extends Atom {

    public static Bool True = new Bool(true);
    public static Bool False = new Bool(false);

    private boolean value;

    public Bool(boolean value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bool bool = (Bool) o;

        if (value != bool.value) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (value ? 1 : 0);
    }

    @Override
    public String toString() {
        /*return value ? "#t" : "#f";*/
        return Boolean.toString(value);
    }
}
