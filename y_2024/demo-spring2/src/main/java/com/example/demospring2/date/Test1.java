package com.example.demospring2.date;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-06-08
 */
public class Test1 {

    public static void test1() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime curDate = LocalDateTime.now().withSecond(0).withHour(0).withMinute(0).withNano(0);
        LocalDateTime parse = LocalDateTime.parse("2023-06-07 17:48:45", df).withSecond(0).withHour(0).withMinute(0).withNano(0);

        System.out.println(Duration.between(parse, curDate).toDays());
    }

    public static void test2() {
        String startDs = "2023-08-01";
        String endDs = "2024-02-10";

        List<String> dsList = new ArrayList<>();
        DateTime curDate = DateUtil.parseDate(startDs).setField(DateField.DAY_OF_MONTH, 1);
        DateTime endDate = DateUtil.parseDate(endDs);
        while (curDate.isBeforeOrEquals(endDate)) {
            dsList.add(curDate.toString("yyyy-MM"));
            curDate = curDate.offset(DateField.MONTH, 1);
        }

        dsList.forEach(System.out::println);
    }

    public static void main(String[] args) {

        test2();
    }
}
