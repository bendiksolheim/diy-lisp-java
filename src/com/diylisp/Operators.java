package com.diylisp;

import java.util.ArrayList;

public class Operators {

    private static ArrayList<String> mathOperators = new ArrayList<String>() {{
        add("+");
        add("-");
        add("/");
        add("*");
        add("mod");
    }};

    public static boolean isMathOperator(String candidate) {
        return mathOperators.contains(candidate);
    }

    public static boolean isBooleanOperator(String candidate) {
        return candidate.equals(">");
    }
}
