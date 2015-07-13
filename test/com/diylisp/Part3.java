package com.diylisp;

import com.diylisp.model.Bool;
import com.diylisp.model.Environment;
import org.junit.Test;

import static com.diylisp.Evaluator.evaluate;
import static com.diylisp.Parser.parse;
import static com.diylisp.model.Int.number;
import static junit.framework.TestCase.assertEquals;

public class Part3 {

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
    public void TestNestedExpressions() {
        String expression = "(eq #f (> (- (+ 1 3) (* 2 (mod 7 4))) 4 ))";
        assertEquals(Bool.True, evaluate(parse(expression), new Environment()));
    }

    /**
     * If statements are basic control structures
     *
     * The `if` should first evaluate its first argument. If this evaluates to true, then
     * the second argument is evaluated and returned. Otherwise the third and last argument
     * is evaluated and returned instead
     */
    @Test
    public void TestBasicIfStatement() {
        assertEquals(number(42), evaluate(parse("(if #t 42 1000)"), new Environment()));
        assertEquals(number(1000), evaluate(parse("(if #f 42 1000)"), new Environment()));
        assertEquals(Bool.True, evaluate(parse("(if #t #t #f)"), new Environment()));
    }

    /**
     * The branch of the if statement that is discarded should never be evaluated
     */
    @Test
    public void TestOnlyCorrectBranchIsEvaluated() {
        assertEquals(number(42), evaluate(parse("(if #f (this should not be evaluated) 42)"), new Environment()));
    }

    /**
     * A final test with a more complex if expression.
     * This test should already be passing if the above ones are.
     */
    @Test
    public void TestIfWithSubExpression() {
        String program = "" +
                "(if (> 1 2)" +
                "   (- 1000 1)" +
                "   (+ 40 (- 3 1)))";
        assertEquals(number(42), evaluate(parse(program), new Environment()));
    }
}
