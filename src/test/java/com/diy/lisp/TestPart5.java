package com.diy.lisp;

import com.diy.lisp.exception.LispException;
import com.diy.lisp.model.*;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.HashMap;

import static com.diy.lisp.Evaluator.evaluate;
import static com.diy.lisp.Parser.parse;
import static com.diy.lisp.TestHelpers.assertException;
import static com.diy.lisp.TestHelpers.map;
import static com.diy.lisp.model.Environment.env;
import static com.diy.lisp.model.Int.number;
import static com.diy.lisp.model.SList.list;
import static com.diy.lisp.model.Symbol.symbol;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertEquals;

/**
 * This part is all about defining and using functions
 *
 * We'll start by implementing the `lamba` form, which is used to create function closures
 */

@Category(com.diy.lisp.TestPart5.class)
public class TestPart5 {

    /**
     * The lambda form should evaluate to a Closure
     */
    @Test
    public void testLambdaEvaluatesToClosure() {
        AbstractSyntaxTree ast = list(symbol("lambda"), list(), number(42));
        AbstractSyntaxTree closure = evaluate(ast, new Environment());
        assertTrue(closure instanceof Closure);
    }

    /**
     * The closure should keep a copy of the environment where it was defined.
     *
     * Once we start calling functions later, we'll need access to the environment
     * from where the function was created in order to resovle all free variables.
     */
    @Test
    public void testLambdaClosureKeepsDefiningEnv() {
        HashMap<Symbol, AbstractSyntaxTree> map = map(symbol("foo"), number(1));
        map.put(symbol("bar"), number(2));
        AbstractSyntaxTree ast = list(symbol("lambda"), list(), number(42));
        Environment env = new Environment(map);
        Closure closure = (Closure)evaluate(ast, env);
        assertEquals(env, closure.env);
    }

    /**
     * The closure contains the parameter list and function body too
     */
    @Test
    public void testLambdaClosureHoldsFunction() {
        Closure closure = (Closure) evaluate(parse("(lambda (x y) (+ x y))"), new Environment());
        assertEquals(list(symbol("x"), symbol("y")), closure.params);
        assertEquals(list(symbol("+"), symbol("x"), symbol("y")), closure.body);
    }

    /**
     * The parameter of a `lambda` should be a list.
     */
    @Test
    public void testLambdaArgumentsAreLists() {
        Closure closure = (Closure) evaluate(parse("(lambda (x y) (+ x y))"), new Environment());
        assertTrue(closure.params instanceof SList);

        String badProgram = "(lambda not-a-list (body of fn))";
        assertException(LispException.class, () -> evaluate(parse(badProgram), new Environment()));
    }

    /**
     * The `lambda` form should expect exactly two arguments.
     */
    @Test
    public void testLambdaNumberOfArguments() {
        String program = "(lambda (foo) (bar) (baz))";
        assertException(LispException.class, () -> evaluate(parse(program), new Environment()));
    }

    /**
     * The function body should not be evaluated when the lambda is defined.
     *
     * The call to `lambda` should return a function closure holding, among other things
     * the function body. The body should not be evaluated before the function is called.
     */
    @Test
    public void testDefiningLambdaWithErrorInBody() {
        String program = "" +
                "(lambda (x y)"+
                "   (function body ((that) would never) work))";
        AbstractSyntaxTree closure = evaluate(parse(program), new Environment());
        assertTrue(closure instanceof Closure);
    }


    /**
     * Now that we have the `lambda` form implemented, let's see if we can call some functions.
     *
     * When evaluating ASTs which are lists, if the first element isn't one of the special forms
     * we have been working with so far, it is a function call. The first element of the list is
     * the function, and the rest of the elements are arguments.
     */


    /**
     * The first case we'll handle is when the AST is a list with an actual closure
     * as the first element.
     *
     * In this first test, we'll start with a closure with no arguments and no free
     * variables. All we need to do is to evaluate and return the function body.
     */
    @Test
    public void testEvaluatingCallToClosure() {
        AbstractSyntaxTree closure = evaluate(parse("(lambda () (+ 1 2))"), new Environment());
        AbstractSyntaxTree ast = list(closure);
        AbstractSyntaxTree result = evaluate(ast, new Environment());
        assertEquals(number(3), result);
    }

    /**
     * The function body must be evaluated in an environment where the parameters are bound.
     *
     * Create an environment where the function parameters (which are stored in the closure)
     * are bound to the actual argument values in the function call. Use this environment
     * when evaluating the function body.
     */
    @Test
    public void testEvaluatingCallToClosureWithArguments() {
        Environment env = new Environment();
        AbstractSyntaxTree closure = evaluate(parse("(lambda (a b) (+ a b))"), new Environment());
        AbstractSyntaxTree ast = list(closure, number(4), number(5));
        assertEquals(number(9), evaluate(ast, env));
    }

    /**
     * Call to function should evaluate all arguments.
     *
     * When a function is applied, the arguments should be evaluated before being bound
     * to the parameter names.
     */
    @Test
    public void testCallToFunctionShouldEvaluateArguments() {
        Environment env = new Environment();
        AbstractSyntaxTree closure = evaluate(parse("(lambda (a) (+ a 5))"), env);
        AbstractSyntaxTree ast = list(closure, parse("(if #f 0 (+ 10 10))"));
        assertEquals(number(25), evaluate(ast, env));
    }

    /**
     * The body should be evaluated in the environment from the closure.
     *
     * The functions free variables, i.e. those not specified as part of the parameter list,
     * should be looked up in the environment from where the function was defined. This is
     * the environment included in the closure. Make sure this environment is used when
     * evaluating the body.
     */
    @Test
    public void testEvaluatingCallToClosureWithFreeVariables() {
        AbstractSyntaxTree closure = evaluate(parse("(lambda (x) (+ x y))"), new Environment(map(symbol("y"), number(1))));
        AbstractSyntaxTree ast = list(closure, number(0));
        assertEquals(number(1), evaluate(ast, new Environment(map(symbol("y"), number(2)))));
    }


    /**
     * Okay, now we're able to evaluate ASTs with closures as the first argument. But normally
     * the closures don't just happen to be there all by themselves. Generally we'll find some
     * expression, evaluate it to a closure, and then evaluate a new AST with the closure just
     * like we did above.
     *
     * (some-exp arg1 arg2 ...) -> (closure arg1 arg2 ...) -> result-of-function-call
     */

    /**
     * A call to a symbol corresponds to a call to its value in the environment.
     *
     * When a symbol is the first element of the AST list, it is resolved to its value in
     * the environment (which should be a function closure). An AST with the variables
     * replaced with its value should then be evaluated instead.
     */
    @Test
    public void testCallingVerySimpleFunctionInEnvironment() {
        Environment env = new Environment();
        evaluate(parse("(define add (lambda (x y) (+ x y)))"), env);
        assertTrue(env.lookup(symbol("add")) instanceof Closure);

        assertEquals(number(3), evaluate(parse("(add 1 2)"), env));
    }

    /**
     * It should be possible to define and call functions directly.
     *
     * A lambda definition in the call position of an AST should be evaluated, and then
     * evaluated as before.
     */
    @Test
    public void testCallingLambdaDirectly() {
        AbstractSyntaxTree ast = parse("((lambda (x) x) 42)");
        assertEquals(number(42), evaluate(ast, new Environment()));
    }

    /**
     * Actually, all ASTs that are not atoms should be evaluated and the called
     *
     * In this test, a call is done to the if-expression. The `if` should be evaluated,
     * which will result in a `lambda` expression. The lambda is evaluated, giving a
     * closure. The result is an AST with a `closure` as the first element, which we
     * already know how to evaluate.
     */
    @Test
    public void testCallingComplexExpressionWhichEvaluatesToFunction() {
        String program = "" +
                "((if #f" +
                "   wont-evaluate-this-branch" +
                "   (lambda (x) (+ x y)))" +
                " 2)";
        Environment env = env(map(symbol("y"), number(3)));
        assertEquals(number(5), evaluate(parse(program), env));
    }


    /**
     * Now that we have the happy cases working, let's see what should happen when
     * function calls are done incorrectly.
     */


    /**
     * A function call to a non-function should result in an error.
     */
    @Test
    public void testCallingAtomRaisesException() {
        assertException(LispException.class, () -> evaluate(parse("(#t foo bar)"), env()));
        assertException(LispException.class, () -> evaluate(parse("(42)"), env()));
    }

    /**
     * The arguments passed to a function should be evaluated.
     *
     * We should accept parameters that are produced through function calls.
     * If you are seeing stack overflows e.g java.lang.StackOverflowError, then you
     * should double-check that you are properly evaluating the passed
     * function arguments
     */
    @Test
    public void testMakeSureArgumentsToFunctionsAreEvaluated() {
        Environment env = env();
        String program = "((lambda (x) x) (+ 1 2))";
        assertEquals(number(3), evaluate(parse(program), env));
    }

    /**
     * Functions should raise an exceptions when called with wrong number of arguments.
     */
    @Test
    public void testCallingWithWrongNumberOfArguments() {
        Environment env = env();
        evaluate(parse("(define fn (lambda (p1 p2) 'whatever))"), env);
        assertException(LispException.class, () -> evaluate(parse("(fn 1 2 3)"), env));
    }

    /**
     * Calling nothing should fail (remember to quote empty data lists)
     */
    @Test
    public void testCallingNothing() {
        assertException(LispException.class, () -> evaluate(parse("()"), env()));
    }


    /**
     * One final test to see that recursive functions are working as expected.
     * The good news: this should already be working by now :)
     */


    /**
     * Tests that a named function is included in the environment
     * where it is evaluated
     */
    @Test
    public void testCallingFunctionRecursively() {
        Environment env = env();
        evaluate(parse("" +
                "(define my-fn" +
                "   ;; A meaningless, but recursive, function\n" +
                "   (lambda (x)" +
                "       (if (eq x 0)" +
                "           42" +
                "           (my-fn (- x 1)))))"), env);

        assertEquals(number(42), evaluate(parse("(my-fn 0)"), env));
        assertEquals(number(42), evaluate(parse("(my-fn 10)"), env));
    }
}
