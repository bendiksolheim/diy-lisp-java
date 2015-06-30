package com.diylisp;

import com.diylisp.exception.LispException;
import com.diylisp.model.*;
import com.diylisp.model.Number;
import com.diylisp.types.Environment;
import org.junit.Test;

import static com.diylisp.Asserts.assertException;
import static com.diylisp.Evaluator.evaluate;
import static com.diylisp.Parser.parse;
import static com.diylisp.model.Number.number;
import static com.diylisp.model.Quote.quote;
import static com.diylisp.model.SExpression.sexp;
import static com.diylisp.model.Symbol.symbol;
import static junit.framework.TestCase.assertEquals;

public class Part2 {

    /**
     * Boolean should evaluate to themselves
     */
    @Test
    public void TestEvaluatingBoolean() {
        assertEquals(true, evaluate(Bool.True, new Environment()));
        assertEquals(false, evaluate(Bool.False, new Environment()));
    }

    /**
     * ... and so should integers
     */
    @Test
    public void TestEvaluatingNumbers() {
        assertEquals(42, evaluate(number(42), new Environment()));
    }

    /**
     * When a call is done to the `quote` form, the argument should be returned without being evaluated
     *
     * (quote foo) -> foo
     */
    @Test
    public void TestEvaluatingQuote() {
        assertEquals(symbol("foo"), evaluate(quote(symbol("foo")), new Environment()));

        assertEquals(sexp(number(1), number(2), Bool.False),
                evaluate(quote(sexp(number(1), number(2), Bool.False)), new Environment()));

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
        assertEquals(true, evaluate(sexp(symbol("atom"), Bool.True), new Environment()));
        assertEquals(true, evaluate(sexp(symbol("atom"), Bool.False), new Environment()));
        assertEquals(true, evaluate(sexp(symbol("atom"), number(42)), new Environment()));
        assertEquals(true, evaluate(sexp(symbol("atom"), quote(symbol("foo"))), new Environment()));
        assertEquals(false, evaluate(sexp(symbol("atom"), quote(sexp(number(1), number(2)))), new Environment()));
    }

    /**
     * The `eq` function is used to check whether two expressions are the same atom
     */
    @Test
    public void TestEvaluatingEqFunction() {
        assertEquals(true, evaluate(sexp(symbol("eq"), number(1), number(1)), new Environment()));
        assertEquals(false, evaluate(sexp(symbol("eq"), number(1), number(2)), new Environment()));

        // From this point on, the ASTs might sometimes be too long or cumbersome to
        // write down explicitly, and we'll use `parse` to make them for us.
        // Remember, if you need to have a look at exactly what is passed to `evaluate`,
        // just add a `System.out.println`-statement in `evaluate`

        assertEquals(true, evaluate(parse("(eq 'foo 'foo)"), new Environment()));
        assertEquals(false, evaluate(parse("(eq 'foo 'bar)"), new Environment()));

        // Lists are never equal, because lists are not atoms
        assertEquals(false, evaluate(parse("(eq '(1 2 3) '(1 2 3))"), new Environment()));
    }

    /**
     * To be able to do anything useful, we need some basic math operators.
     *
     * Since we only operate with integers, `/` must represent integer division.
     * `mod` is the modulo operator.
     */
    @Test
    public void TestBasicMathOperators() {
        assertEquals(4, evaluate(parse("(+ 2 2)"), new Environment()));
        assertEquals(1, evaluate(parse("(- 2 1)"), new Environment()));
        assertEquals(3, evaluate(parse("(/ 6 2)"), new Environment()));
        assertEquals(3, evaluate(parse("(/ 7 2)"), new Environment()));
        assertEquals(6, evaluate(parse("(* 2 3)"), new Environment()));
        assertEquals(1, evaluate(parse("(mod 7 2)"), new Environment()));
        assertEquals(true, evaluate(parse("(> 7 2)"), new Environment()));
        assertEquals(false, evaluate(parse("(> 2 7)"), new Environment()));
        assertEquals(false, evaluate(parse("(> 7 7)"), new Environment()));
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
