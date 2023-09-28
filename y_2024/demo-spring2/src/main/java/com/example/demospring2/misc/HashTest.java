package com.example.demospring2.misc;

import com.bosszp.itool.core.util.HashUtil;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-03-23
 */
public class HashTest {

    public static void test2() {
        LinkedHashMap<String, String> domainContexts = new LinkedHashMap<>();
        domainContexts.put("1", "a");
        domainContexts.put("2", "b");
        LinkedList<Map.Entry<String, String>> entryList = new LinkedList<>(domainContexts.entrySet());

        entryList.pollLast();

        System.out.println(domainContexts);
    }

    public static void main(String[] args) {
//        // 4 , 3
//        System.out.println(HashUtil.getIdxOfValue(123456789, 2, 100));
//        // 4 , 4
//        System.out.println(HashUtil.getIdxOfValue(123456789, 3, 10));
//        // 4 , 2
//        System.out.println(HashUtil.getIdxOfValue(123456789, 1, 1000));

        test2();
    }
}
