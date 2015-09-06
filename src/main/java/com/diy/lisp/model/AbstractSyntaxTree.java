package com.diy.lisp.model;

import com.diy.lisp.exception.NotImplementedException;

import java.util.List;

public abstract class AbstractSyntaxTree {
    public abstract AbstractSyntaxTree evaluate(Environment env);

    public AbstractSyntaxTree evaluate(List<AbstractSyntaxTree> exps, Environment env) {
        throw new NotImplementedException("Not implemented yet. You'll do this as part on your own");
    }

    public abstract AbstractSyntaxTree copy();
}
