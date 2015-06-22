package com.diylisp;

import com.diylisp.model.AbstractSyntaxTree;
import com.diylisp.model.Bool;
import com.diylisp.model.Number;
import com.diylisp.model.SExpression;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.diylisp.Symbol.symbol;
import static com.diylisp.model.Bool.bool;
import static com.diylisp.model.SExpression.sexp;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class ParserTest {

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
}
