package com.diy.lisp.model;

import com.diy.lisp.exception.NotImplementedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.diy.lisp.model.Symbol.symbol;

public class SList extends AbstractSyntaxTree implements Iterable<AbstractSyntaxTree> {

    private List<AbstractSyntaxTree> expressions;

    @Override
    public AbstractSyntaxTree evaluate(List<AbstractSyntaxTree> exps, Environment env) {
        throw new NotImplementedException("Not implemented yet. You'll do this part on your own");
    }

    @Override
    public AbstractSyntaxTree evaluate(Environment env) {
        throw new NotImplementedException("Not implemented yet. You'll do this part on your own");
    }

    /**
     * The code below is there for your convenience, and should not need
     * to be changed by you. Feel free to use it as you wish, though!
     */

    public SList(List<AbstractSyntaxTree> expressions) {
        this.expressions = expressions;
    }

    public SList(AbstractSyntaxTree... expressions) {
        this.expressions = Arrays.asList(expressions);
    }

    public static SList list(AbstractSyntaxTree... expressions) {
        return new SList(expressions);
    }

    public static SList list(List<AbstractSyntaxTree> expressions) {
        return new SList(expressions);
    }

    public static SList quote(AbstractSyntaxTree expression) {
        ArrayList<AbstractSyntaxTree> exps = new ArrayList(Arrays.asList(symbol("quote"), expression));
        return list(exps);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SList that = (SList) o;

        if (!expressions.equals(that.expressions)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return expressions.hashCode();
    }

    @Override
    public String toString() {
        if (expressions.size() == 0)
            return "()";

        String first = expressions.get(0).toString();
        String exps = (first.equals("quote")
                ? expressions.subList(1, expressions.size())
                : expressions)
                .stream()
                .map(AbstractSyntaxTree::toString)
                .collect(Collectors.joining(" "));
        return first.equals("quote")
            ? "'" + exps
            : "(" + exps + ")";
    }

    @Override
    public AbstractSyntaxTree copy() {
        List<AbstractSyntaxTree> copied = expressions
                .stream()
                .map(AbstractSyntaxTree::copy)
                .collect(Collectors.toCollection(ArrayList::new));
        return list(copied);
    }

    @Override
    public Iterator<AbstractSyntaxTree> iterator() {
        return expressions.iterator();
    }

    public AbstractSyntaxTree get(int index) {
        return expressions.get(index);
    }
}