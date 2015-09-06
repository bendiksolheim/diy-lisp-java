package com.diy.lisp;

import com.diy.lisp.exception.ParseException;
import com.diy.lisp.model.*;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static com.diy.lisp.Parser.parse;
import static com.diy.lisp.TestHelpers.assertException;
import static com.diy.lisp.model.Int.number;
import static com.diy.lisp.model.SExpression.quote;
import static com.diy.lisp.model.Symbol.symbol;
import static com.diy.lisp.model.Bool.bool;
import static com.diy.lisp.model.SExpression.sexp;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

@Category(com.diy.lisp.TestPart1.class)
public class TestPart1 {

    /**
     *  Symbols are represented by text strings. Parsing a single atom should result
     *  in an AST consisting of only that symbol
     */
    @Test
    public void TestParsingASingleSymbol() {
        assertEquals(symbol("foo"), parse("foo"));
    }

    /**
     *  Booleans are the special symbols #t and #f. In the ASTs they are represented
     *  by our Bool class, which encapsulates a Java boolean.
     */
    @Test
    public void TestParsingSingleBooleans() {
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
    public void TestParsingSingleInteger() {
        assertEquals(number(42), parse("42"));
        assertEquals(number(1337), parse("1337"));
    }

    /**
     *  A list is represented by a number of elements surrounded by parens.
     *  Tip: The useful helper function `findMathingParen` is already provided in the Parser class
     */
    @Test
    public void TestParsingListOfOnlySymbols() {
        assertEquals(
            sexp(symbol("foo"), symbol("bar"), symbol("baz")),
            parse("(foo bar baz)")
        );

        assertEquals(sexp(asList()), parse("()"));
    }

    /**
     *  When parsing lists, make sure each of the sub-expressions are also parsed properly
     */
    @Test
    public void TestParsingListOfMixedTypes() {
        assertEquals(
            sexp(symbol("foo"), bool(true), number(123)),
            parse("(foo #t 123)")
        );
    }

    /**
     *  Parsing should also handle nested lists properly
     */
    @Test
    public void TestParsingNestedLists() {
        String program = "(foo (bar ((#t)) x) (baz y))";
        AbstractSyntaxTree ast = sexp(
            symbol("foo"),
            sexp(symbol("bar"), sexp(sexp(bool(true))), symbol("x")),
            sexp(symbol("baz"), symbol("y"))
        );
        assertEquals(ast, parse(program));
    }

    /**
     *  The proper exception should be raised if the expression is incomplete
     */
    @Test
    public void TestParseExceptionMissingParen() {
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
    public void TestParseExceptionExtraParen() {
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
    public void TestParseWithExtraWhitespace() {
        String program = "                                   \n" +
                "                                            \n" +
                "(program          with much      whitespace)\n" +
                "                                            \n";
        SExpression expected = sexp(symbol("program"), symbol("with"), symbol("much"), symbol("whitespace"));
        assertEquals(expected, parse(program));
    }

    /**
     * All comments should be stripped away as a part of the parsing
     */
    @Test
    public void TestParseComments() {
        String program = ";; this first line is a comment\n" +
                "(define variable                        \n" +
                "   ; this is also a comment             \n" +
                "       (if #t                           \n" +
                "           42 ; inline comment!         \n" +
                "           (something else)))           \n";
        SExpression expected = sexp(
            symbol("define"),
            symbol("variable"),
            sexp(
                symbol("if"),
                bool(true),
                number(42),
                sexp(
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
    public void TestParseLargerExample() {
        String program = "" +
                "(define fact\n" +
                ";; Factorial function\n" +
                "(lambda (n)\n" +
                "   (if (<= n 1)\n" +
                "       1 ; Factorial of 0 is 1, and we deny\n" +
                "         ; the existence of negative numbers\n" +
                "       (* n (fact (- n 1))))))";
        SExpression expected = sexp(
            symbol("define"),
            symbol("fact"),
            sexp(
                symbol("lambda"),
                sexp(
                    symbol("n")
                ),
                sexp(
                    symbol("if"),
                    sexp(
                        symbol("<="),
                        symbol("n"),
                        number(1)
                    ),
                    number(1),
                    sexp(
                        symbol("*"),
                        symbol("n"),
                        sexp(
                            symbol("fact"),
                            sexp(
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
    public void TestExpandSingleQuotedSymbol() {
        String program = "(foo 'nil)";
        SExpression expected = sexp(
            symbol("foo"),
            sexp(symbol("quote"), symbol("nil"))
        );
        assertEquals(expected, parse(program));
    }

    @Test
    public void TestNestedQuotes() {
        assertEquals(quote(quote(quote(quote(symbol("foo"))))), parse("''''foo"));
    }

    /**
     * One final test to see that quote expansion works
     */
    @Test
    public void TestExpandCrazyQuoteCombo() {
        String program = "'(this ''''(makes ''no) 'sense)";
        assertEquals(program, parse(program).toString());
    }
}
