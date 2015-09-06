package com.diy.lisp.model;

import com.diy.lisp.Evaluator;
import com.diy.lisp.Operators;

import java.util.List;

import static com.diy.lisp.model.SList.list;

public class Symbol extends Atom {

    private String value;

    public Symbol(String value) {
        this.value = value;
    }

    public static Symbol symbol(String value) {
        return new Symbol(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Symbol symbol = (Symbol) o;

        if (!value.equals(symbol.value)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public AbstractSyntaxTree evaluate(Environment env) {
        return env.lookup(this);
    }

    @Override
    public AbstractSyntaxTree evaluate(List<AbstractSyntaxTree> exps, Environment env) {
        if (value.equals("atom"))
            return Evaluator.evaluateAtom(exps.get(1), env);

        if (value.equals("eq"))
            return Evaluator.evaluateEq(exps, env);

        if (Operators.isMathOperator(value))
            return Evaluator.evaluateMath(value, exps, env);

        if (Operators.isLargerThanOperator(value))
            return Evaluator.evaluateLargerThan(value, exps, env);

        if (value.equals("if"))
            return Evaluator.evaluateIf(exps, env);

        if (value.equals("define"))
            return Evaluator.evaluateDefine(exps, env);

        if (value.equals("lambda"))
            return Evaluator.evaluateLambda(exps, env);

        if (value.equals("cons"))
            return Evaluator.evaluateCons(exps, env);

        if (value.equals("head"))
            return Evaluator.evaluateHead(exps, env);

        if (value.equals("tail"))
            return Evaluator.evaluateTail(exps, env);

        if (value.equals("empty"))
            return Evaluator.evaluateEmpty(exps, env);

        if (value.equals("cond"))
            return Evaluator.evaluateCond(exps, env);

        if (value.equals("let"))
            return Evaluator.evaluateLet(exps, env);

        if (value.equals("defn"))
            return Evaluator.evaluateDefn(exps, env);

        AbstractSyntaxTree val = env.lookup(this);
        exps.set(0, val);
        return Evaluator.evaluate(list(exps), env);
    }

    @Override
    public AbstractSyntaxTree copy() {
        return symbol(value);
    }
}
