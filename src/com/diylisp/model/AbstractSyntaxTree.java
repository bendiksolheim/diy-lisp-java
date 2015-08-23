package com.diylisp.model;

import com.diylisp.exception.LispException;

import java.util.List;

public abstract class AbstractSyntaxTree {
    public abstract AbstractSyntaxTree evaluate(Environment env);

    public AbstractSyntaxTree evaluate(List<AbstractSyntaxTree> exps, Environment env) {
        throw new LispException(String.format("%s is not a function", exps.get(0).toString()));
    }

    public boolean isAtom() {
        return false;
    }

    public abstract AbstractSyntaxTree copy();
}
