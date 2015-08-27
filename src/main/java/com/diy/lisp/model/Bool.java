package com.diy.lisp.model;

import com.diy.lisp.Evaluator;

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
        return this;
    }

    @Override
    public AbstractSyntaxTree copy() {
        return bool(value);
    }

    public AbstractSyntaxTree evaluateIf(AbstractSyntaxTree ast1, AbstractSyntaxTree ast2, Environment env) {
        return Evaluator.evaluate(value ? ast1 : ast2, env);
    }
}
