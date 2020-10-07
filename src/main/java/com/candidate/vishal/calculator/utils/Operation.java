package com.candidate.vishal.calculator.utils;

/**
 * Enum to capture operations from the expression string
 */
public enum Operation {
    ADD("add"),
    SUB("sub"),
    MULT("mult"),
    DIV("div"),
    LET("let");

    private final String operation;

    Operation(String operation) {
        this.operation = operation;
    }

    public static boolean isOperationLet(String anOperation) {
        return LET.toString().equalsIgnoreCase(anOperation);
    }

    public String toString() {
        return this.operation;
    }

    public static Operation getEnumFromString(String string) {
        if(string != null ) {
            try {
                return Enum.valueOf(Operation.class, string.trim().toUpperCase());
            } catch(IllegalArgumentException ex) {
            }
        }
        return null;
    }
}
