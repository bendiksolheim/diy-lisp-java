package com.diylisp;

import com.diylisp.exception.LispException;
import com.diylisp.model.AbstractSyntaxTree;
import com.diylisp.model.Atom;
import com.diylisp.model.Bool;
import com.diylisp.model.Int;
import com.diylisp.types.Environment;

import java.util.List;

import static com.diylisp.model.Bool.bool;
import static com.diylisp.model.Int.number;

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
        return bool(Atom.isAtom(evaluate(exp, env)));
    }

    public static Bool evaluateEq(List<AbstractSyntaxTree> exps, Environment env) {
        Object exp1 = evaluate(exps.get(1), env);
        Object exp2 = evaluate(exps.get(2), env);
        return bool(exp1.equals(exp2) && Atom.isAtom(exp1));
    }

    public static Int evaluateMath(String operator, List<AbstractSyntaxTree> exps, Environment env) {
        Int exp1 = evaluateNumber(exps.get(1), env);
        Int exp2 = evaluateNumber(exps.get(2), env);

        if (operator.equals("+"))
            return exp1.plus(exp2);

        if (operator.equals("-"))
            return exp1.minus(exp2);

        if (operator.equals("/"))
            return exp1.divide(exp2); // integer division is default when both the dividend and the divisor are integers

        if (operator.equals("*"))
            return exp1.multiply(exp2);

        return exp1.mod(exp2);
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
}
