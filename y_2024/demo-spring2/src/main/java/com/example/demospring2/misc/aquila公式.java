package com.example.demospring2.misc;

import java.util.Stack;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-12-06
 */
public class aquila公式 {

    public static boolean isFormulaValid(String formula) {
        Stack<Character> stack = new Stack<>();

        for (char ch : formula.toCharArray()) {
            if (isOpeningBracket(ch) || isUpperCaseLetter(ch)) {
                stack.push(ch);
            } else if (isClosingBracket(ch)) {
                if (stack.isEmpty() || !isMatchingPair(stack.pop(), ch)) {
                    return false;
                }
            } else if (isLogicalOperator(ch)) {
                if (stack.isEmpty() || !isUpperCaseLetter(stack.peek())) {
                    return false;
                }
                // Skip the logical operator characters
                stack.pop();
            }
        }

        return stack.isEmpty();
    }

    private static boolean isOpeningBracket(char ch) {
        return ch == '(';
    }

    private static boolean isClosingBracket(char ch) {
        return ch == ')';
    }

    private static boolean isLogicalOperator(char ch) {
        return ch == 'a' || ch == 'o';
    }

    private static boolean isUpperCaseLetter(char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    private static boolean isMatchingPair(char open, char close) {
        return (open == '(' && close == ')');
    }

    public static void main(String[] args) {
        String formula1 = "(A and B) or (C and D)";
        String formula2 = "A and B) or (C and D";
        String formula3 = "(A and B or (C and D)";
        String formula4 = "(A and B) or C and D)";

        System.out.println(formula1 + ": " + isFormulaValid(formula1));
        System.out.println(formula2 + ": " + isFormulaValid(formula2));
        System.out.println(formula3 + ": " + isFormulaValid(formula3));
        System.out.println(formula4 + ": " + isFormulaValid(formula4));
    }
}
