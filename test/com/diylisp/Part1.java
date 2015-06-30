package com.diylisp;

import com.diylisp.exception.ParseException;
import com.diylisp.model.*;
import com.diylisp.model.Number;
import org.junit.Test;

import static com.diylisp.model.Symbol.symbol;
import static com.diylisp.model.Bool.bool;
import static com.diylisp.model.Quote.quote;
import static com.diylisp.model.SExpression.sexp;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;

public class Part1 {

    /**
     *  Symbols are represented by text strings. Parsing a single atom should result
     *  in an AST consisting of only that symbol
     */
    @Test
    public void TestParsingASingleSymbol() {
        assertEquals(new Symbol("foo"), Parser.parse("foo"));
    }

    /**
     *  Booleans are the special symbols #t and #f. In the ASTs they are represented
     *  by Javas Boolean.TRUE and Boolean.FALSE, respectively.
     */
    @Test
    public void TestParsingSingleBooleans() {
        assertEquals(new Bool(true), Parser.parse("#t"));
        assertEquals(new Bool(false), Parser.parse("#f"));
    }

    /**
     *  Integers are represented in the ASTs as Java Integers.
     *  Tip: Integer has a handy static function `parseInt`.
     */
    @Test
    public void TestParsingSingleInteger() {
        assertEquals(new Number(42), Parser.parse("42"));
        assertEquals(new Number(1337), Parser.parse("1337"));
    }

    /**
     *  A list is represented by a number of elements surrounded by parens.
     *  Tip: The useful helper function `findMathingParen` is already provided in the Parser class
     */
    @Test
    public void TestParsingListOfOnlySymbols() {
        assertEquals(
                new SExpression(asList(new Symbol("foo"), new Symbol("bar"), new Symbol("baz"))),
                Parser.parse("(foo bar baz)")
        );

        assertEquals(new SExpression(asList()), Parser.parse("()"));
    }

    /**
     *  When parsing lists, make sure each of the sub-expressions are also parsed properly
     */
    @Test
    public void TestParsingListOfMixedTypes() {
        assertEquals(
                new SExpression(asList(new Symbol("foo"), Bool.True, new Number(123))),
                Parser.parse("(foo #t 123)")
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
        assertEquals(ast, Parser.parse(program));
    }

    /**
     *  The proper exception should be raised if the expression is incomplete
     */
    @Test
    public void TestParseExceptionMissingParen() {
        String program = "(foo (bar x y)";
        try {
            Parser.parse(program);
            fail("Should get an exception on previous line");
        } catch (Exception e) {
            assertTrue(e instanceof ParseException);
            assertTrue(e.getMessage().startsWith("Incomplete expression"));
        }
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
            Parser.parse(program);
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
        String program = "                      \n" +
                "                               \n" +
                "(program          with much      whitespace)" +
                "                               \n";
        SExpression expected = sexp(new Symbol("program"), new Symbol("with"), new Symbol("much"), new Symbol("whitespace"));
        assertEquals(expected, Parser.parse(program));
    }

    /**
     * All comments should be stripped away as a part of the parsing
     */
    @Test
    public void TestParseComments() {
        String program = ";; this first line is a comment\n" +
                "(define variable\n" +
                "   ; this is also a comment\n" +
                "       (if #t\n" +
                "           42 ; inline comment!\n" +
                "           (something else)))";
        SExpression expected = sexp(
                new Symbol("define"),
                new Symbol("variable"),
                sexp(
                        new Symbol("if"),
                        new Bool(true),
                        new Number(42),
                        sexp(
                                new Symbol("something"),
                                new Symbol("else")
                        )
                )
        );
        assertEquals(expected, Parser.parse(program));
    }

    /**
     * Test a larger exmple to check that everything works as expected
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
                new Symbol("define"),
                new Symbol("fact"),
                sexp(
                        new Symbol("lambda"),
                        sexp(
                                new Symbol("n")
                        ),
                        sexp(
                                new Symbol("if"),
                                sexp(
                                        new Symbol("<="),
                                        new Symbol("n"),
                                        new Number(1)
                                ),
                                new Number(1),
                                sexp(
                                        new Symbol("*"),
                                        new Symbol("n"),
                                        sexp(
                                                new Symbol("fact"),
                                                sexp(
                                                        new Symbol("-"),
                                                        new Symbol("n"),
                                                        new Number(1)
                                                )
                                        )
                                )
                        )
                )
        );
        assertEquals(expected, Parser.parse(program));
    }

    /**
     * Quiting is a shorthand syntax for calling the `quote` form
     * Examples:
     *  'foo -> (quote foo)
     *  '(foo bar) -> (quote (foo bar))
     */
    @Test
    public void TestExpandSingleQuotedSymbol() {
        String program = "(foo 'nil)";
        SExpression expected = sexp(
                new Symbol("foo"),
                quote(new Symbol("nil"))
        );
        assertEquals(expected, Parser.parse(program));
    }

    /**
     * One final test to see that quote expansion works
     */
    @Test
    public void TestExpandCrazyQuoteCombo() {
        String program = "'(this ''''(makes ''no) 'sense)";
        assertEquals(program, Parser.parse(program).toString());
    }
}
