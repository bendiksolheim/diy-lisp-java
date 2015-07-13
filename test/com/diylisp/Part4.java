package com.diylisp;

import com.diylisp.exception.LispException;
import com.diylisp.model.Bool;
import com.diylisp.model.Environment;
import com.sun.tools.doclint.Env;
import junit.framework.Assert;
import org.junit.Test;

import static com.diylisp.Evaluator.evaluate;
import static com.diylisp.TestHelpers.assertException;
import static com.diylisp.TestHelpers.map;
import static com.diylisp.model.Int.number;
import static com.diylisp.model.Environment.env;
import static com.diylisp.model.Symbol.symbol;
import static junit.framework.TestCase.assertEquals;

public class Part4 {

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
    public void TestSimpleLookup() {
        Environment env = new Environment(map("var", number(42)));
        assertEquals(number(42), env.lookup("var"));
    }

    /**
     * When looking up an undefined symbol, an exception should be raised.
     *
     * The error message should ideally contain the relevant symbol, and inform that
     * it has not been defined.
     */
    @Test
    public void TestLookupOnMissingRaisesException() {
        Environment env = new Environment();
        assertException(LispException.class, () -> env.lookup("var"));
    }

    /**
     * The `extend` function return a new environment extended with more bindings
     */
    @Test
    public void TestLookupFromInnerEnv() {
        Environment env1 = new Environment(map("foo", number(42)));
        Environment env2 = env1.extend(map("bar", Bool.True));
        assertEquals(number(42), env2.lookup("foo"));
        assertEquals(Bool.True, env2.lookup("bar"));
    }

    /**
     * Extending overwrites old bindings to the same variable name
     */
    @Test
    public void TestLookupDeeplyNestedVar() {
        Environment env = env(map("a", number(1)))
                .extend(map("b", number(2)))
                .extend(map("c", number(3)))
                .extend(map("foo", number(100)));
        assertEquals(number(100), env.lookup("foo"));
    }

    /**
     * The extend method should create a new environment, leaving the old one unchanged.
     */
    @Test
    public void TestExtendReturnsNewEnvironment() {
        Environment env = env(map("foo", number(1)));
        Environment extended = env.extend(map("foo", number(2)));
        assertEquals(number(1), env.lookup("foo"));
        assertEquals(number(2), extended.lookup("foo"));
    }

    /**
     * When calling `set`, the environment should be updated
     */
    @Test
    public void TestSetChangesEnvironmentInPlace() {
        Environment env = new Environment();
        env.set("foo", number(2));
        assertEquals(number(2), env.lookup("foo"));
    }

    /**
     * Variables can only be defined once.
     *
     * Setting a variable in an environment where it is already defined should result
     * in an appropriate error
     */
    @Test
    public void TestRedefinVariablesIllegal() {
        Environment env = env(map("foo", number(1)));
        assertException(LispException.class, () -> {
            env.set("foo", number(2));
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
    public void TestEvaluatingSymbol() {
        Environment env = env(map("foo", number(42)));
        assertEquals(number(42), evaluate(symbol("foo"), env));
    }

    /**
     * Referencing undefined variables should raise an appropriate exception
     *
     * This test should already be working if you implemented the environment correctly
     */
    @Test
    public void TestLookupMissingVariable() {
        Environment env = new Environment();
        assertException(LispException.class, () -> env.lookup("foo"));
    }
}
