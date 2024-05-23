package com.example.demospring2.json;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-06-01
 */
public class Test1 {

    public static void test1() {
        List<Integer> list = Arrays.asList(1, null, 2);
//        System.out.println(JSONObject.toJSONString(list));

        System.out.println(JSONUtil.toJsonStr(list));
        System.out.println(JSONUtil.toJsonStr(JSONUtil.parseArray(list, false)));
    }

    public static void main(String[] args) {
        test1();
    }
}
