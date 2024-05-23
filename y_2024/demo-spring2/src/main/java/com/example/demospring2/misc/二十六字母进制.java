package com.example.demospring2.misc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-04-18
 */
@Slf4j
public class 二十六字母进制 {

    private static
    String[] digits = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    public static String num2word(int num) {
        StringBuilder sb = new StringBuilder();

        while (num > 0) {
            int remainder = num % 26;
            sb.insert(0, digits[remainder]);
            num = (num - remainder) / 26;
        }

        return sb.toString();
    }

    public static int word2num(String base26) {
        int[] digits = new int[base26.length()];
        for (int i = 0; i < base26.length(); i++) {
            char c = base26.charAt(i);
            digits[i] = c - 'a';
        }

        int num = 0;
        for (int i = 0; i < digits.length; i++) {
            num += digits[i] * Math.pow(26, digits.length - i - 1);
        }

        return num;
    }

    public static String test(String base26) {

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < base26.length(); i++) {
            char c = base26.charAt(i);
            int digit = c - 'a';
            result.append(Integer.toString(digit, 26));
        }

        String nextBase26Value;
        if (result.length() < 2) {
            nextBase26Value = "b";
        } else {
            int nextDigit = Integer.parseInt(result.substring(0, 2), 26) + 1;
            nextBase26Value = Integer.toString(nextDigit, 26);
            if (nextBase26Value.length() < 2) {
                nextBase26Value = "a" + nextBase26Value;
            }
        }

        StringBuilder finalResult = new StringBuilder();
        for (int i = 0; i < nextBase26Value.length(); i++) {
            char c = nextBase26Value.charAt(i);
            int digit = Integer.parseInt(String.valueOf(c), 26);
            finalResult.append((char) (digit + 'a'));
        }
        return finalResult.toString();
    }

    private static String getNextBase26Value(String base26Value, int n) {
        StringBuilder nextBase26Value = new StringBuilder();
        int length = base26Value.length();
        int start = Math.max(0, length - 2);
        int end = length;

        String subValue = base26Value.substring(start, end);
        int digit = Integer.parseInt(subValue, 26) + 1;
        nextBase26Value.append(Integer.toString(digit, 26));

        while (nextBase26Value.length() < n) {
            start = Math.max(0, start - 1);
            subValue = base26Value.substring(start, end);
            digit = Integer.parseInt(subValue, 26);
            nextBase26Value.insert(0, Integer.toString(digit, 26));
        }

        return nextBase26Value.toString();
    }

    public static void main(String[] args) {
        log.info("[num2word] {} -> {}", 20, num2word(20));
        log.info("[word2num] {} -> {}", "cd", word2num("cd"));
        log.info("[num2word] {} -> {}", 55, num2word(55));
        log.info("[word2num] {} -> {}", "u", word2num("u"));

//        System.out.println(getNextBase26Value("a", 2));

        System.out.println(test("a"));

    }

}
