package com.example.demospring2.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-01-17
 */
@Slf4j
public class LogUtil {

    private static boolean openPointLog = true;

    public static void setUsername(String username) {
        MDC.put("username", username);
    }

    public static void setTraceId(String traceId) {
        MDC.put("traceId", traceId);
    }

    public static String getTraceId() {
        return MDC.get("traceId");
    }

    public static String getUsername() {
        return MDC.get("username");
    }

    public static void removeTraceId() {
        MDC.remove("traceId");
    }

    public static void removeUsername() {
        MDC.remove("username");
    }

    public static void printPointLog(String format, Object... arguments) {
        if (openPointLog) {
            log.info(format, arguments);
        }
    }

    public static void printPointLog(String msg) {
        if (openPointLog) {
            log.info(msg);
        }
    }
}
