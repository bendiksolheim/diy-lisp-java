package com.diy.lisp;

import com.diy.lisp.model.Environment;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static com.diy.lisp.Interpreter.interpret;
import static com.diy.lisp.model.Bool.bool;
import static com.diy.lisp.model.Environment.env;
import static com.diy.lisp.model.Int.number;
import static com.diy.lisp.model.SExpression.quote;
import static com.diy.lisp.model.SExpression.sexp;
import static com.diy.lisp.model.Symbol.symbol;
import static junit.framework.TestCase.assertEquals;

public class Part7 {

    private Environment env = env();
    private String path = System.getProperty("user.dir") + File.separator + "stdlib.diy";

    @Before
    public void Before() {
        env = env();
        Interpreter.interpretFile(path, env);
    }

    @Test
    public void TestNot() {
        assertEquals(bool(false), interpret("(not #t)", env));
        assertEquals(bool(true), interpret("(not #f)", env));
    }

    @Test
    public void TestOr() {
        assertEquals(bool(false), interpret(("(or #f #f)"), env));
        assertEquals(bool(true), interpret(("(or #t #f)"), env));
        assertEquals(bool(true), interpret(("(or #f #t)"), env));
        assertEquals(bool(true), interpret(("(or #t #t)"), env));
    }

    @Test
    public void TestAnd() {
        assertEquals(bool(false), interpret(("(and #f #f)"), env));
        assertEquals(bool(false), interpret(("(and #t #f)"), env));
        assertEquals(bool(false), interpret(("(and #f #t)"), env));
        assertEquals(bool(true), interpret(("(and #t #t)"), env));
    }

    @Test
    public void TestXor() {
        assertEquals(bool(false), interpret(("(xor #f #f)"), env));
        assertEquals(bool(true), interpret(("(xor #t #f)"), env));
        assertEquals(bool(true), interpret(("(xor #f #t)"), env));
        assertEquals(bool(false), interpret(("(xor #t #t)"), env));
    }

    /**
     * The language core just contains the > operator.
     * It's time to implement the rest.
     */
    @Test
    public void TestGreaterOrEqual() {
        assertEquals(bool(false), interpret("(>= 1 2)", env));
        assertEquals(bool(true), interpret("(>= 2 2)", env));
        assertEquals(bool(true), interpret("(>= 2 1)", env));
    }

    @Test
    public void TestLessOrEqual() {
        assertEquals(bool(true), interpret("(<= 1 2)", env));
        assertEquals(bool(true), interpret("(<= 2 2)", env));
        assertEquals(bool(false), interpret("(<= 2 1)", env));
    }

    @Test
    public void TestLessThan() {
        assertEquals(bool(true), interpret("(< 1 2)", env));
        assertEquals(bool(false), interpret("(< 2 2)", env));
        assertEquals(bool(false), interpret("(< 2 1)", env));
    }

    /**
     * Lets also implement some basic list functions.
     * These should be pretty easy with some basic recursion.
     */

    /**
     * Count the number of elements in a list
     *
     * Tip: how many elements are there in the empty list?
     */
    @Test
    public void TestLength() {
        assertEquals(number(5), interpret("(length '(1 2 3 4 5))", env));
        assertEquals(number(3), interpret("(length '(#t '(1 2 3) 'foo-bar))", env));
        assertEquals(number(0), interpret("(length '())", env));
    }

    @Test
    public void TestSum() {
        assertEquals(number(5), interpret("(sum '(1 1 1 1 1))", env));
        assertEquals(number(10), interpret("(sum '(1 2 3 4))", env));
        assertEquals(number(0), interpret("(sum '())", env));
    }

    /**
     * Output a list with a range of numbers.
     *
     * The two arguments define the bounds of the (inclusive) bounds of the range.
     */
    @Test
    public void TestRange() {
        assertEquals(sexp(number(1), number(2), number(3), number(4), number(5)), interpret("(range 1 5)", env));
        assertEquals(sexp(number(1)), interpret("(range 1 1)", env));
        assertEquals(sexp(), interpret("(range 2 1)", env));
    }

    /**
     * Append should merge two lists together.
     */
    @Test
    public void TestAppend() {
        assertEquals(sexp(), interpret("(append '() '())", env));
        assertEquals(sexp(number(1)), interpret("(append '() '(1))", env));
        assertEquals(sexp(number(2)), interpret("(append '(2) '())", env));
        assertEquals(sexp(number(1), number(2), number(3), number(4), number(5)), interpret("(append '(1 2) '(3 4 5))", env));
        assertEquals(sexp(bool(true), bool(false), quote(symbol("maybe"))), interpret("(append '(#t) '(#f 'maybe))", env));
    }

    /**
     * Reverse simply outputs the same list with elements in reverse order.
     *
     * Tip: See if you might be able to utilize the function you just made.
     */
    @Test
    public void TestReverse() {
        assertEquals(sexp(), interpret("(reverse '())", env));
        assertEquals(sexp(number(1)), interpret("(reverse '(1))", env));
        assertEquals(sexp(number(4), number(3), number(2), number(1)), interpret("(reverse '(1 2 3 4))", env));
    }

    /**
     * Next, our standard library should contain the three most fundamental functions:
     * `filter`, `map`, and `reduce`
     */

    /**
     * Filter removes any element not satisfying a predicate from a list
     */
    @Test
    public void TestFilter() {
        String program = "" +
                "(define even" +
                "   (lambda (x)" +
                "       (eq (mod x 2) 0)))";
        interpret(program, env);
        assertEquals(sexp(number(2), number(4), number(6)), interpret("(filter even '(1 2 3 4 5 6))", env));
    }

    /**
     * Map applies a given function to all elements of a list
     */
    @Test
    public void TestMap() {
        String program = "" +
                "(define inc" +
                "   (lambda (x) (+ 1 x)))";
        interpret(program, env);
        assertEquals(sexp(number(2), number(3), number(4)), interpret("(map inc '(1 2 3))", env));
    }

    /**
     * Reduce, also known as fold, reduces a list into a single value.
     *
     * It does this by combining elements two by two, until there is only
     * one left.
     *
     * If this is unfamiliar to you, have a look at
     * http://en.wikipedia.org/wiki/Fold_(higher-order_function)
     */
    @Test
    public void TestReduce() {
        String max = "" +
                "(define max" +
                "    (lambda (a b)" +
                "       (if (> a b) a b)))";
        interpret(max, env);
        assertEquals(number(6), interpret("(reduce max 0 '(1 6 3 2))", env));

        String add = "" +
                "(define add" +
                "   (lambda (a b) (+ a b)))";
        interpret(add, env);
        assertEquals(number(10), interpret("(reduce add 0 (range 1 4))", env));
    }

    /**
     * Finally, no stdlib is complete without a sorting algorithm.
     * Quicksort or mergesort might be good options, but you choose which
     * ever one you prefer.
     *
     * You might want to implement a few helper functions at the same time
     */
    @Test
    public void TestSort() {
        assertEquals(sexp(), interpret("(sort '())", env));
        assertEquals(sexp(number(1)), interpret("(sort '(1))", env));
        assertEquals(sexp(number(1), number(2), number(3), number(4), number(5), number(6), number(7)),
                interpret("(sort '(6 3 7 2 4 1 5))", env));
        assertEquals(sexp(number(1), number(2), number(3), number(4), number(5), number(6), number(7)),
                interpret("(sort '(7 6 5 4 3 2 1))", env));
        assertEquals(sexp(number(1), number(1), number(1)),
                interpret("(sort '(1 1 1))", env));
    }

}
