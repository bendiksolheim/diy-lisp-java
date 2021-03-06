package com.diy.lisp;

import com.diy.lisp.exception.LispException;
import com.diy.lisp.model.Environment;
import com.diy.lisp.model.SList;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static com.diy.lisp.TestHelpers.assertException;
import static com.diy.lisp.Evaluator.evaluate;
import static com.diy.lisp.Parser.parse;
import static com.diy.lisp.model.Bool.bool;
import static com.diy.lisp.model.Int.number;
import static com.diy.lisp.model.SList.list;
import static com.diy.lisp.model.SList.quote;
import static com.diy.lisp.model.Symbol.symbol;
import static junit.framework.TestCase.assertEquals;

@Category(com.diy.lisp.TestPart2.class)
public class TestPart2 {

    /**
     * It's time to evaluate our expressions. From now on you will leave
     * `Parser.java` behind and continue on in the other files.
     * Evaluation starts in the file `Evaluator.java`, but some things
     * should be evaluated in the model files as well.
     */

    /**
     * Booleans should evaluate to themselves
     */
    @Test
    public void testEvaluatingBoolean() {
        assertEquals(bool(true), evaluate(bool(true), new Environment()));
        assertEquals(bool(false), evaluate(bool(false), new Environment()));
    }

    /**
     * ... and so should integers
     */
    @Test
    public void testEvaluatingNumbers() {
        assertEquals(number(42), evaluate(number(42), new Environment()));
    }

    /**
     * Let's have a look at the `quote` form. When you evaluate a `quote`,
     * you should return the quoted part without evaluating it.
     * So, evaluating `(quote foo)` should return `foo`, without evaluating
     * it.
     */
    @Test
    public void testEvaluatingQuote() {
        assertEquals(symbol("foo"), evaluate(quote(symbol("foo")), new Environment()));

        assertEquals(list(number(1), number(2), bool(false)),
                evaluate(quote(list(number(1), number(2), bool(false))), new Environment()));

        assertEquals(list(), evaluate(quote(list()), new Environment()));
    }

    /**
     * The `atom` form is used to determine whether an expression is an atom.
     *
     * Atoms are expressions that are not lists, i.e. integers, booleans or symbols.
     * Remember that the argument to `atom` must be evaluated before the the check is done.
     */
    @Test
    public void testEvaluatingAtomFunction() {
        assertEquals(bool(true), evaluate(list(symbol("atom"), bool(true)), new Environment()));
        assertEquals(bool(true), evaluate(list(symbol("atom"), bool(false)), new Environment()));
        assertEquals(bool(true), evaluate(list(symbol("atom"), number(42)), new Environment()));
        assertEquals(bool(true), evaluate(list(symbol("atom"), quote(symbol("foo"))), new Environment()));
        assertEquals(bool(false), evaluate(list(symbol("atom"), quote(list(number(1), number(2)))), new Environment()));
    }

    /**
     * The `eq` function is used to check whether two expressions are the same atom
     */
    @Test
    public void testEvaluatingEqFunction() {
        assertEquals(bool(true), evaluate(list(symbol("eq"), number(1), number(1)), new Environment()));
        assertEquals(bool(false), evaluate(list(symbol("eq"), number(1), number(2)), new Environment()));

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
    public void testBasicMathOperators() {
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
    public void testMathOperatorsOnlyWorkOnNumbers() {
        assertException(LispException.class, () -> evaluate(parse("(+ 1 'foo)"), new Environment()));
        assertException(LispException.class, () -> evaluate(parse("(- 1 'foo)"), new Environment()));
        assertException(LispException.class, () -> evaluate(parse("(/ 1 'foo)"), new Environment()));
        assertException(LispException.class, () -> evaluate(parse("(mod 1 'foo)"), new Environment()));
    }
}
