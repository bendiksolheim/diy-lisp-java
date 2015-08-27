package com.diy.lisp;

import com.diy.lisp.exception.LispException;
import com.diy.lisp.model.Environment;
import org.junit.Test;

import static com.diy.lisp.TestHelpers.assertException;
import static com.diy.lisp.Evaluator.evaluate;
import static com.diy.lisp.Parser.parse;
import static com.diy.lisp.model.Bool.bool;
import static com.diy.lisp.model.Int.number;
import static com.diy.lisp.model.SExpression.quote;
import static com.diy.lisp.model.SExpression.sexp;
import static com.diy.lisp.model.Symbol.symbol;
import static junit.framework.TestCase.assertEquals;

public class Part2 {

    /**
     * Boolean should evaluate to themselves
     */
    @Test
    public void TestEvaluatingBoolean() {
        assertEquals(bool(true), evaluate(bool(true), new Environment()));
        assertEquals(bool(false), evaluate(bool(false), new Environment()));
    }

    /**
     * ... and so should integers
     */
    @Test
    public void TestEvaluatingNumbers() {
        assertEquals(number(42), evaluate(number(42), new Environment()));
    }

    /**
     * When a call is done to the `quote` form, the argument should be returned without being evaluated
     *
     * (quote foo) -> foo
     */
    @Test
    public void TestEvaluatingQuote() {
        assertEquals(symbol("foo"), evaluate(quote(symbol("foo")), new Environment()));

        assertEquals(sexp(number(1), number(2), bool(false)),
                evaluate(quote(sexp(number(1), number(2), bool(false))), new Environment()));

        assertEquals(sexp(), evaluate(quote(sexp()), new Environment()));
    }

    /**
     * The `atom` form is used to determine whether an expression is an atom.
     *
     * Atoms are expressions that are not lists, i.e. integers, booleans or symbols.
     * Remember that the argument to `atom` must be evaluated before the the check is done.
     */
    @Test
    public void TestEvaluatingAtomFunction() {
        assertEquals(bool(true), evaluate(sexp(symbol("atom"), bool(true)), new Environment()));
        assertEquals(bool(true), evaluate(sexp(symbol("atom"), bool(false)), new Environment()));
        assertEquals(bool(true), evaluate(sexp(symbol("atom"), number(42)), new Environment()));
        assertEquals(bool(true), evaluate(sexp(symbol("atom"), quote(symbol("foo"))), new Environment()));
        assertEquals(bool(false), evaluate(sexp(symbol("atom"), quote(sexp(number(1), number(2)))), new Environment()));
    }

    /**
     * The `eq` function is used to check whether two expressions are the same atom
     */
    @Test
    public void TestEvaluatingEqFunction() {
        assertEquals(bool(true), evaluate(sexp(symbol("eq"), number(1), number(1)), new Environment()));
        assertEquals(bool(false), evaluate(sexp(symbol("eq"), number(1), number(2)), new Environment()));

        // From this point on, the ASTs might sometimes be too long or cumbersome to
        // write down explicitly, and we'll use `parse` to make them for us.
        // Remember, if you need to have a look at exactly what is passed to `evaluate`,
        // just add a `System.out.println`-statement in `evaluate`

        assertEquals(bool(true), evaluate(parse("(eq 'foo 'foo)"), new Environment()));
        assertEquals(bool(false), evaluate(parse("(eq 'foo 'bar)"), new Environment()));

        // Lists are never equal, because lists are not atoms
        assertEquals(bool(false), evaluate(parse("(eq '(1 2 3) '(1 2 3))"), new Environment()));
    }

    /**
     * To be able to do anything useful, we need some basic math operators.
     *
     * Since we only operate with integers, `/` must represent integer division.
     * `mod` is the modulo operator.
     */
    @Test
    public void TestBasicMathOperators() {
        assertEquals(number(4), evaluate(parse("(+ 2 2)"), new Environment()));
        assertEquals(number(1), evaluate(parse("(- 2 1)"), new Environment()));
        assertEquals(number(3), evaluate(parse("(/ 6 2)"), new Environment()));
        assertEquals(number(3), evaluate(parse("(/ 7 2)"), new Environment()));
        assertEquals(number(6), evaluate(parse("(* 2 3)"), new Environment()));
        assertEquals(number(1), evaluate(parse("(mod 7 2)"), new Environment()));
        assertEquals(bool(true), evaluate(parse("(> 7 2)"), new Environment()));
        assertEquals(bool(false), evaluate(parse("(> 2 7)"), new Environment()));
        assertEquals(bool(false), evaluate(parse("(> 7 7)"), new Environment()));
    }

    /**
     * The math functions should only allow numbers as arguments
     */
    @Test
    public void TestMathOperatorsOnlyWorkOnNumbers() {
        assertException(LispException.class, () -> evaluate(parse("(+ 1 'foo)"), new Environment()));
        assertException(LispException.class, () -> evaluate(parse("(- 1 'foo)"), new Environment()));
        assertException(LispException.class, () -> evaluate(parse("(/ 1 'foo)"), new Environment()));
        assertException(LispException.class, () -> evaluate(parse("(mod 1 'foo)"), new Environment()));
    }
}
