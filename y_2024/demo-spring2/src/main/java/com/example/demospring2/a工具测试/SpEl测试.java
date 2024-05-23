package com.example.demospring2.a工具测试;

import com.alibaba.fastjson.JSONArray;
import org.springframework.expression.Expression;
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
        Expression expression = parser.parseExpression("aa is #{num[1]}", new TemplateParserContext());
        Object paramObj = new Object() {
          public int[] getNum() {
              return new int[]{0,1};
          }
        };
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("num", new int[]{0,1});
        System.out.println(expression.getValue(paramObj));

        System.out.println("------end 3-------");
    }

    public static void main(String[] args) {
        test1();
        test2();
        test3();
    }
}
