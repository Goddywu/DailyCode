package com.example.demospring2.misc;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-02-28
 */
public class 正则 {

    public static void m1() {
        System.out.println(splitString("理想02"));
        System.out.println(splitString("理2想91"));
        System.out.println(splitString("理2想"));
        System.out.println(splitString("1221张"));
        System.out.println(splitString("1221"));
        System.out.println(splitString("沙发"));
    }

    public static List<String> splitString(String input) {
        // 定义正则表达式，匹配最后的数字
        Pattern pattern = Pattern.compile("(.*?)(\\d*)$");
        Matcher matcher = pattern.matcher(input);

        String letters = "";
        String numbers = "";

        // 如果匹配成功，则提取文字部分和数字部分
        if (matcher.find()) {
            letters = matcher.group(1);
            numbers = matcher.group(2);
        }

        return Arrays.asList(letters, numbers);
    }

    public static String m2(int num) {
        return String.format("%02d", num);
    }


    public static void main(String[] args) {
//        m1();
        System.out.println(m2(0));
        System.out.println(m2(1));
        System.out.println(m2(100));
    }
}
