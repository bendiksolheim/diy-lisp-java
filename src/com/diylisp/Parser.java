package com.diylisp;

import com.diylisp.exception.ParseException;
import com.diylisp.model.*;
import com.diylisp.model.Int;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.diylisp.model.SExpression.sexp;
import static com.diylisp.model.Symbol.symbol;
import static java.util.Arrays.asList;

public class Parser {

    public static AbstractSyntaxTree parse(String source) {
        source = removeComments(source).trim();

        if (hasMultipleExpressions(source))
            throw new ParseException("Expected EOF: " + source);

        if (source.equals("#t"))
            return Bool.True;

        if (source.equals("#f"))
            return Bool.False;

        if (Int.isNumber(source))
            return new Int(source);

        if (source.charAt(0) == '\'') {
            AbstractSyntaxTree quoted = parse(source.substring(1));
            List<AbstractSyntaxTree> exps = asList(new AbstractSyntaxTree[]{symbol("quote"), quoted});
            return sexp(exps);
        }

        if (source.charAt(0) == '\"') {
            return new Str(source.substring(1, source.length() - 1));
        }

        if (source.charAt(0) == '(') {
            int end = findMatchingParen(source);
            String[] expressionList = splitExpressions(source.substring(1, end));
            List<AbstractSyntaxTree> expressions = asList(expressionList)
                    .stream()
                    .map(Parser::parse)
                    .collect(Collectors.toList());
            return sexp(expressions);
        }

        return new Symbol(source);
    }

    public static List<AbstractSyntaxTree> parseMultiple(String source) {
        source = removeComments(source);
        return asList(splitExpressions(source))
                .stream()
                .map(Parser::parse)
                .collect(Collectors.toList());
    }

    public static String removeComments(String source) {
        return source.replaceAll(";.*\n", "\n");
    }

    private static boolean hasMultipleExpressions(String source) {
        String[] expressionList = splitExpressions(source);
        return expressionList.length >= 2;
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
                throw new ParseException("Incomplete expression: " + source.substring(start));

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

        if (source.charAt(0) == '\'') {
            String[] exps = firstExpression(source.substring(1));
            exps[0] = source.substring(0, 1) + exps[0];
            return exps;
        }

        if (source.charAt(0) == '\"') {
            for (int i = 1; i < source.length(); i++) {
                if (source.charAt(i) == '\"' && source.charAt(i - 1) != '\\') {
                    return new String[] {source.substring(0, i + 1), source.substring(i + 1, source.length())};
                }
            }

            throw new ParseException(String.format("Unclosed string: %s", source));
        }

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
