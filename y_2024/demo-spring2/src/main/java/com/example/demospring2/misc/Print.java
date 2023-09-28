package com.example.demospring2.misc;

import cn.hutool.core.lang.Pair;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-01-10
 */
public class Print {

    private final static ScriptEngine jsEngine = new ScriptEngineManager().getEngineByName("js");

    public static String evalJs(String jsScript) {
        try {
            return jsEngine.eval(jsScript).toString();
        } catch (ScriptException e) {
            return null;
        }
    }

    private static String findMostOne(List<String> strList) {
        if (strList.isEmpty()) {
            return null;
        }
        Map<String, Integer> m = new HashMap<>();
        for (String str : strList) {
            m.compute(str, (k, v) -> {
                if (v == null) {
                    v = 0;
                }
                return v + 1;
            });
        }

        Integer max = null;
        String mostOne = null;
        for (Map.Entry<String, Integer> e : m.entrySet()) {
            if (max == null || max < e.getValue()) {
                max = e.getValue();
                mostOne = e.getKey();
            }
        }
        return mostOne;
    }

    @Data
    @AllArgsConstructor
    private static class SubWordInfo {
        private String domain;
        private String intent;
        private List<Pair<String, String>> slotList;
    }

    public final static Comparator<SubWordInfo> sortSubWord = Comparator
            .<SubWordInfo>comparingInt(subWordInfo -> subWordInfo.getSlotList().size())
            .reversed()
            .thenComparing(subWordInfo -> {
                // 按照 intent=adjust_pos 和 intent=adjust_neg 进行排序
                if ("adjust_pos".equals(subWordInfo.getIntent())) {
                    return 0;
                } else if ("adjust_neg".equals(subWordInfo.getIntent())) {
                    return 1;
                } else {
                    return 2; // 如果有其他情况，按照默认值排序
                }
            });

    private static final String AT_EMAIL_REGEX = "#\\{[\\w|@|.| ]+}";

    private static String fillEmailForMarkdown(String text) {
        Pattern compile = Pattern.compile(AT_EMAIL_REGEX);
        Matcher matcher = compile.matcher(text);
        StringBuilder sb = new StringBuilder();
        int lastInd = 0;// 记录正则匹配到的最后一个邮箱的末尾索引
        while (matcher.find()) {
            sb.append(text, lastInd, matcher.start());
            lastInd = matcher.end();
            String parseStr = text.substring(matcher.start(), matcher.end());
            String emailStr = parseStr.substring(2, parseStr.length() - 1).trim();
            String atUserName = String.format("<at email=\"%s\"></at>", emailStr);
            sb.append(atUserName);
        }
        sb.append(text, lastInd, text.length());
        return sb.toString();
    }

    public static void main(String[] args) {
//        System.out.println(String.format("%+.2f", 0.001d));
//        System.out.println(String.format("%+.2f", -0.001d));
//        double a = (264 - 1782d/3) * 100 / (1782/3);
//        System.out.println(a);

//        String regex = "[。，]";
//        String[] split = "你好啊，中国。早安".split(regex);
//        System.out.println(split);
//        String[] split = "[1,2]".replaceAll("[\\[\\]]", "").split(",");
//        String[] split2 = "num".replaceAll("[\\[\\]]", "").split(",");
//        System.out.println(Arrays.asList("[1,2]".split("[,]")));

//        Pattern numberPattern = Pattern.compile("\\d+");
//        Pattern pattern = Pattern.compile("(\\d+上下)");
//        Matcher matcher = pattern.matcher("你30上下好，我20上下");
//        int idx = 0;
//        while (matcher.find()) {
//            String group = matcher.group();
//            System.out.println(group);
//            numberPattern.matcher(group).find();
//            String num = numberPattern.matcher(group).group(0);
//            System.out.println("num: " + num);
//            idx++;
//        }

//        Matcher matcher1 = numberPattern.matcher("30上下");
//        matcher1.find();
//        System.out.println(matcher1.group(0));
//        Matcher matcher1 = numberPattern.matcher("30上下");
//        while (matcher1.find()) {
//            System.out.println(matcher1.group(0));
//        }

//        System.out.println(evalJs("2+3"));

//        System.out.println(findMostOne(Arrays.asList("1", "2", "2")));

//        JSONObject jsonObject = JSONObject.parseObject("{1: 'adjust_pos', 2: 'adjust_neg', 3: 'query', 0: 'unknown'}");

//        String[] strArray = new String[]{"1", "2"};
//        List<String> ans = new ArrayList<>(strArray.length);
//        ans.addAll(Arrays.asList(strArray));

//        List<SubWordInfo> subWordInfoList = new ArrayList<>();
//        subWordInfoList.add(new SubWordInfo("a", "adjust_neg", Arrays.asList(Pair.of("1", "1"))));
//        subWordInfoList.add(new SubWordInfo("b", "adjust_pos", Arrays.asList(Pair.of("1", "1"))));
//        subWordInfoList.add(new SubWordInfo("c", "adjust_neg", Arrays.asList(Pair.of("1", "1"), Pair.of("1", "1"))));
//        subWordInfoList.add(new SubWordInfo("d", "adjust_pos", Arrays.asList(Pair.of("1", "1"), Pair.of("1", "1"))));
//        SubWordInfo subWordInfo = subWordInfoList.stream().min(sortSubWord).get();

//        List<String> stringList = Arrays.asList("1");
//
//        stringList = new ArrayList<>(stringList);
//        stringList.add("2");
//        System.out.println(stringList);

//        String a = "{\"areaItemOptional\":null,\"areaListOptional\":null,\"areaOptional\":null,\"bossId\":49332879,\"domainContextMap\":{},\"domainOptional\":null,\"intentOptional\":null,\"jobId\":0,\"jobIdOptional\":null}";

//        JSONObject obj = JSONObject.parseObject("{\"nums\":\"[0, \"inf\"]\"}");
//        JSONObject obj = JSONObject.parseObject("{\"nums\":\"[0, inf]\"}");
//        List<String> stringList = JSONArray.parseArray(obj.getString("nums"), String.class);

//        System.out.println((double) 1 / 2);


        System.out.println(fillEmailForMarkdown("你好#{caijian@kanzhun.com}hhh#{goddy@kanzhun.com}"));


        System.out.println("--- end ---");
    }
}
