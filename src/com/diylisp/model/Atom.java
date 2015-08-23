package com.diylisp.model;

public abstract class Atom extends AbstractSyntaxTree {

    @Override
    public boolean isAtom() {
        return true;
    }
}
