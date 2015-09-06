package com.diy.lisp.model;

import com.diy.lisp.exception.NotImplementedException;

import java.util.List;

public class Symbol extends Atom {

    private String value;

    @Override
    public AbstractSyntaxTree evaluate(Environment env) {
        throw new NotImplementedException("Not implemented yet. You'll do this part on your own");
    }

    @Override
    public AbstractSyntaxTree evaluate(List<AbstractSyntaxTree> exps, Environment env) {
        throw new NotImplementedException("Not implemented yet. You'll do this part on your own");
    }

    /**
     * The code below is there for your convenience, and should not need
     * to be changed by you. Feel free to use it as you wish, though!
     */

    public Symbol(String value) {
        this.value = value;
    }

    public static Symbol symbol(String value) {
        return new Symbol(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Symbol symbol = (Symbol) o;

        if (!value.equals(symbol.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public AbstractSyntaxTree copy() {
        return symbol(value);
    }
}
