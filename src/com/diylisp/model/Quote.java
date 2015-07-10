package com.diylisp.model;

import com.diylisp.types.Environment;

public class Quote extends AbstractSyntaxTree {

    private AbstractSyntaxTree expression;

    public Quote(AbstractSyntaxTree expression) {
        this.expression = expression;
    }

    public static Quote quote(AbstractSyntaxTree expression) {
        return new Quote(expression);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quote)) return false;

        Quote quote = (Quote) o;

        return expression.equals(quote.expression);

    }

    @Override
    public int hashCode() {
        return expression.hashCode();
    }


    @Override
    public String toString() {
        return "'" + expression.toString();
    }

    @Override
    public AbstractSyntaxTree evaluate(Environment env) {
        return expression;
    }
}
