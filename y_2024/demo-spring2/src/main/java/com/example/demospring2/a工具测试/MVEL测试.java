package com.example.demospring2.a工具测试;

import com.google.common.collect.ImmutableMap;
import org.mvel2.MVEL;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-05-27
 */
public class MVEL测试 {

    public static String execMVEL(String expression, Map<String, Object> variables) {
        Serializable serializable = MVEL.compileExpression(expression);

        return MVEL.executeExpression(serializable, variables, String.class);
    }

    public static void main(String[] args) {
        System.out.println("\033[45m" + "你好" + "\u001B[0m");
        System.out.println("\033[45m" + "你好" + "\033[0m");
        System.out.println("\033[44m" + "你好" + "\033[0m");
        System.out.println(execMVEL("a * 2 > 0", ImmutableMap.of("a", "1")));
        System.out.println(execMVEL("a * 2", ImmutableMap.of("a", "1")));
        System.out.println(execMVEL("b * 2 ", ImmutableMap.of("a", "1")));

    }
}
