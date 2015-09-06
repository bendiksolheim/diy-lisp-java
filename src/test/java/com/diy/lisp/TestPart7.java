package com.diy.lisp;

import com.diy.lisp.model.Environment;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.File;

import static com.diy.lisp.Interpreter.interpret;
import static com.diy.lisp.model.Environment.env;
import static junit.framework.TestCase.assertEquals;

@Category(com.diy.lisp.TestPart7.class)
public class TestPart7 {

    private Environment env = env();
    private String path = System.getProperty("user.dir") + File.separator + "stdlib.diy";

    @Before
    public void before() {
        env = env();
        Interpreter.interpretFile(path, env);
    }

    @Test
    public void testNot() {
        assertEquals("#f", interpret("(not #t)", env));
        assertEquals("#t", interpret("(not #f)", env));
    }

    @Test
    public void testOr() {
        assertEquals("#f", interpret(("(or #f #f)"), env));
        assertEquals("#t", interpret(("(or #t #f)"), env));
        assertEquals("#t", interpret(("(or #f #t)"), env));
        assertEquals("#t", interpret(("(or #t #t)"), env));
    }

    @Test
    public void testAnd() {
        assertEquals("#f", interpret(("(and #f #f)"), env));
        assertEquals("#f", interpret(("(and #t #f)"), env));
        assertEquals("#f", interpret(("(and #f #t)"), env));
        assertEquals("#t", interpret(("(and #t #t)"), env));
    }

    @Test
    public void testXor() {
        assertEquals("#f", interpret(("(xor #f #f)"), env));
        assertEquals("#t", interpret(("(xor #t #f)"), env));
        assertEquals("#t", interpret(("(xor #f #t)"), env));
        assertEquals("#f", interpret(("(xor #t #t)"), env));
    }

    /**
     * The language core just contains the > operator.
     * It's time to implement the rest.
     */
    @Test
    public void testGreaterOrEqual() {
        assertEquals("#f", interpret("(>= 1 2)", env));
        assertEquals("#t", interpret("(>= 2 2)", env));
        assertEquals("#t", interpret("(>= 2 1)", env));
    }

    @Test
    public void testLessOrEqual() {
        assertEquals("#t", interpret("(<= 1 2)", env));
        assertEquals("#t", interpret("(<= 2 2)", env));
        assertEquals("#f", interpret("(<= 2 1)", env));
    }

    @Test
    public void testLessThan() {
        assertEquals("#t", interpret("(< 1 2)", env));
        assertEquals("#f", interpret("(< 2 2)", env));
        assertEquals("#f", interpret("(< 2 1)", env));
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
    public void testLength() {
        assertEquals("5", interpret("(length '(1 2 3 4 5))", env));
        assertEquals("3", interpret("(length '(#t '(1 2 3) 'foo-bar))", env));
        assertEquals("0", interpret("(length '())", env));
    }

    @Test
    public void testSum() {
        assertEquals("5", interpret("(sum '(1 1 1 1 1))", env));
        assertEquals("10", interpret("(sum '(1 2 3 4))", env));
        assertEquals("0", interpret("(sum '())", env));
    }

    /**
     * Output a list with a range of numbers.
     *
     * The two arguments define the bounds of the (inclusive) bounds of the range.
     */
    @Test
    public void testRange() {
        assertEquals("(1 2 3 4 5)", interpret("(range 1 5)", env));
        assertEquals("(1)", interpret("(range 1 1)", env));
        assertEquals("()", interpret("(range 2 1)", env));
    }

    /**
     * Append should merge two lists together.
     */
    @Test
    public void testAppend() {
        assertEquals("()", interpret("(append '() '())", env));
        assertEquals("(1)", interpret("(append '() '(1))", env));
        assertEquals("(2)", interpret("(append '(2) '())", env));
        assertEquals("(1 2 3 4 5)", interpret("(append '(1 2) '(3 4 5))", env));
        assertEquals("(#t #f 'maybe)", interpret("(append '(#t) '(#f 'maybe))", env));
    }

    /**
     * Reverse simply outputs the same list with elements in reverse order.
     *
     * Tip: See if you might be able to utilize the function you just made.
     */
    @Test
    public void testReverse() {
        assertEquals("()", interpret("(reverse '())", env));
        assertEquals("(1)", interpret("(reverse '(1))", env));
        assertEquals("(4 3 2 1)", interpret("(reverse '(1 2 3 4))", env));
    }

    /**
     * Next, our standard library should contain the three most fundamental functions:
     * `filter`, `map`, and `reduce`
     */

    /**
     * Filter removes any element not satisfying a predicate from a list
     */
    @Test
    public void testFilter() {
        String program = "" +
                "(define even" +
                "   (lambda (x)" +
                "       (eq (mod x 2) 0)))";
        interpret(program, env);
        assertEquals("(2 4 6)", interpret("(filter even '(1 2 3 4 5 6))", env));
    }

    /**
     * Map applies a given function to all elements of a list
     */
    @Test
    public void testMap() {
        String program = "" +
                "(define inc" +
                "   (lambda (x) (+ 1 x)))";
        interpret(program, env);
        assertEquals("(2 3 4)", interpret("(map inc '(1 2 3))", env));
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
    public void testReduce() {
        String max = "" +
                "(define max" +
                "    (lambda (a b)" +
                "       (if (> a b) a b)))";
        interpret(max, env);
        assertEquals("6", interpret("(reduce max 0 '(1 6 3 2))", env));

        String add = "" +
                "(define add" +
                "   (lambda (a b) (+ a b)))";
        interpret(add, env);
        assertEquals("10", interpret("(reduce add 0 (range 1 4))", env));
    }

    /**
     * Finally, no stdlib is complete without a sorting algorithm.
     * Quicksort or mergesort might be good options, but you choose which
     * ever one you prefer.
     *
     * You might want to implement a few helper functions at the same time
     */
    @Test
    public void testSort() {
        assertEquals("()", interpret("(sort '())", env));
        assertEquals("(1)", interpret("(sort '(1))", env));
        assertEquals("(1 2 3 4 5 6 7)", interpret("(sort '(6 3 7 2 4 1 5))", env));
        assertEquals("(1 2 3 4 5 6 7)", interpret("(sort '(7 6 5 4 3 2 1))", env));
        assertEquals("(1 1 1)", interpret("(sort '(1 1 1))", env));
    }

}
