package com.diy.lisp.model;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import static com.diy.lisp.model.Symbol.symbol;

public class SExpression extends AbstractSyntaxTree implements Iterable<AbstractSyntaxTree> {

    private List<AbstractSyntaxTree> expressions;

    public SExpression(List<AbstractSyntaxTree> expressions) {
        this.expressions = expressions;
    }

    public SExpression(AbstractSyntaxTree... expressions) {
        this.expressions = Arrays.asList(expressions);
    }

    public static SExpression sexp(AbstractSyntaxTree... expressions) {
        return new SExpression(expressions);
    }

    public static SExpression sexp(List<AbstractSyntaxTree> expressions) {
        return new SExpression(expressions);
    }

    public static SExpression quote(AbstractSyntaxTree... expressions) {
        ArrayList<AbstractSyntaxTree> exps = new ArrayList(Arrays.asList(expressions));
        exps.add(0, symbol("quote"));
        return sexp(exps);
    }

    public static SExpression quote(List<AbstractSyntaxTree> expressions) {
        expressions.add(0, symbol("quote"));
        return sexp(expressions);
    }

    @Override
    public AbstractSyntaxTree evaluate(List<AbstractSyntaxTree> exps, Environment env) {
        throw new NotImplementedException();
    }

    @Override
    public AbstractSyntaxTree evaluate(Environment env) {
        throw new NotImplementedException();
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

        SExpression that = (SExpression) o;

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
        return sexp(copied);
    }

    @Override
    public Iterator<AbstractSyntaxTree> iterator() {
        return expressions.iterator();
    }

    public AbstractSyntaxTree get(int index) {
        return expressions.get(index);
    }
}