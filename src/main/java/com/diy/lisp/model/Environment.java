package com.diy.lisp.model;

import com.diy.lisp.exception.NotImplementedException;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    private final Map<Symbol, AbstractSyntaxTree> variables;

    public AbstractSyntaxTree lookup(Symbol key) {
        throw new NotImplementedException("Not implemented yet. You'll do this part on your own");
    }

    public Environment extend(HashMap<Symbol, AbstractSyntaxTree> variables) {
        throw new NotImplementedException("Not implemented yet. You'll do this part on your own");
    }

    public void set(Symbol key, AbstractSyntaxTree value) {
        throw new NotImplementedException("Not implemented yet. You'll do this part on your own");
    }

    /**
     * The code below is there for your convenience, and should not need
     * to be changed by you. Feel free to use it as you wish, though!
     */

    public Environment() {
        variables = new HashMap<>();
    }

    public Environment(HashMap<Symbol, AbstractSyntaxTree> variables) {
        this.variables = variables;
    }

    public static Environment env(HashMap<Symbol, AbstractSyntaxTree> variables) {
        return new Environment(variables);
    }

    public static Environment env() {
        return env(new HashMap<>());
    }

    @Override
    public String toString() {
        return "[Environment]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Environment)) return false;

        Environment that = (Environment) o;

        return variables.equals(that.variables);

    }

    @Override
    public int hashCode() {
        return variables.hashCode();
    }
}
