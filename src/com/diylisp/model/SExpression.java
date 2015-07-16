package com.diylisp.model;

import com.diylisp.Evaluator;
import com.diylisp.exception.LispException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SExpression extends AbstractSyntaxTree {

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
        String exps = expressions
                .stream()
                .map(AbstractSyntaxTree::toString)
                .collect(Collectors.joining(" "));
        return "(" + exps + ')';
    }

    @Override
    public AbstractSyntaxTree evaluate(List<AbstractSyntaxTree> exps, Environment env) {
        exps.set(0, Evaluator.evaluateList(expressions, env));
        return exps.get(0).evaluate(exps, env);
    }

    @Override
    public AbstractSyntaxTree evaluate(Environment env) {
        if (expressions.size() == 0)
            throw new LispException("List was empty");

        return Evaluator.evaluateList(expressions, env);
    }

    @Override
    public AbstractSyntaxTree copy() {
        List<AbstractSyntaxTree> copied = expressions.stream().map(AbstractSyntaxTree::copy).collect(Collectors.toCollection(ArrayList::new));
        return sexp(copied);
    }
}
