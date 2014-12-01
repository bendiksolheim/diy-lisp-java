package com.diylisp;

import com.diylisp.model.*;
import com.diylisp.model.Number;

public class Parser {

    public static AbstractSyntaxTree parse(String source) {
        source = removeComments(source);

        if (source == "#t")
            return Bool.True;

        if (source == "#f")
            return Bool.False;

        if (Number.isNumber(source))
            return new Number(source);

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
