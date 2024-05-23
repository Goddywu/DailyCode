package com.example.demospring2.misc;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-03-12
 */
public class 日期 {


    public static int gotLeftWeekDay(String ds) {
        Date date = DateUtil.parseDate(ds);
        return (int) DateUtil.endOfWeek(date).between(date, DateUnit.DAY) + 1;
    }

    public static void t1() {
        System.out.println(gotLeftWeekDay(DateUtil.today()));
        System.out.println(gotLeftWeekDay("2024-03-24"));
        System.out.println(gotLeftWeekDay("2024-03-25"));
    }

    public static void main(String[] args) {
//        t1();
        System.out.println(LocalDate.now());
    }
}
