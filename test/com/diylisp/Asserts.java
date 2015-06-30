package com.diylisp;

import java.util.function.Supplier;

import static junit.framework.TestCase.fail;

/**
 * Created by bendik solheim on 30/06/15.
 */
public class Asserts {

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

            fail(String.format("%s is not of type Lisp", e.getClass().toString()));
        }

        fail(String.format("Did not throw exception of type %s", c.toString()));
    }
}
