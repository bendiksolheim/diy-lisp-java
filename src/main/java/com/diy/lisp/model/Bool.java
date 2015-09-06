package com.diy.lisp.model;

import com.diy.lisp.exception.NotImplementedException;

public class Bool extends Atom {

    private boolean value;

    public Bool(boolean value) {
        this.value = value;
    }

    public static Bool bool(boolean value) {
        return new Bool(value);
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
        return value ? "#t" : "#f";
    }

    @Override
    public AbstractSyntaxTree evaluate(Environment env) {
        throw new NotImplementedException("Not implemented yet. You'll do this as part on your own");
    }

    @Override
    public AbstractSyntaxTree copy() {
        return bool(value);
    }
}
