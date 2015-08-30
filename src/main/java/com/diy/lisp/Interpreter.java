package com.diy.lisp;

import com.diy.lisp.model.AbstractSyntaxTree;
import com.diy.lisp.model.Environment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.diy.lisp.Evaluator.evaluate;
import static com.diy.lisp.Parser.parse;

public class Interpreter {

    /**
     * Interpret a lisp program statement.
     *
     * Accepts a program statement as a string, interprets it, and then
     * returns the resulting lisp expression as a string.
     */
    public static String interpret(String source, Environment env) {
        return evaluate(parse(source), env).toString();
    }

    /**
     * Interpret a lisp file.
     *
     * Accepts the name of a lisp file containing a series of statements.
     * Returns the value of the last expression of the file.
     */
    public static AbstractSyntaxTree interpretFile(String path, Environment env) {
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(path)));
            List<AbstractSyntaxTree> parsed = Parser.parseMultiple(fileContent);
            parsed.stream()
                .forEach((exp) -> Evaluator.evaluate(exp, env));

            return parsed.get(parsed.size() - 1);
        } catch (IOException e) {
            System.out.println(String.format("Fatal error while trying to read contents of %s as string", path));
            e.printStackTrace();
        }

        return null;
    }
}
