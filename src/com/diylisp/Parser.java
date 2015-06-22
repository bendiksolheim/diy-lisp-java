package com.diylisp;

import com.diylisp.model.*;
import com.diylisp.model.Number;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class Parser {

    public static AbstractSyntaxTree parse(String source) {
        source = removeComments(source).trim();

        if (source.equals("#t"))
            return Bool.True;

        if (source.equals("#f"))
            return Bool.False;

        if (Number.isNumber(source))
            return new Number(source);

        if (source.charAt(0) == '(') {
            int end = findMatchingParen(source);
            String[] expressionList = splitExpressions(source.substring(1, end));
            List<AbstractSyntaxTree> expressions = asList(expressionList)
                    .stream()
                    .map(Parser::parse)
                    .collect(Collectors.toList());
            return new SExpression(expressions);
        }

        return new Symbol(source);
    }

    private static String removeComments(String source) {
        return source.replaceAll(";.*\n", "\n");
    }

    private static int findMatchingParen(String source) {
        return findMatchingParen(source, 0);
    }

    private static int findMatchingParen(String source, int start) {
        int pos = start;
        int openBrackets = 1;
        while (openBrackets > 0) {
            pos++;
            if (pos == source.length())
                throw new RuntimeException("Expected EOF");

            if (source.charAt(pos) == '(')
                openBrackets++;

            if (source.charAt(pos) == ')')
                openBrackets--;
        }

        return pos;
    }

    private static String[] splitExpressions(String source) {
        source = source.trim();

        List<String> expressions = new ArrayList<>();
        String[] split;
        while ((split = firstExpression(source)) != null) {
            source = split[1];
            expressions.add(split[0]);
        }

        return expressions.toArray(new String[]{});
    }


    private static String[] firstExpression(String source) {
        if (source == null || source.equals(""))
            return null;

        source = source.trim();
        if (source.charAt(0) == '(') {
            int index = findMatchingParen(source) + 1;
            return new String[] { source.substring(0, index), source.substring(index)};
        }

        Pattern p = Pattern.compile("^[^\\s']+");
        Matcher m = p.matcher(source);
        boolean found = m.find();
        if (!found)
            return null;

        int index = m.end();
        return new String[] { source.substring(0, index), source.substring(index) };
    }

}
