package com.diylisp;

import com.diylisp.model.Bool;
import com.diylisp.model.Number;
import org.junit.Test;

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
     */
    @Test
    public void TestParsingListOfSymbols() {
        //throw new NotImplementedException();
    }
}
