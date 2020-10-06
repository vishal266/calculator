package com.candidate.vishal.calculator;

import com.candidate.vishal.calculator.exception.DivideByZeroException;
import com.candidate.vishal.calculator.exception.InvalidExpressionException;
import com.candidate.vishal.calculator.exception.InvalidOperationException;
import com.candidate.vishal.calculator.exception.ParanthesesMismatchException;
import com.candidate.vishal.calculator.helper.EvaluateExpression;
import com.candidate.vishal.calculator.helper.ValidateExpression;

import org.apache.log4j.Logger;

public class CalculatorMain {

    private final static Logger log = Logger.getLogger(CalculatorMain.class);

    public static void main(String[] args) {
        if(args == null || args.length == 0) {
            log.error("No input specified... Run program with arguments");
            throw new IllegalArgumentException("No input specified... Run program with arguments");
        }
        String exp = args[0];
        ValidateExpression validateExpression = new ValidateExpression();
        EvaluateExpression evaluateExpression = new EvaluateExpression();
        try {
            System.out.println("Input expression: " + exp);

            log.info("Validating the whole expression...");
            validateExpression.checkExpression(exp);
            log.info("Validation success!");

            log.info("Computing the expression for result...");
            int result = evaluateExpression.evaluate(exp);
            System.out.println("Output: " + result);
        } catch (InvalidOperationException | ParanthesesMismatchException | InvalidExpressionException | DivideByZeroException e) {
            e.printStackTrace();
        }
    }
}
