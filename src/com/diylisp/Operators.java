package com.diylisp;

import java.util.ArrayList;

/**
 * Created by bendik solheim on 29/06/15.
 */
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
