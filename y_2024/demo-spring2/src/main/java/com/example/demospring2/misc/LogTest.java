package com.example.demospring2.misc;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-10-27
 */
@Slf4j
public class LogTest {

    public static void method1() {
        method2();
    }

    public static void method2() {
        method3();
    }

    public static void method3() {
        printCallStack();
    }

    public static void printCallStack() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 1; i < stackTrace.length; i++) {
            StackTraceElement element = stackTrace[i];
            System.out.println("\tat " + element.getClassName() + "." + element.getMethodName()
                    + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
        }
    }

    public static void main(String[] args) {
        method1();
    }
}
