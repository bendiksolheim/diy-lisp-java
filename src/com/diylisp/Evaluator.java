package com.diylisp;

import com.diylisp.exception.LispException;
import com.diylisp.model.AbstractSyntaxTree;
import com.diylisp.model.Atom;
import com.diylisp.types.Environment;

import java.util.List;

public class Evaluator {

    // Each type is responsible for evaluating itself
    public static Object evaluate(AbstractSyntaxTree ast, Environment env) {
        return ast.evaluate(env);
    }

    // If we have a list, the first expression is responsible for parsing the rest
    public static Object evaluateList(List<AbstractSyntaxTree> exps, Environment env) {
        return exps.get(0).evaluate(exps, env);
    }

    public static boolean evaluateAtom(AbstractSyntaxTree exp, Environment env) {
        return Atom.isAtom(evaluate(exp, env));
    }

    public static boolean evaluateEq(List<AbstractSyntaxTree> exps, Environment env) {
        Object exp1 = evaluate(exps.get(1), env);
        Object exp2 = evaluate(exps.get(2), env);
        return exp1.equals(exp2) && Atom.isAtom(exp1);
    }

    public static int evaluateMath(String operator, List<AbstractSyntaxTree> exps, Environment env) {
        int exp1 = evaluateNumber(exps.get(1), env);
        int exp2 = evaluateNumber(exps.get(2), env);

        if (operator.equals("+"))
            return exp1 + exp2;

        if (operator.equals("-"))
            return exp1 - exp2;

        if (operator.equals("/"))
            return exp1 / exp2; // integer division is default when both the dividend and the divisor are integers

        if (operator.equals("*"))
            return exp1 * exp2;

        return exp1 % exp2;
    }

    public static Object evaluateBooleanMath(String value, List<AbstractSyntaxTree> exps, Environment env) {
        int exp1 = evaluateNumber(exps.get(1), env);
        int exp2 = evaluateNumber(exps.get(2), env);
        return exp1 > exp2;
    }

    public static Object evaluateIf(List<AbstractSyntaxTree> exps, Environment env) {
        boolean test = evaluateBoolean(exps.get(1), env);
        return evaluate(test ? exps.get(2) : exps.get(3), env);
    }

    public static int evaluateNumber(AbstractSyntaxTree exp, Environment env) {
        Object o = evaluate(exp, env);
        if (o instanceof Integer)
            return (int)o;

        throw new LispException(String.format("%s is not a number", exp.toString()));
    }

    public static boolean evaluateBoolean(AbstractSyntaxTree exp, Environment env) {
        Object o = evaluate(exp, env);
        if (o instanceof Boolean)
            return (boolean) o;

        throw new LispException(String.format("%s is not a bool", exp.toString()));
    }
}
