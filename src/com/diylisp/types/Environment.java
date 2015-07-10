package com.diylisp.types;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Environment {

    private final Map<String, Object> variables;

    public Environment() {
        variables = new HashMap<>();
    }

    public Environment(HashMap<String, Object> variables) {
        this.variables = variables;
    }

    public Object lookup(String key) {
        return null;
    }

    public Environment extend(HashMap<String, Object> variables) {
        /*HashMap<String, Object> oldVariables = this.variables
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> null
                ));*/

        return null;
    }
}
