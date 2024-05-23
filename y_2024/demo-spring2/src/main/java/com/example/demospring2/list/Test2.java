package com.example.demospring2.list;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bosszp.itool.core.util.HashUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-05-04
 */
public class Test2 {

    private static void insertAtPositionOrEnd(List<String> list, int index, String str) {
        list.add(Math.min(index, list.size()), str);
    }

    private static void test1() {
//        List<String> list = new ArrayList<>();
//        list.add(Math.min(2, list.size()), "2");
//        System.out.println(list);

        List<String> list2 = new ArrayList<>();
        list2.add("0");
        list2.add("1");
//        list2.add("2");
//        list2.add("3");
//        list2.add("4");
        list2.add(Math.min(0, list2.size()), "x");
        System.out.println(list2);




    }

    private static void test2() {
//        JSONObject jsonObject = JSONUtil.createObj()
//                .set("nums", Arrays.asList(1, 2));
//        String numsStr = jsonObject.getStr("nums");
//        List<Integer> nums = Convert.convert(new TypeReference<List<Integer>>() {
//        }, numsStr);


//        String a = "[[994,993,992,991,990,989,988],[372,373]]";
//
//        List<List<Integer>> convert = Convert.convert(new TypeReference<List<List<Integer>>>() {
//        }, a);

//        System.out.println(111);
        System.out.println(JSONUtil.toList("[1,2,3]", Integer.class));
    }

    private static void test3() {
        List<Integer> idList = Arrays.asList(1);
        int page = 0;

        int MAX_SIZE = 1000;
        int maxPage = (idList.size() + MAX_SIZE - 1) / MAX_SIZE;

        int start = page * MAX_SIZE;
        int end = Math.min(idList.size(), (page + 1) * MAX_SIZE);
        System.out.println(idList.subList(start, end));
    }

    public static void main(String[] args) {
//        test2();
//        test1();
//        System.out.println(HashUtil.getIdxOfValue(123456, 100));
//        System.out.println(Arrays.asList(2, 1, 1, 2).stream().distinct().limit(3).collect(Collectors.toList()));

        test3();
    }
}
