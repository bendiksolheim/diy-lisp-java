package com.diylisp.model;

import com.diylisp.types.Closure;

public abstract class Atom extends AbstractSyntaxTree {

    public static boolean isAtom(Object o) {
        return o instanceof Integer ||
                o instanceof String ||
                o instanceof Boolean ||
                o instanceof Closure ||
                o instanceof Atom;
    }

}
