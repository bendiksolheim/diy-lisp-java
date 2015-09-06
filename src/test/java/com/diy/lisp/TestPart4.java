package com.diy.lisp;

import com.diy.lisp.exception.LispException;
import com.diy.lisp.model.Bool;
import com.diy.lisp.model.Environment;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static com.diy.lisp.Evaluator.evaluate;
import static com.diy.lisp.Parser.parse;
import static com.diy.lisp.TestHelpers.assertException;
import static com.diy.lisp.TestHelpers.map;
import static com.diy.lisp.model.Bool.bool;
import static com.diy.lisp.model.Int.number;
import static com.diy.lisp.model.Environment.env;
import static com.diy.lisp.model.Symbol.symbol;
import static junit.framework.TestCase.assertEquals;

@Category(com.diy.lisp.TestPart4.class)
public class TestPart4 {

    /**
     * Before we go on to evaluating programs using variables, we need to implement
     * an environment to store them in.
     *
     * It's time to fill in the blanks in the `Environment` class.
     */

    /**
     * An environment should store variables and provide lookup
     */
    @Test
    public void testSimpleLookup() {
        Environment env = new Environment(map(symbol("var"), number(42)));
        assertEquals(number(42), env.lookup(symbol("var")));
    }

    /**
     * When looking up an undefined symbol, an exception should be raised.
     *
     * The error message should ideally contain the relevant symbol, and inform that
     * it has not been defined.
     */
    @Test
    public void testLookupOnMissingRaisesException() {
        Environment env = new Environment();
        assertException(LispException.class, () -> env.lookup(symbol("var")));
    }

    /**
     * The `extend` function return a new environment extended with more bindings
     */
    @Test
    public void testLookupFromInnerEnv() {
        Environment env1 = new Environment(map(symbol("foo"), number(42)));
        Environment env2 = env1.extend(map(symbol("bar"), bool(true)));
        assertEquals(number(42), env2.lookup(symbol("foo")));
        assertEquals(bool(true), env2.lookup(symbol("bar")));
    }

    /**
     * Extending overwrites old bindings to the same variable name
     */
    @Test
    public void testLookupDeeplyNestedVar() {
        Environment env = env(map(symbol("a"), number(1)))
            .extend(map(symbol("b"), number(2)))
            .extend(map(symbol("c"), number(3)))
            .extend(map(symbol("foo"), number(100)));
        assertEquals(number(100), env.lookup(symbol("foo")));
    }

    /**
     * The extend method should create a new environment, leaving the old one unchanged.
     */
    @Test
    public void testExtendReturnsNewEnvironment() {
        Environment env = env(map(symbol("foo"), number(1)));
        Environment extended = env.extend(map(symbol("foo"), number(2)));
        assertEquals(number(1), env.lookup(symbol("foo")));
        assertEquals(number(2), extended.lookup(symbol("foo")));
    }

    /**
     * When calling `set`, the environment should be updated
     */
    @Test
    public void testSetChangesEnvironmentInPlace() {
        Environment env = new Environment();
        env.set(symbol("foo"), number(2));
        assertEquals(number(2), env.lookup(symbol("foo")));
    }

    /**
     * Variables can only be defined once.
     *
     * Setting a variable in an environment where it is already defined should result
     * in an appropriate error
     */
    @Test
    public void testRedefinVariablesIllegal() {
        Environment env = env(map(symbol("foo"), number(1)));
        assertException(LispException.class, () -> {
            env.set(symbol("foo"), number(2));
            return null;
        });
    }

    /**
     * With the `Environment` working, it's time to implement evaluation of expressions
     * with variables.
     */


    /**
     * Symbols (other than #t and #f) are treated as variable references.
     *
     * When evaluating a symbol, the corresponding value should be looked up in the
     * environment.
     */
    @Test
    public void testEvaluatingSymbol() {
        Environment env = env(map(symbol("foo"), number(42)));
        assertEquals(number(42), evaluate(symbol("foo"), env));
    }

    /**
     * Referencing undefined variables should raise an appropriate exception
     *
     * This test should already be working if you implemented the environment correctly
     */
    @Test
    public void testLookupMissingVariable() {
        Environment env = new Environment();
        assertException(LispException.class, () -> env.lookup(symbol("foo")));
    }

    /**
     * Test of simple `define` statements.
     *
     * The `defined` form is used to define new bindings to the environment.
     * A `defined` call should result in a change in the environment. What you
     * return from evaluating the definition is not important (although it
     * affects what's printed to the REPL)
     */
    @Test
    public void testDefine() {
        Environment env = new Environment();
        evaluate(parse("(define x 1000)"), env);
        assertEquals(number(1000), env.lookup(symbol("x")));
    }

    /**
     * Defines should have exactly two arguments, or raise an error.
     *
     * This type of check could benefit the other forms we implement as well,
     * and you might want to add them elsewhere. It quickly gets tiresome to
     * test for this however, so the tests won't require you to.
     */
    @Test
    public void testDefineWithWrongNumberOfArguments() {
        assertException(LispException.class, () -> evaluate(parse("(define x)"), new Environment()));
        assertException(LispException.class, () -> evaluate(parse("(defined x 1 2)"), new Environment()));
    }

    /**
     * Define requires the first argument to be a symbol
     */
    @Test
    public void testDefineWithNonSymbolAsVariable() {
        assertException(LispException.class, () -> evaluate(parse("(define #t 42)"), new Environment()));
    }

    /**
     * Test define and lookup variable in same environment
     *
     * This test should already be working when the above ones are passing
     */
    @Test
    public void testVariableLookupAfterDefine() {
        Environment env = new Environment();
        evaluate(parse("(define foo (+ 2 2))"), env);
        assertEquals(number(4), evaluate(symbol("foo"), env));
    }
}
