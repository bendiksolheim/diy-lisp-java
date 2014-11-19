package com.diylisp;

import com.diylisp.model.Symbol;

public class Parser {

    public static Object parse(String source) {
        source = removeComments(source);

        if (source == "#t")
            return Boolean.TRUE;

        if (source == "#f")
            return Boolean.FALSE;

        if (source.matches("-?\\d+$"))
            return Integer.parseInt(source);

        return new Symbol(source);
    }

    private static String removeComments(String source) {
        return source.replaceAll(";.*\n", "\n");
    }

    private static int findMatchingParen(String source) {
        return findMatchingParen(source, 0);
    }

    private static int findMatchingParen(String source, int start) {
        return 0;
    }

}
