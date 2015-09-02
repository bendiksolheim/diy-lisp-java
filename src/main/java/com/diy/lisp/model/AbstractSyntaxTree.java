package com.diy.lisp.model;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

public abstract class AbstractSyntaxTree {
    public abstract AbstractSyntaxTree evaluate(Environment env);

    public AbstractSyntaxTree evaluate(List<AbstractSyntaxTree> exps, Environment env) {
        throw new NotImplementedException();
    }

    public abstract AbstractSyntaxTree copy();
}
