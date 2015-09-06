package com.diy.lisp;

import com.diy.lisp.exception.NotImplementedException;
import com.diy.lisp.exception.ParseException;
import com.diy.lisp.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

/**
 * This is the parser module, with the `parse` function which you'll implement
 * as part part 1 of the workshop. Its job is to convert string into data
 * structures that the evaluator can understand.
 */
public class Parser {

    /**
     * Parse string representation of one *single* expression into
     * the corresponding AbstractSyntaxTree
     */
    public static AbstractSyntaxTree parse(String source) {
        throw new NotImplementedException("Not implemented yet. You'll do this part on your own");
    }

    /**
     * Below are a few useful utility functions. These should come in handy when
     * implementing `parse`. We don't want to spend the day implementing parenthesis
     * counting, after all.
     */

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
