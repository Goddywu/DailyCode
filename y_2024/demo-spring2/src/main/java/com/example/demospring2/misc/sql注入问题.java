package com.example.demospring2.misc;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-04-20
 */
public class sql注入问题 {

    public static String escapeSql(String sql) {
        if (sql == null || sql.isEmpty()) {
            return sql;
        }

        StringBuilder escapedSql = new StringBuilder();
        for (int i = 0; i < sql.length(); i++) {
            char c = sql.charAt(i);
            if (c == '\'' || c == '\"' || c == '\\' || c == '\0') {
                escapedSql.append('\\');
            }
            escapedSql.append(c);
        }

        return escapedSql.toString();
    }

    public static void main(String[] args) {

        System.out.println(escapeSql("pin'yin"));
    }
}
