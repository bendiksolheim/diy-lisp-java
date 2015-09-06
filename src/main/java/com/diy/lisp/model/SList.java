package com.diy.lisp.model;

import com.diy.lisp.exception.LispException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.diy.lisp.Evaluator.evaluateList;
import static com.diy.lisp.model.Bool.bool;
import static com.diy.lisp.model.Symbol.symbol;

public class SList extends AbstractSyntaxTree implements Iterable<AbstractSyntaxTree> {

    private List<AbstractSyntaxTree> expressions;

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

    public int size() {
        return expressions.size();
    }

    public List<Symbol> asSymbols() {
        return expressions.stream().map(exp -> (Symbol) exp).collect(Collectors.toList());
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
    public AbstractSyntaxTree evaluate(List<AbstractSyntaxTree> exps, Environment env) {
        exps.set(0, evaluateList(expressions, env));
        return exps.get(0).evaluate(exps, env);
    }

    @Override
    public AbstractSyntaxTree evaluate(Environment env) {
        if (expressions.size() == 0)
            throw new LispException("List was empty");

        if (expressions.get(0).equals(symbol("quote")))
            return expressions.get(1);

        return evaluateList(expressions, env);
    }

    @Override
    public AbstractSyntaxTree copy() {
        List<AbstractSyntaxTree> copied = expressions
                .stream()
                .map(AbstractSyntaxTree::copy)
                .collect(Collectors.toCollection(ArrayList::new));
        return list(copied);
    }

    public AbstractSyntaxTree cons(AbstractSyntaxTree head) {
        expressions.add(0, head);
        return list(expressions);
    }

    public AbstractSyntaxTree head() {
        if (expressions.size() == 0)
            throw new LispException("Cannot call head on an empty list");

        return expressions.get(0);
    }

    public AbstractSyntaxTree tail() {
        if (expressions.size() == 0)
            throw new LispException("Cannot call tail on an empty list");

        return list(expressions.subList(1, expressions.size()));
    }

    public AbstractSyntaxTree isEmpty() {
        return bool(expressions.size() == 0);
    }

    @Override
    public Iterator<AbstractSyntaxTree> iterator() {
        return expressions.iterator();
    }

    public AbstractSyntaxTree get(int index) {
        return expressions.get(index);
    }
}