package com.diy.lisp;

import com.diy.lisp.model.Int;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class Operators {

    private static Map<String, BiFunction<Int, Int, Int>> math = new HashMap<String, BiFunction<Int, Int, Int>>() {{
        put("+", (a, b) -> a.plus(b));
        put("-", (a, b) -> a.minus(b));
        put("/", (a, b) -> a.divide(b));
        put("*", (a, b) -> a.multiply(b));
        put("mod", (a, b) -> a.mod(b));
    }};

    public static boolean isMathOperator(String candidate) {
        return math.containsKey(candidate);
    }

    public static boolean isLargerThanOperator(String candidate) {
        return candidate.equals(">");
    }

    public static BiFunction<Int, Int, Int> getOperator(String operator) {
        return math.get(operator);
    }
}
