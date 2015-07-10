package com.diylisp.model;

import com.diylisp.Evaluator;
import com.diylisp.types.Environment;

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
    public AbstractSyntaxTree evaluate(Environment env) {
        return Evaluator.evaluateList(expressions, env);
    }
}
