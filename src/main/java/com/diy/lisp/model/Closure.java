package com.diy.lisp.model;

import com.diy.lisp.Evaluator;
import com.diy.lisp.exception.LispException;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
        throw new NotImplementedException();
    }

    @Override
    public AbstractSyntaxTree copy() {
        return closure(env, (SExpression)params.copy(), body.copy());
    }

    @Override
    public AbstractSyntaxTree evaluate(List<AbstractSyntaxTree> exps, Environment env) {
        throw new NotImplementedException();
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
