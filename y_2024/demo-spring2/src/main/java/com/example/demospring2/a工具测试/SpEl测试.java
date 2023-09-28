package com.example.demospring2.a工具测试;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-06-06
 * https://docs.spring.io/spring-framework/docs/3.0.x/reference/expressions.html
 * https://blog.csdn.net/u012060033/article/details/131220323
 */
public class SpEl测试 {

    public static void test1() {
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("{#num[0],#num[1]}");

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("num", new int[]{0,1});

        String value = expression.getValue(context).toString();
        List<Integer> javaList = JSONArray.parseArray(value).toJavaList(Integer.class);
        System.out.println(javaList);
        System.out.println("------end 1-------");
    }

    public static void test2() {
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("aa is #{[num][0] + 20}, #{[num][1] + 20}", new TemplateParserContext());
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("num", new int[]{0,1});
        System.out.println(expression.getValue(paramsMap));
        System.out.println("------end 2-------");
    }

    public static void test3() {
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("aa is #{#num[1]}", new TemplateParserContext());
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setVariable("num", new int[]{0,1});
        System.out.println(expression.getValue(context));

        System.out.println("------end 3-------");
    }

    public static void test4() {
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("aa is #{num[1]}", new TemplateParserContext());
        Object paramObj = new Object() {
            public int[] num() { // 或者 getNum()
                return new int[]{0,1};
            }
        };
        System.out.println(expression.getValue(paramObj));

        System.out.println("------end 4-------");
    }

    public static void test5() {
        JSONObject json = new JSONObject();
        json.put("name", "John");
        json.put("age", 30);
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext(json);
        context.addPropertyAccessor(new MapAccessor());
        System.out.println(parser.parseExpression("name").getValue(context, String.class));

        System.out.println("------end 5-------");
    }

    public static void test6() {
        ExpressionParser parser = new SpelExpressionParser();

        Expression expression = parser.parseExpression("aa is #{num[1] + 20}", new TemplateParserContext());

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.addPropertyAccessor(new MapAccessor());
        JSONObject jsonObject = new JSONObject().fluentPut("num", new int[]{0,1});
        context.setRootObject(jsonObject);
        System.out.println(expression.getValue(context));

        System.out.println("------end 6-------");
    }

    public static void test7() {
        ExpressionParser parser = new SpelExpressionParser();

        Expression expression = parser.parseExpression("aa is #{num[1] + 20}", new TemplateParserContext());

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.addPropertyAccessor(new MapAccessor());
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("num", new int[]{0,1});
        context.setRootObject(objectMap);
        System.out.println(expression.getValue(context));

        System.out.println("------end 7-------");
    }

    public static void test8() {
        ExpressionParser parser = new SpelExpressionParser();

        Expression expression = parser.parseExpression("[#{num[0]},#{num[1]}]", new TemplateParserContext());

        StandardEvaluationContext context = new StandardEvaluationContext();
//        context.setVariable("num", new int[]{0,1});
        context.addPropertyAccessor(new MapAccessor());
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("num", new int[]{0,1});
        context.setRootObject(objectMap);
        System.out.println(expression.getValue(context));

        System.out.println("------end 8-------");
    }

    public static void test9() {
        ExpressionParser parser = new SpelExpressionParser();

        Expression expression = parser.parseExpression("[#{num[1]}, #{num[1].doubleValue() / 1000}]", new TemplateParserContext());

        StandardEvaluationContext context = new StandardEvaluationContext();
//        context.setVariable("num", new int[]{0,1});
        context.addPropertyAccessor(new MapAccessor());
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("num", new int[]{0,1});
        context.setRootObject(objectMap);
        System.out.println(expression.getValue(context));

        System.out.println("------end 9-------");
    }

    public static void test10() {
        ExpressionParser parser = new SpelExpressionParser();

        Expression expression = parser.parseExpression("[#{num[0]*24},inf]", new TemplateParserContext());

        StandardEvaluationContext context = new StandardEvaluationContext();
//        context.setVariable("num", new int[]{0,1});
        context.addPropertyAccessor(new MapAccessor());
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("num", new int[]{3,});
        context.setRootObject(objectMap);
        System.out.println(expression.getValue(context));

        System.out.println("------end 9-------");
    }

    public static void main(String[] args) {
//        test1();
//        test2();
//        test3();
//        test4();
//        test5();
//        test6();
//        test7();
//        test8();
//        test9();
        test10();
    }

}
