package com.candidate.vishal.calculator.helper;

import com.candidate.vishal.calculator.exception.DivideByZeroException;
import com.candidate.vishal.calculator.exception.InvalidExpressionException;
import com.candidate.vishal.calculator.exception.InvalidOperationException;
import com.candidate.vishal.calculator.utils.Operation;
import org.apache.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class EvaluateExpression {
    private final static Logger log = Logger.getLogger(EvaluateExpression.class);

    public int evaluate(String expression) throws DivideByZeroException, InvalidOperationException, InvalidExpressionException {
        log.info("Computing & evaluating the expression for result... " + expression);
        expression = expression.toLowerCase();
        char[] tokens = expression.toCharArray();
        String op;

        // Stack for numbers
        Deque<Integer> values = new ArrayDeque<>();
        // Stack for operations
        Deque<String> ops = new ArrayDeque<>();
        // Map to store variables for let
        Map<String, Integer> variableMapForLet = new HashMap<>();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] == ' ' || tokens[i] == ',' || tokens[i] == '(') { // skip
                continue;
            }
            if (tokens[i] >= '0' && tokens[i] <= '9') { // Current token is a number, push it to stack for numbers
                log.debug("number found");
                StringBuilder sb = new StringBuilder();
                // There may be more than one digits in number
                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') {
                    sb.append(tokens[i++]);
                }
                Operation anOp = Operation.getEnumFromString(ops.peek());
                if(!ops.isEmpty() && anOp == null) {
                    variableMapForLet.put(ops.pop(), Integer.parseInt(sb.toString()));
                    ops.pop();
                } else {
                    values.push(Integer.parseInt(sb.toString()));
                }
                i = i - 1;
            } else if (tokens[i] == ')') {  // Closing brace encountered, solve entire expression
                log.debug("Ending paranthesis. Solve expression until here.");
                while (!ops.isEmpty() && values.size() >= 2) {
                    Operation anOp = Operation.getEnumFromString(ops.peek());
                    if(anOp == null) {
                        if(!values.isEmpty()) {
                            variableMapForLet.put(ops.pop(), values.peek());
                        }
                        ops.pop();
                    } else {
                        values.push(performOperation(ops.pop(), values.pop(), values.pop()));
                        if(ops.isEmpty()) {
                            continue;
                        }
                        anOp = Operation.getEnumFromString(ops.peek());
                        if(!ops.isEmpty() && anOp == null) {
                            variableMapForLet.put(ops.pop(), values.pop());
                            ops.pop();
                        }
                    }
                }
            } else if (Character.isLetter(tokens[i])) {  // Current token is a character
                log.debug("Current token is a character: " + tokens[i]);
                if(!ops.isEmpty() && Operation.isOperationLet(ops.peek())) {
                    while (!ops.isEmpty() && Operation.isOperationLet(ops.peek())) {
                        StringBuilder sb = new StringBuilder();
                        // There may be more than one characters
                        while (i < tokens.length && Character.isLetter(tokens[i])) {
                            sb.append(tokens[i++]);
                        }
                        ops.push(sb.toString());
                        i = i - 1;
                    }
                } else {
                    StringBuilder sb = new StringBuilder();
                    // There may be more than one characters
                    while (i < tokens.length && Character.isLetter(tokens[i])) {
                        sb.append(tokens[i++]);
                    }
                    op = sb.toString();
                    while (!ops.isEmpty() && op.equals("")) {
                        values.push(performOperation(ops.pop(), values.pop(), values.pop()));
                    }
                    if(variableMapForLet.containsKey(op)) {
                        values.push(variableMapForLet.get(op));
                    } else {
                        Operation anOperation = Operation.getEnumFromString(op);
                        log.debug("Adding operation to operation stack..." + anOperation.toString());
                        if(anOperation != null) {
                            switch (anOperation) {
                                case ADD:
                                case SUB:
                                case MULT:
                                case DIV:
                                case LET:
                                    ops.push(op);
                                    break;
                                default:
                                    throw new InvalidOperationException("No support");
                            }
                        }
                        i = i - 1;
                        if(anOperation == null && !variableMapForLet.isEmpty() && !variableMapForLet.containsKey(op)) {
                            throw new InvalidExpressionException("Variable in let not found...");
                        }
                    }
                }
            }
        }
        log.info("Result is: " + values.peek());
        return values.pop();
    }

    private int performOperation(String op, int b, int a) throws DivideByZeroException {
        Operation operation = Operation.getEnumFromString(op);
        switch (operation) {
            case ADD:
                return a + b;
            case SUB:
                return a - b;
            case MULT:
                return a * b;
            case DIV:
                if (b == 0) {
                    log.error("Cannot divide by zero error...");
                    throw new DivideByZeroException("Cannot divide by zero error...");
                }
                return a / b;
        }
        return 0;
    }
}
