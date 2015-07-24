package com.diylisp;

import com.diylisp.model.AbstractSyntaxTree;
import com.diylisp.model.Environment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.diylisp.Evaluator.evaluate;
import static com.diylisp.Parser.parse;

public class Interpreter {

    public static AbstractSyntaxTree interpret(String source, Environment env) {
        return evaluate(parse(source), env);
    }

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
