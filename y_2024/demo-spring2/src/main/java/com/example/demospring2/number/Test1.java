package com.example.demospring2.number;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-02-17
 */
public class Test1 {

    public static void main(String[] args) {
//        BigDecimal a = new BigDecimal(-2);
//        if (a.compareTo(BigDecimal.ZERO) != 0) {
//            System.out.println(1);
//        }

        String a = Arrays.asList().stream().filter(Objects::nonNull).map(v -> "'" + v + "'").collect(Collectors.joining(","));
        System.out.println(11);
    }
}
