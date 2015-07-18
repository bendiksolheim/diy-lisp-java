package com.diylisp;

import com.diylisp.exception.LispException;
import com.diylisp.model.AbstractSyntaxTree;
import org.junit.Test;

import static com.diylisp.Evaluator.evaluate;
import static com.diylisp.Parser.parse;
import static com.diylisp.TestHelpers.assertException;
import static com.diylisp.model.Environment.env;
import static com.diylisp.model.Int.number;
import static junit.framework.TestCase.assertEquals;

public class Part6 {

    /**
     * One way to create lists is by quoting.
     *
     * We have already implemented `quote` so this test should already be
     * passing.
     *
     * The reason we need to use `quote` here is that otherwise the expression would
     * be seen as a call to the first element -- `1` in this case, which obviously isn't
     * a function.
     */
    @Test
    public void TestCreatingListsByQuoting() {
        assertEquals(parse("(1 2 3 #t)"), evaluate(parse("'(1 2 3 #t)"), env()));
    }

    /**
     * The `cons` function prepends an element to the front of the list.
     */
    @Test
    public void TestCreatingListWithCons() {
        AbstractSyntaxTree list = evaluate(parse("(cons 0 '(1 2 3))"), env());
        assertEquals(parse("(0 1 2 3)"), list);
    }

    /**
     * The `cons` needs to evaluate its arguments.
     *
     * Like all the other special forms and functions in our language, `cons` is
     * call-by-value. This means that the arguments must be evaluated before we
     * create the list with their values.
     */
    @Test
    public void TestCreatingLongerListsWithOnlyCons() {
        String program = "(cons 3 (cons (- 4 2) (cons 1 '())))";
        assertEquals(parse("(3 2 1)"), evaluate(parse(program), env()));
    }

    /**
     * `head` extracts the first element of a list
     */
    @Test
    public void TestGettingFirstElementFromList() {
        assertEquals(number(1), evaluate(parse("(head '(1))"), env()));
        assertEquals(number(1), evaluate(parse("(head '(1 2 3 4 5))"), env()));
    }

    /**
     * If the list if empty there is no first element, and `head` should raise an error.
     */
    @Test
    public void TestGettingFirstElementFromEmptyList() {
        assertException(RuntimeException.class, () -> evaluate(parse("(head (quote ()))"), env()));
    }
}
