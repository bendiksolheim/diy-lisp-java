package com.diylisp.model;

import com.diylisp.exception.LispException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    public AbstractSyntaxTree lookup(Symbol key) {
        if (variables.containsKey(key))
            return variables.get(key);

        throw new LispException(String.format("Key '%s' was not found in environment", key));
    }

    public Environment extend(HashMap<Symbol, AbstractSyntaxTree> variables) {
        HashMap<Symbol, AbstractSyntaxTree> newVariables = new HashMap<>();
        this.variables.entrySet().stream().forEach((e) -> {
            newVariables.put(e.getKey(), e.getValue().copy());
        });

        newVariables.putAll(variables);

        return new Environment(newVariables);
    }

    public void set(Symbol key, AbstractSyntaxTree value) {
        if (variables.containsKey(key))
            throw new LispException(String.format("Variable %s is already defined", key));

        variables.put(key, value);
    }
}
