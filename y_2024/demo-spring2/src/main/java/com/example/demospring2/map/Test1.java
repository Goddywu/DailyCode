package com.example.demospring2.map;

import java.util.*;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-02-15
 */
public class Test1 {
    public static void test1() {

        HashMap<String, Integer> map = new HashMap<>();
        map.put("111", 1);

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            entry.setValue(2);
        }

        System.out.println(map);
    }

    public static void test2() {
        Map<String, List<Integer>> map = new HashMap<>();
        map.computeIfAbsent("1", k -> new ArrayList<>()).add(1);
        map.computeIfAbsent("1", k -> new ArrayList<>()).add(2);
        System.out.println(map);
    }

    public static void test3() {
        Map<String, Integer> map = new HashMap<>();
        map.computeIfAbsent("1", k -> 0);
    }

    private static Map<Long, Integer> allotNum(List<Long> idList, int num) {
        Map<Long, Integer> idNumMap = new HashMap<>();
        for (long id : idList) {
            idNumMap.put(id, 0);
        }
        int accountNumCopy = num;
        int i = 0;
        while (accountNumCopy > 0) {
            long bossId = idList.get(i);
            idNumMap.put(bossId, idNumMap.getOrDefault(bossId, 0) + 1);
            accountNumCopy--;
            i++;
            if (i == idList.size()) {
                i = 0;
            }
        }
        return idNumMap;
    }

    public static void test4() {
        System.out.println(allotNum(Arrays.asList(1L, 2L, 3L), 1));
        System.out.println(allotNum(Arrays.asList(1L, 2L, 3L), 2));
        System.out.println(allotNum(Arrays.asList(1L, 2L, 3L), 10));
        System.out.println(allotNum(Arrays.asList(), 1));
    }


    public static void main(String[] args) {

//        test3();
        test4();

//        test2();
//        Map<Integer, Integer> map = new HashMap<>();
//        if (Objects.equals(map.get(1), 2)) {
//            System.out.println(333);
//        }
//        if (map.get(1) == 2) {
//            System.out.println(12);
//        }
    }
}
