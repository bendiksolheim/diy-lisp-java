package com.diy.lisp.model;

public abstract class Atom extends AbstractSyntaxTree {

    @Override
    public boolean isAtom() {
        return true;
    }
}
