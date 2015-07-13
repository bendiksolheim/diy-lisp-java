package com.diylisp.model;

import com.diylisp.exception.LispException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Environment {

    private final Map<String, AbstractSyntaxTree> variables;

    public Environment() {
        variables = new HashMap<>();
    }

    public Environment(HashMap<String, AbstractSyntaxTree> variables) {
        this.variables = variables;
    }

    public static Environment env(HashMap<String, AbstractSyntaxTree> variables) {
        return new Environment(variables);
    }

    public AbstractSyntaxTree lookup(String key) {
        if (variables.containsKey(key))
            return variables.get(key);

        throw new LispException(String.format("Key '%s' was not found in environment", key));
    }

    public Environment extend(HashMap<String, AbstractSyntaxTree> variables) {
        HashMap<String, AbstractSyntaxTree> newVariables = new HashMap<>();
        this.variables.entrySet().stream().forEach((e) -> {
            newVariables.put(e.getKey(), e.getValue().copy());
        });

        newVariables.putAll(variables);

        return new Environment(newVariables);
    }

    public void set(String key, AbstractSyntaxTree value) {
        if (variables.containsKey(key))
            throw new LispException(String.format("Variable %s is already defined", key));

        variables.put(key, value);
    }
}
