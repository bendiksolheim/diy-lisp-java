package com.diy.lisp;

import com.diy.lisp.exception.LispException;
import com.diy.lisp.exception.ParseException;
import com.diy.lisp.model.Environment;

import java.io.File;
import java.util.Scanner;

import static com.diy.lisp.Interpreter.interpret;
import static com.diy.lisp.Interpreter.interpretFile;

public class Repl {

    static String path = System.getProperty("user.dir") + File.separator + "stdlib.diy";
    static Scanner scanner = new Scanner(System.in);

    public static void print(String s) {
        System.out.println(s);
    }

    /**
     * Reads from stdin until we have at least one s-expression
     * @return complete s-expression as String
     */
    public static String readExpression() {
        String exp = "";
        int openParens = 0;
        String line = "";
        while (true) {
            if ("".equals(exp))
                line = readLine("→  ");
            else
                line = readLine("…  ");
            openParens += countOpenParens(line);
            exp += line;

            if (openParens <= 0)
                break;
        }

        return exp;
    }

    public static String readLine(String prompt) {
        System.out.print(prompt);
        String line = scanner.nextLine();
        return Parser.removeComments(line += "\n");
    }

    public static int countOpenParens(String s) {
        int length = s.length();
        return (length - s.replace("(", "").length()) - (length - s.replace(")", "").length());
    }

    public static void main(String[] args) {
        print("");
        print("      Welcome to      ");
        print("     the DIY-lisp     ");
        print("         REPL         ");
        print("");
        print(" use ^D to exit");
        print("");

        Environment env = new Environment();
        interpretFile(path, env);
        String source = "";

        while(true) {
            try {
                source = readExpression();
                print(interpret(source, env));
            } catch (ParseException p) {
                print("ParseException: " + p.getMessage());
            } catch (LispException l) {
                print("LispException: " + l.getMessage());
            } catch (Exception e) {
                print("Java is showing through..");
                e.printStackTrace();
            }
        }
    }
}
