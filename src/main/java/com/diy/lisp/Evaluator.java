package com.diy.lisp;

import com.diy.lisp.exception.LispException;
import com.diy.lisp.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

import static com.diy.lisp.model.Bool.bool;
import static com.diy.lisp.model.Closure.closure;

/**
 * This is the evaluator module. The `evaluate` function below is the heart
 * of your language, and the focus for most of parts 2 through 6.
 */
public class Evaluator {

    // Each type is responsible for evaluating itself
    public static AbstractSyntaxTree evaluate(AbstractSyntaxTree ast, Environment env) {
        return ast.evaluate(env);
    }

    // If we have a list, the first expression is responsible for parsing the rest
    public static AbstractSyntaxTree evaluateList(List<AbstractSyntaxTree> exps, Environment env) {
        return exps.get(0).evaluate(exps, env);
    }

    public static Bool evaluateAtom(AbstractSyntaxTree exp, Environment env) {
        return bool(evaluate(exp, env).isAtom());
    }

    public static Bool evaluateEq(List<AbstractSyntaxTree> exps, Environment env) {
        AbstractSyntaxTree exp1 = evaluate(exps.get(1), env);
        AbstractSyntaxTree exp2 = evaluate(exps.get(2), env);
        return bool(exp1.equals(exp2) && exp1.isAtom());
    }

    public static Int evaluateMath(String operator, List<AbstractSyntaxTree> exps, Environment env) {
        Int a = evaluateNumber(exps.get(1), env);
        Int b = evaluateNumber(exps.get(2), env);
        BiFunction<Int, Int, Int> function = Operators.getOperator(operator);

        return function.apply(a, b);
    }

    public static Bool evaluateBooleanMath(String value, List<AbstractSyntaxTree> exps, Environment env) {
        Int exp1 = evaluateNumber(exps.get(1), env);
        Int exp2 = evaluateNumber(exps.get(2), env);
        return exp1.largerThan(exp2);
    }

    public static AbstractSyntaxTree evaluateIf(List<AbstractSyntaxTree> exps, Environment env) {
        Bool condition = evaluateBoolean(exps.get(1), env);
        return condition.evaluateIf(exps.get(2), exps.get(3), env);
    }

    public static AbstractSyntaxTree evaluateDefine(List<AbstractSyntaxTree> exps, Environment env) {
        if (exps.size() != 3)
            throw new LispException(String.format("Wrong number of arguments for define. Need 2, found  %s", exps.size() - 1));

        Symbol s = evaluateSymbol(exps.get(1));
        env.set(s, evaluate(exps.get(2), env));
        return s;
    }

    public static Closure evaluateLambda(List<AbstractSyntaxTree> exps, Environment env) {
        if (exps.size() != 3)
            throw new LispException(String.format("Wrong number of arguments for lambda. Need 2, found %s", exps.size() - 1));

        SExpression params = evaluateSexp(exps.get(1));
        return closure(env, params, exps.get(2));
    }

    public static Int evaluateNumber(AbstractSyntaxTree exp, Environment env) {
        AbstractSyntaxTree ast = evaluate(exp, env);
        if (ast instanceof Int)
            return (Int)ast;

        throw new LispException(String.format("%s is not a number", exp.toString()));
    }

    public static Bool evaluateBoolean(AbstractSyntaxTree exp, Environment env) {
        AbstractSyntaxTree ast = evaluate(exp, env);
        if (ast instanceof Bool)
            return (Bool) ast;

        throw new LispException(String.format("%s is not a bool", exp.toString()));
    }

    public static Symbol evaluateSymbol(AbstractSyntaxTree exp) {
        if (exp instanceof Symbol)
            return (Symbol) exp;

        throw new LispException(String.format("%s is not a symbol", exp.toString()));
    }

    public static SExpression evaluateSexp(AbstractSyntaxTree exp) {
        if (exp instanceof SExpression)
            return (SExpression) exp;

        throw new LispException(String.format("%s is not a sexp", exp));
    }

    public static Str evaluateStr(AbstractSyntaxTree exp) {
        if (exp instanceof Str)
            return (Str) exp;

        throw new LispException(String.format("%s is not an Str", exp));
    }

    public static AbstractSyntaxTree evaluateCons(List<AbstractSyntaxTree> exps, Environment env) {
        AbstractSyntaxTree head = evaluate(exps.get(1), env);
        AbstractSyntaxTree tail = evaluate(exps.get(2), env);
        if (tail instanceof SExpression) {
            SExpression list = evaluateSexp(tail);
            return list.cons(head);
        }

        return evaluateStr(tail).cons(evaluateStr(head));
    }

    public static AbstractSyntaxTree evaluateHead(List<AbstractSyntaxTree> exps, Environment env) {
        AbstractSyntaxTree evaluatedList = evaluate(exps.get(1), env);
        if (evaluatedList instanceof SExpression) {
            SExpression e = evaluateSexp(evaluatedList);
            return e.head();
        }

        return evaluateStr(evaluatedList).head();
    }

    public static AbstractSyntaxTree evaluateTail(List<AbstractSyntaxTree> exps, Environment env) {
        AbstractSyntaxTree evaluatedList = evaluate(exps.get(1), env);
        if (evaluatedList instanceof SExpression) {
            SExpression e = evaluateSexp(evaluatedList);
            return e.tail();
        }

        return evaluateStr(evaluatedList).tail();
    }

    public static AbstractSyntaxTree evaluateEmpty(List<AbstractSyntaxTree> exps, Environment env) {
        AbstractSyntaxTree evaluatedList = evaluate(exps.get(1), env);
        if (evaluatedList instanceof SExpression) {
            SExpression e = evaluateSexp(evaluatedList);
            return e.isEmpty();
        }

        return evaluateStr(evaluatedList).isEmpty();
    }

    public static AbstractSyntaxTree evaluateCond(List<AbstractSyntaxTree> exps, Environment env) {
        SExpression sexp = evaluateSexp(exps.get(1));
        SExpression subExp;
        for (AbstractSyntaxTree exp  : sexp) {
            subExp = evaluateSexp(exp);
            Bool cond = evaluateBoolean(subExp.get(0), env);
            if (cond.equals(bool(true)))
                return evaluate(subExp.get(1), env);
        }

        return bool(false);
    }

    public static AbstractSyntaxTree evaluateLet(List<AbstractSyntaxTree> exps, Environment env) {
        SExpression bindings = evaluateSexp(exps.get(1));
        for (AbstractSyntaxTree exp : bindings) {
            SExpression binding = evaluateSexp(exp);
            Symbol key = evaluateSymbol(binding.get(0));
            AbstractSyntaxTree value = evaluate(binding.get(1), env);
            env = env.extend(new HashMap<Symbol, AbstractSyntaxTree>() {{ put(key, value); }});
        }
        AbstractSyntaxTree expression = exps.get(2);
        return evaluate(expression, env);
    }

    public static AbstractSyntaxTree evaluateDefn(List<AbstractSyntaxTree> exps, Environment env) {
        Closure closure = evaluateLambda(exps.subList(1, exps.size()), env);
        env.set(evaluateSymbol(exps.get(1)), closure);
        return closure;
    }
}
