package com.diylisp;

import com.diylisp.model.AbstractSyntaxTree;
import com.diylisp.model.Symbol;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static junit.framework.TestCase.fail;

public class TestHelpers {

    /**
     * Takes a type of exception and a supplier, and fails if the supplier did not produce the expected exception
     * @param c
     * @param exp
     */
    public static void assertException(Class<? extends Exception> c, Supplier<Object> exp) {
        try {
            exp.get();
        } catch (Exception e) {
            if (e.getClass() == c)
                return;

            e.printStackTrace();
            fail(String.format("%s is not of type %s", e.getClass().toString(), c.toString()));
        }

        fail(String.format("Did not throw exception of type %s", c.toString()));
    }

    public static HashMap<Symbol, AbstractSyntaxTree> map(Symbol key, AbstractSyntaxTree value) {
        HashMap<Symbol, AbstractSyntaxTree> m = new HashMap<>();
        m.put(key, value);
        return m;
    }
}
