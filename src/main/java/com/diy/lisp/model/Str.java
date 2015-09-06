package com.diy.lisp.model;

import com.diy.lisp.exception.NotImplementedException;

public class Str extends Atom {

    private final String str;

    public Str(String str) {
        this.str = str;
    }

    public static Str str(String str) {
        return new Str(str);
    }

    @Override
    public AbstractSyntaxTree evaluate(Environment env) {
        throw new NotImplementedException("Not implemented yet. You'll do this as part on your own");
    }

    @Override
    public AbstractSyntaxTree copy() {
        return new Str(str + "");
    }

    @Override
    public String toString() {
        return String.format("\"%s\"", str);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Str)) return false;

        Str str1 = (Str) o;

        return str.equals(str1.str);

    }

    @Override
    public int hashCode() {
        return str.hashCode();
    }
}
