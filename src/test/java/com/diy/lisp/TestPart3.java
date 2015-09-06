package com.diy.lisp;

import com.diy.lisp.model.Environment;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static com.diy.lisp.Evaluator.evaluate;
import static com.diy.lisp.Parser.parse;
import static com.diy.lisp.model.Bool.bool;
import static com.diy.lisp.model.Int.number;
import static junit.framework.TestCase.assertEquals;

@Category(com.diy.lisp.TestPart3.class)
public class TestPart3 {

    /**
     * Remember, functions should evaluate their arguments
     *
     * (Except `quote` and `if`, that is, which aren't really functions...) Thus,
     * nested expressions should work just fine without any further work at this point.
     *
     * If this test is failing, make sure that `+`, `>` and so on is evaluating their
     * arguments before operating on them
     */
    @Test
    public void testNestedExpressions() {
        String expression = "(eq #f (> (- (+ 1 3) (* 2 (mod 7 4))) 4 ))";
        assertEquals(bool(true), evaluate(parse(expression), new Environment()));
    }

    /**
     * If statements are basic control structures
     *
     * The `if` should first evaluate its first argument. If this evaluates to true, then
     * the second argument is evaluated and returned. Otherwise the third and last argument
     * is evaluated and returned instead
     */
    @Test
    public void testBasicIfStatement() {
        assertEquals(number(42), evaluate(parse("(if #t 42 1000)"), new Environment()));
        assertEquals(number(1000), evaluate(parse("(if #f 42 1000)"), new Environment()));
        assertEquals(bool(true), evaluate(parse("(if #t #t #f)"), new Environment()));
    }

    /**
     * The branch of the if statement that is discarded should never be evaluated
     */
    @Test
    public void testOnlyCorrectBranchIsEvaluated() {
        assertEquals(number(42), evaluate(parse("(if #f (this should not be evaluated) 42)"), new Environment()));
    }

    /**
     * A final test with a more complex if expression.
     * This test should already be passing if the above ones are.
     */
    @Test
    public void testIfWithSubExpression() {
        String program = "" +
                "(if (> 1 2)" +
                "   (- 1000 1)" +
                "   (+ 40 (- 3 1)))";
        assertEquals(number(42), evaluate(parse(program), new Environment()));
    }
}
