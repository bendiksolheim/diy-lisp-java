package com.diylisp;

import com.diylisp.exception.ParseException;
import com.diylisp.model.AbstractSyntaxTree;
import com.diylisp.model.Bool;
import com.diylisp.model.Closure;
import com.diylisp.model.Environment;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static com.diylisp.Interpreter.interpret;
import static com.diylisp.Interpreter.interpretFile;
import static com.diylisp.Parser.parse;
import static com.diylisp.TestHelpers.assertException;
import static com.diylisp.model.Bool.bool;
import static com.diylisp.model.Int.number;
import static com.diylisp.model.Str.str;
import static com.diylisp.model.Symbol.symbol;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * In this last part, we provide tests for some suggestions on how to improve
 * the language a bit. Treat these tasks as optional, and suggestions only.
 * Feel free to do something completely different, if you fancy.
 */
public class Part8 {

    private Environment env;
    private String path = System.getProperty("user.dir") + File.separator + "stdlib.diy";

    @Before
    public void Before() {
        env = Environment.env();
        interpretFile(path, env);
    }

    /**
     * Suggestion 1: `cond`
     * First off, we will implement a new control structure found in most LISPs,
     * the `cond` form (not to be confused with `cons`). The name `cond` is short for
     * "conditional", and is sort of a buffed up version of `if`.
     *
     * Implement this as a new case in the part of your code which does evaluation.
     */

    /**
     * `cond` takes as arguments a list of tupes (two-element lists, or "conses").
     *
     * The first element of each list is evaluated in order, until one evaluates
     * to `#t`. The second element of each that tuple is returned.
     */
    @Test
    public void TestCondReturnsRightBranch() {
        String program = "" +
                "(cond ((#f 'foo)" +
                "       (#t 'bar)" +
                "       (#f 'baz)))";
        assertEquals(symbol("bar"), interpret(program, env));
    }

    /**
     * Of all second tuple elements, only the one we return is ever evaluated
     */
    @Test
    public void TestCondDoesNotEvaluateAllBranches() {
        interpret("(define foo 42)", env);
        String program = "" +
                "(cond ((#f fire-the-missiles)" +
                "       (#t foo)" +
                "       (#f something-else-we-wont-do)))";
        assertEquals(number(42), interpret(program, env));
    }

    /**
     * Once we find a predicate that evaluates to `#t`, no more predicates should
     * be evaluated.
     */
    @Test
    public void TestCondNotEvaluatingMorePredicatesThanNecessary() {
        String program = "" +
                "(cond ((#f 1)" +
                "       (#t 2)" +
                "       (dont-evaluate-me! 3)))";
        assertEquals(number(2), interpret(program, env));
    }

    /**
     * Remember to evaluate the predicates before checking whether they are true.
     */
    @Test
    public void TestCondEvaluatesPredicates() {
        String program = "" +
                "(cond (((not #t) 'totally-not-true)" +
                "       ((> 4 3) 'tru-dat)))";
        assertEquals(symbol("tru-dat"), interpret(program, env));
    }

    /**
     * If we evaluate all the predicates, only to find that none of them turned out
     * to be true, then `cond` should return `#f`.
     */
    @Test
    public void TestCondReturnsFalseAsDefault() {
        String program = "" +
                "(cond ((#f 'no)" +
                "       (#f 'nope)" +
                "       (#f 'i-dont-even)))";
        assertEquals(bool(false), interpret(program, env));
    }

    /**
     * Suggestion 2: Strings
     *
     * So far, our new language has been missing a central data type, one that no
     * real language could do without –– strings. So, let's add them to the language.
     */

    /**
     * First things first, we need to be able to parse strings.
     *
     * As you may have noticed, every type our language supports has its own type in
     * Java land. We will continue this way of doing things –– you will find a class called
     * `Str` in the `model` package, which you can use to implement string support.
     */
    @Test
    public void TestParsingSimpleStrings() {
        AbstractSyntaxTree ast = parse("\"foo bar\"");
        assertEquals(str("foo bar"), ast);
    }

    /**
     * Empty strings are strings too!
     */
    @Test
    public void TestParsingEmptyStrings() {
        assertEquals(str(""), parse("\"\""));
    }

    /**
     * We should be able to create strings with "-characters (double quote) by escaping them.
     *
     * (So, right now you might be put off by the crazy amount of backslashes in our strings.
     * Because the Java compiler uses backslash as an escape character, we need to escape our
     * backslashes to get a single backslash in our string. We also need to escape our double
     * quotes as they normally terminate the string, and hence after escaping, \\\" = \".
     */
    @Test
    public void TestParsingStringsWithEscapedDoubleQuotes() {
        AbstractSyntaxTree ast = parse("\"Say \\\"what\\\" one more time!\"");
        assertEquals(str("Say \\\"what\\\" one more time!"), ast);
    }

    /**
     * Strings that are not closed result in a parse error
     */
    @Test
    public void TestParsingUnclosedStrings() {
        assertException(ParseException.class, () -> parse("\"Hei, close me!"));
    }

    /**
     * Strings are delimited by the first and last(unescaped) double quotes.
     *
     * Thus, unescaped quotes followed by anything at all should be considered
     * invalid and throw an exception
     */
    @Test
    public void TestParsingStringsAreClosedByFirstClosingQuotes() {
        assertException(ParseException.class, () -> parse("\"foo\" bar"));
    }

    /**
     * Strings are one of the basic data types, and thus an atom. Strings should
     * therefore evaluate to themselves.
     */
    @Test
    public void TestEvaluatingStrings() {
        String quote = "\"The limits of my language means the limits of my world\"";
        assertEquals(str("The limits of my language means the limits of my world"), interpret(quote, env));
    }

    /**
     * It is common in many languages for strings to behave as lists. This can be
     * rather convenient, so let's make it that way here as well.
     *
     * We have four basic list functions: `cons`, `head`, `tail` and `empty`.
     *
     * To take the easy one first: `empty` should only return `#t` for the empty
     * string (and empty lists, as before.
     */
    @Test
    public void TestEmptyStringsBevaheAsEmptyLists() {
        assertEquals(bool(true), interpret("(empty \"\")", env));
        assertEquals(bool(false), interpret("(empty \"not empty\")", env));
    }

    /**
     * Next, `head` and `tail` needs to extract the first character and the rest
     * of the characters, respectively, from the string
     */
    @Test
    public void TestStringsHaveHeadsAndTails() {
        assertEquals(str("f"), interpret("(head \"foobar\")", env));
        assertEquals(str("oobar"), interpret("(tail \"foobar\")", env));
    }

    /**
     * Finally, we need to be able to reconstruct a string from its head and tail
     */
    @Test
    public void TestConsingStringsBackTogether() {
        assertEquals(str("foobar"), interpret("(cons \"f\" \"oobar\")", env));
    }

    /**
     * Suggestion 3: `let`
     *
     * The `let` form enables us to make local bindings.
     *
     * It takes two arguments. First a list of bindings, secondly an expression to be
     * evaluated within an environment where those bindings exists.
     */

    /**
     * The result when evaluating a `let` binding is the evaluation of the
     * expression given as argument.
     *
     * Let's first try one without any bindings.
     */
    @Test
    public void TestLetReturnsResultOfTheGivenExpression() {
        String program = "(let () (if #t 'yep 'nope))";
        assertEquals(symbol("yep"), interpret(program, env));
    }

    /**
     * The evaluation of the inner expression should have available the bindings
     * provided within the first argument.
     */
    @Test
    public void TestLetExtendsEnvironment() {
        String program = "" +
                "(let ((foo (+ 1000 42)))" +
                "     foo)";

        assertEquals(number(1042), interpret(program, env));
    }

    /**
     * Each new binding should have access to the previous bindings in the list.
     */
    @Test
    public void TestLetBindingsHaveAccessToPreviousBindings() {
        String program = "" +
                "(let ((foo 10)" +
                "      (bar (+ foo 5)))" +
                "     bar)";
        assertEquals(number(15), interpret(program, env));
    }

    /**
     * Let bindings should shadow definitions from outer environments
     */
    @Test
    public void TestLetBindingsOvershadowOuterEnvironment() {
        interpret("(define foo 1)", env);
        String program = "" +
                "(let ((foo 2))" +
                "     foo)";
        assertEquals(number(2), interpret(program, env));
    }

    /**
     * After the let is evaluated, all of its bindings are forgotten
     */
    @Test
    public void TestLetBindingsDoNotAffectOuterEnvironment() {
        interpret("(define foo 1)", env);
        assertEquals(number(2), interpret("(let ((foo 2)) foo)", env));
        assertEquals(number(1), interpret("foo", env));
    }

    /**
     * Suggestion 4: `defn`
     *
     * So far, to define functions we have had to write
     *
     *  (define my-function
     *      (lambda (foo bar)
     *          'function-body-here))
     *
     * It is a bit ugly to have to make a lambda every time you want a named function.
     * Let's add some syntactic sugar, shall we:
     *
     *  (defn my-function (foo bar)
     *      'function-body-here)
     */

    /**
     * Like `define`, the `defn` form should bind a variable to the environment.
     *
     * This variable should be a closure, just like if we had defined a new
     * variable using the old `define` + `lambda` syntax.
     */
    @Test
    public void TestDefnBindsTheVariableJustLikeDefine() {
        interpret("(defn foo (x) (> x 10))", env);
        assertTrue(env.lookup(symbol("foo")) instanceof Closure);
    }

    /**
     * The closure created should be no different than from the old syntax.
     */
    @Test
    public void TestDefnResultsInTheCorrectClosure() {
        interpret("(defn foo-1 (x) (> x 10))", env);
        interpret("(define foo-2 (lambda (x) (> x 10)))", env);

        AbstractSyntaxTree foo1 = env.lookup(symbol("foo-1"));
        AbstractSyntaxTree foo2 = env.lookup(symbol("foo-2"));

        assertEquals(foo1, foo2);
    }
}
