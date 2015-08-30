package com.diy.lisp.model;

import com.diy.lisp.Evaluator;
import com.diy.lisp.exception.LispException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Closure extends AbstractSyntaxTree {

    public final Environment env;
    public final SExpression params;
    public final AbstractSyntaxTree body;

    public Closure(Environment env, SExpression params, AbstractSyntaxTree body) {
        this.env = env;
        this.params = params;
        this.body = body;
    }

    public static Closure closure(Environment env, SExpression params, AbstractSyntaxTree body) {
        return new Closure(env, params, body);
    }

    @Override
    public AbstractSyntaxTree evaluate(Environment env) {
        return null;
    }

    @Override
    public AbstractSyntaxTree copy() {
        return closure(env, (SExpression)params.copy(), body.copy());
    }

    @Override
    public AbstractSyntaxTree evaluate(List<AbstractSyntaxTree> exps, Environment env) {
        if (params.size() != exps.size() - 1)
            throw new LispException(String.format("Wrong number of arguments in function call. Excepted %s, got %s.", params.size(), exps.size() - 1));

        if (params.size() > 0) {
            HashMap<Symbol, AbstractSyntaxTree> newVars = merge(params.asSymbols(), exps.subList(1, exps.size()), env);
            Environment newEnvironment = this.env.extend(newVars);
            return Evaluator.evaluate(body, newEnvironment);
        }

        return Evaluator.evaluate(body, this.env);
    }

    /**
     * Creates a HashMap from a list of symbols and a list of expressions, while evaluating the expressions with env.
     */
    private HashMap<Symbol, AbstractSyntaxTree> merge(List<Symbol> params, List<AbstractSyntaxTree> expressions, Environment env) {
        HashMap<Symbol, AbstractSyntaxTree> map = new HashMap<>();

        Iterator<Symbol> symbols = params.iterator();
        Iterator<AbstractSyntaxTree> exps = expressions.iterator();
        while (symbols.hasNext() && exps.hasNext()) {
            map.put(symbols.next(), Evaluator.evaluate(exps.next(), env));
        }

        return map;
    }

    @Override
    public String toString() {
        return "[Closure]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Closure)) return false;

        Closure closure = (Closure) o;

        if (!env.equals(closure.env)) return false;
        if (!params.equals(closure.params)) return false;
        return body.equals(closure.body);

    }

    @Override
    public int hashCode() {
        int result = env.hashCode();
        result = 31 * result + params.hashCode();
        result = 31 * result + body.hashCode();
        return result;
    }
}