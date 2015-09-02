package com.diy.lisp;

import com.diy.lisp.exception.LispException;
import com.diy.lisp.model.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

import static com.diy.lisp.model.Bool.bool;
import static com.diy.lisp.model.Closure.closure;

/**
 * This is the evaluator module. The `evaluate` function below is the heart
 * of your language, and the focus for most of parts 2 through 6.
 */
public class Evaluator {

    /**
     * Tip: make each type responsible of evaluating itself, and you won't have to check
     * what instance `ast` is.
     */
    public static AbstractSyntaxTree evaluate(AbstractSyntaxTree ast, Environment env) {
        throw new NotImplementedException();
    }
}
