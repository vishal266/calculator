package com.candidate.vishal.calculator.helper;


import com.candidate.vishal.calculator.exception.*;
import com.candidate.vishal.calculator.utils.Operation;
import org.apache.log4j.Logger;

public class ValidateExpression {

    private final static Logger log = Logger.getLogger(ValidateExpression.class);

    private boolean checkMatchedParantheses(String expression) throws ParanthesesMismatchException {
        log.debug("Validating parantheses in the expression...");
        int count = 0;
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '(') {
                count++;
            } else if (expression.charAt(i) == ')') {
                if(count > 0) {
                    count--;
                } else {
                    throw new ParanthesesMismatchException("Missing number of matching parantheses. Check your expression!");
                }
            }
        }
        log.debug("Parantheses check done...");
        return count == 0;
    }

    public void checkExpression(String expression) throws InvalidOperationException, ParanthesesMismatchException, InvalidExpressionException {
        if(expression == null || expression.isEmpty()) {
            log.error("Expression invalid/empty...");
            throw new InvalidExpressionException("Expression invalid/empty...");
        }
        log.debug("validating expression: " + expression);
        expression = expression.trim();
        expression = expression.toLowerCase();
        String anOp;
        Operation operation = null;
        if(expression.startsWith(Operation.MULT.toString())) {
            anOp = expression.substring(0, 4);
            operation = Operation.getEnumFromString(anOp);
        } else if(expression.startsWith(Operation.ADD.toString()) || expression.startsWith(Operation.SUB.toString())
                || expression.startsWith(Operation.DIV.toString()) || expression.startsWith((Operation.LET.toString()))) {
            anOp = expression.substring(0, 3);
            operation = Operation.getEnumFromString(anOp);
        }
        if(operation != null) {
            log.debug("Operation valid: " + operation.toString());
            switch (operation) {
                case ADD:
                case SUB:
                case MULT:
                case DIV:
                    checkExpressionForNonLet(expression, operation);
                    break;
                case LET:
                    checkExpressionForLet(expression, operation);
                    break;
                default:
                    throw new InvalidOperationException("unknown oper");
            }
        } else {
            if(!expression.matches("[a-zA-z]+") && !isANumber(expression)) {
                log.error("Unsupported operation... Calculator supports only ADD, SUB, MULT, DIV and LET.");
                throw new InvalidOperationException("Unsupported operation... Calculator supports only ADD, SUB, MULT, DIV and LET.");
            }
        }

        if (!checkMatchedParantheses(expression)) {
            log.error("Parantheses not matching.");
            throw new ParanthesesMismatchException("Parantheses not matching.");
        }
    }

    private void checkExpressionForNonLet(String expression, Operation op) throws InvalidOperationException,
            ParanthesesMismatchException, InvalidExpressionException {
        log.debug("Check the expression for regular operations like add, sub, mult, div..." + expression);
        checkBeginningParanthesis(expression, op.toString().length());
        int commaPos = getPosAfterParanthesesValidation(expression, op.toString().length() + 1, ',');
        String expression1 = expression.substring(op.toString().length() + 1, commaPos);
        checkExpression(expression1);

        int endPos = getPosAfterParanthesesValidation(expression, commaPos + 1, ')');
        String expression2 = expression.substring(commaPos + 1, endPos);
        checkExpression(expression2);
        log.debug("Express check done for regular operations");
    }


    private void checkExpressionForLet(String expression, Operation op) throws InvalidExpressionException, InvalidOperationException, ParanthesesMismatchException {
        checkBeginningParanthesis(expression, op.toString().length());
        log.debug("Check the expression for let operations.." + expression);
        int commaFirstPos = getPosAfterParanthesesValidation(expression, op.toString().length() + 1, ',');
        String varName = expression.substring(op.toString().length() + 1, commaFirstPos);
        checkExpression(varName);

        int commaSecondPos = getPosAfterParanthesesValidation(expression, commaFirstPos + 1, ',');
        String valueExprName = expression.substring(commaFirstPos + 1, commaSecondPos);
        checkExpression(valueExprName);

        int endPos = getPosAfterParanthesesValidation(expression, commaSecondPos + 1, ')');
        String exprName = expression.substring(commaSecondPos + 1, endPos);
        checkExpression(exprName);
    }

    private void checkBeginningParanthesis(String expression, int prefix) throws InvalidExpressionException {
        if (!expression.startsWith("(", prefix)) {
            log.error("No beginning parantheses found. Invalid expression!");
            throw new InvalidExpressionException("No beginning parantheses found. Invalid expression!");
        }
    }

    private boolean isANumber(String expression) {
        log.debug("Validate for number: " + expression);
        String s = expression;
        if (expression.startsWith("-")) {
            s = expression.substring(1);
        }
        for (Character c: s.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private int getPosAfterParanthesesValidation(String expression, int pre, char delimiter) {
        log.debug("Expression = " + expression);
        int i = 0;
        try {
            int count = 0;
            for (i=pre; i<expression.length(); i++) {
                if (count == 0 && expression.charAt(i) == delimiter) {
                    return i;
                }
                if (expression.charAt(i) == '(') {
                    count++;
                }
                if (expression.charAt(i) == ')') {
                    if(count == 0) {
                        throw new ParanthesesMismatchException("No matching parantheses found...");
                    }
                    count--;
                }
            }
            if(count > 0) {
                throw new ParanthesesMismatchException("No matching parantheses found...");
            }
        } catch (Exception e){
            log.error(e.toString());
        }
        return i;
    }
}
