package com.diylisp.model;

import com.diylisp.exception.LispException;
import com.diylisp.types.Environment;

import java.util.List;

public abstract class AbstractSyntaxTree {
    public abstract Object evaluate(Environment env);

    public Object evaluate(List<AbstractSyntaxTree> exps, Environment env) {
        throw new LispException(String.format("%s is not a function", exps.get(0).toString()));
    }
}
