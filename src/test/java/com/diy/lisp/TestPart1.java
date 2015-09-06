package com.diy.lisp;

import com.diy.lisp.exception.ParseException;
import com.diy.lisp.model.*;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static com.diy.lisp.Parser.parse;
import static com.diy.lisp.TestHelpers.assertException;
import static com.diy.lisp.model.Int.number;
import static com.diy.lisp.model.SList.list;
import static com.diy.lisp.model.SList.quote;
import static com.diy.lisp.model.Symbol.symbol;
import static com.diy.lisp.model.Bool.bool;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

@Category(com.diy.lisp.TestPart1.class)
public class TestPart1 {

    /**
     *  Symbols are represented by text strings. Parsing a single atom should result
     *  in an AST consisting of only that symbol
     */
    @Test
    public void testParsingASingleSymbol() {
        assertEquals(symbol("foo"), parse("foo"));
    }

    /**
     *  Booleans are the special symbols #t and #f. In the ASTs they are represented
     *  by our Bool class, which encapsulates a Java boolean.
     */
    @Test
    public void testParsingSingleBooleans() {
        assertEquals(bool(true), parse("#t"));
        assertEquals(bool(false), parse("#f"));
    }

    /**
     *  Integers are represented in the ASTs by our Int class, which encapsulates a Java int.
     *  Tip: Integer has a handy static function `parseInt`.
     *  Tip: the Int class has a handy function `isNumber` which checks if a string
     *  is a number
     */
    @Test
    public void testParsingSingleInteger() {
        assertEquals(number(42), parse("42"));
        assertEquals(number(1337), parse("1337"));
    }

    /**
     *  A list is represented by a number of elements surrounded by parens.
     *  Tip: The useful helper function `findMathingParen` is already provided in the Parser class
     */
    @Test
    public void testParsingListOfOnlySymbols() {
        assertEquals(
            list(symbol("foo"), symbol("bar"), symbol("baz")),
            parse("(foo bar baz)")
        );

        assertEquals(list(asList()), parse("()"));
    }

    /**
     *  When parsing lists, make sure each of the sub-expressions are also parsed properly
     */
    @Test
    public void testParsingListOfMixedTypes() {
        assertEquals(
            list(symbol("foo"), bool(true), number(123)),
            parse("(foo #t 123)")
        );
    }

    /**
     *  Parsing should also handle nested lists properly
     */
    @Test
    public void testParsingNestedLists() {
        String program = "(foo (bar ((#t)) x) (baz y))";
        AbstractSyntaxTree ast = list(
                symbol("foo"),
                list(symbol("bar"), list(list(bool(true))), symbol("x")),
                list(symbol("baz"), symbol("y"))
        );
        assertEquals(ast, parse(program));
    }

    /**
     *  The proper exception should be raised if the expression is incomplete
     */
    @Test
    public void testParseExceptionMissingParen() {
        String program = "(foo (bar x y)";
        assertException(ParseException.class, () -> parse(program));
    }

    /**
     * Another exception is raised if the expression is too large
     *
     * The parse function expects to receive only one, single expression. Anything
     * more than this should result in the proper exception
     */
    @Test
    public void testParseExceptionExtraParen() {
        String program = "(foo (bar x y)))";
        try {
            parse(program);
            fail("Should get an exception on previous line");
        } catch (RuntimeException e) {
            assertTrue(e instanceof ParseException);
            assertTrue(e.getMessage().startsWith("Expected EOF"));
        }
    }

    /**
     * Excess whitespace should be removed
     */
    @Test
    public void testParseWithExtraWhitespace() {
        String program = "                                   \n" +
                "                                            \n" +
                "(program          with much      whitespace)\n" +
                "                                            \n";
        SList expected = list(symbol("program"), symbol("with"), symbol("much"), symbol("whitespace"));
        assertEquals(expected, parse(program));
    }

    /**
     * All comments should be stripped away as a part of the parsing
     */
    @Test
    public void testParseComments() {
        String program = ";; this first line is a comment\n" +
                "(define variable                        \n" +
                "   ; this is also a comment             \n" +
                "       (if #t                           \n" +
                "           42 ; inline comment!         \n" +
                "           (something else)))           \n";
        SList expected = list(
                symbol("define"),
                symbol("variable"),
                list(
                        symbol("if"),
                        bool(true),
                        number(42),
                        list(
                                symbol("something"),
                                symbol("else")
                        )
                )
        );
        assertEquals(expected, parse(program));
    }

    /**
     * Test a larger example to check that everything works as expected
     */
    @Test
    public void testParseLargerExample() {
        String program = "" +
                "(define fact\n" +
                ";; Factorial function\n" +
                "(lambda (n)\n" +
                "   (if (<= n 1)\n" +
                "       1 ; Factorial of 0 is 1, and we deny\n" +
                "         ; the existence of negative numbers\n" +
                "       (* n (fact (- n 1))))))";
        SList expected = list(
                symbol("define"),
                symbol("fact"),
                list(
                        symbol("lambda"),
                        list(
                                symbol("n")
                        ),
                        list(
                                symbol("if"),
                                list(
                                        symbol("<="),
                                        symbol("n"),
                                        number(1)
                                ),
                                number(1),
                                list(
                                        symbol("*"),
                                        symbol("n"),
                                        list(
                                                symbol("fact"),
                                                list(
                                                        symbol("-"),
                                                        symbol("n"),
                                                        number(1)
                                                )
                                        )
                                )
                        )
                )
        );
        assertEquals(expected, parse(program));
    }

    /**
     * Quoting is a shorthand syntax for calling the `quote` form
     * Examples:
     *  'foo -> (quote foo)
     *  '(foo bar) -> (quote (foo bar))
     */
    @Test
    public void testExpandSingleQuotedSymbol() {
        String program = "(foo 'nil)";
        SList expected = list(
                symbol("foo"),
                list(symbol("quote"), symbol("nil"))
        );
        assertEquals(expected, parse(program));
    }

    @Test
    public void testNestedQuotes() {
        assertEquals(quote(quote(quote(quote(symbol("foo"))))), parse("''''foo"));
    }

    /**
     * One final test to see that quote expansion works
     */
    @Test
    public void testExpandCrazyQuoteCombo() {
        String program = "'(this ''''(makes ''no) 'sense)";
        assertEquals(program, parse(program).toString());
    }
}
