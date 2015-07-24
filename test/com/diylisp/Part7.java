package com.diylisp;

import com.diylisp.model.AbstractSyntaxTree;
import com.diylisp.model.Bool;
import com.diylisp.model.Environment;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static com.diylisp.Interpreter.interpret;
import static com.diylisp.model.Environment.env;
import static junit.framework.TestCase.assertEquals;

public class Part7 {

    private Environment env = env();
    private String path = System.getProperty("user.dir") + File.separator + "stdlib.diy";

    @Before
    public void TestSomething() {
        AbstractSyntaxTree ast = Interpreter.interpretFile(path, env);
    }

    @Test
    public void TestNot() {
        assertEquals(Bool.False, interpret("(not #t)", env));
        assertEquals(Bool.True, interpret("(not #f)", env));
    }

}
