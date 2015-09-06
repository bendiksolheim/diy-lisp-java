package com.diy.lisp;

import com.diy.lisp.exception.LispException;
import com.diy.lisp.model.AbstractSyntaxTree;
import com.diy.lisp.model.SList;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static com.diy.lisp.Evaluator.evaluate;
import static com.diy.lisp.Parser.parse;
import static com.diy.lisp.TestHelpers.assertException;
import static com.diy.lisp.model.Bool.bool;
import static com.diy.lisp.model.Environment.env;
import static com.diy.lisp.model.Int.number;
import static com.diy.lisp.model.SList.list;
import static junit.framework.TestCase.assertEquals;

@Category(com.diy.lisp.TestPart6.class)
public class TestPart6 {

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
    public void testCreatingListsByQuoting() {
        assertEquals(parse("(1 2 3 #t)"), evaluate(parse("'(1 2 3 #t)"), env()));
    }

    /**
     * The `cons` function prepends an element to the front of the list.
     */
    @Test
    public void testCreatingListWithCons() {
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
    public void testCreatingLongerListsWithOnlyCons() {
        String program = "(cons 3 (cons (- 4 2) (cons 1 '())))";
        assertEquals(parse("(3 2 1)"), evaluate(parse(program), env()));
    }

    /**
     * `head` extracts the first element of a list
     */
    @Test
    public void testGettingFirstElementFromList() {
        assertEquals(number(1), evaluate(parse("(head '(1))"), env()));
        assertEquals(number(1), evaluate(parse("(head '(1 2 3 4 5))"), env()));
    }

    /**
     * If the list if empty there is no first element, and `head` should raise an error.
     */
    @Test
    public void testGettingFirstElementFromEmptyList() {
        assertException(LispException.class, () -> evaluate(parse("(head (quote ()))"), env()));
    }

    /**
     * Must be list to get `head`.
     */
    @Test
    public void testGettingHeadFromValue() {
        assertException(LispException.class, () -> evaluate(parse("(head #t)"), env()));
    }

    /**
     * `tail` returns the tail of the list.
     *
     * The tail is the list retained after removing the first element.
     */
    @Test
    public void testGettingTailOfList() {
        assertEquals(list(number(2), number(3)), evaluate(parse("(tail '(1 2 3))"), env()));
        assertEquals(list(), evaluate(parse("(tail '(1))"), env()));
    }

    /**
     * If the list is empty there is no tail, and `tail` should raise an error.
     */
    @Test
    public void testGettingTailFromEmptyList() {
        assertException(LispException.class, () -> evaluate(parse("(tail (quote ()))"), env()));
    }

    /**
     * Must be list to get `tail`.
     */
    @Test
    public void testGettingTailFromValue() {
        assertException(LispException.class, () -> evaluate(parse("(tail 1)"), env()));
    }

    /**
     * The `empty` form checks whether or not a list is empty
     */
    @Test
    public void testCheckingWhetherListIsEmpty() {
        assertEquals(bool(false), evaluate(parse("(empty '(1 2 3))"), env()));
        assertEquals(bool(false), evaluate(parse("(empty '(1))"), env()));

        assertEquals(bool(true), evaluate(parse("(empty '())"), env()));
        assertEquals(bool(true), evaluate(parse("(empty (tail '(1)))"), env()));
    }

    /**
     * Must be list to see if empty
     */
    @Test
    public void testGettingEmptyFromValue() {
        assertException(LispException.class, () -> evaluate(parse("(empty 123)"), env()));
    }
}
