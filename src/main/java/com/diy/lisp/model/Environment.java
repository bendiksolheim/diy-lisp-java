package com.diy.lisp.model;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    private final Map<Symbol, AbstractSyntaxTree> variables;

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

    public AbstractSyntaxTree lookup(Symbol key) {
        throw new NotImplementedException();
    }

    public Environment extend(HashMap<Symbol, AbstractSyntaxTree> variables) {
        throw new NotImplementedException();
    }

    public void set(Symbol key, AbstractSyntaxTree value) {
        throw new NotImplementedException();
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
