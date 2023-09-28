package com.example.demospring2.c最佳分流;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-02-15
 */
@Slf4j
@Data
public class Ntest1 {

    @Getter
    @Setter
    @AllArgsConstructor
    private static class Tuple {
        private int bossNum;
        private int totalMoney;

        @Override
        public String toString() {
            return "(" + bossNum + ", " + totalMoney + ")";
        }
    }

    private List<Tuple> tupleList;
    private double percent; // 需要多少，0.2

    public void distribute() {

        int sumMoney = 0;
        int sumBoss = 0;
        for (Tuple t : tupleList) {
            sumMoney += t.getTotalMoney();
            sumBoss += t.getBossNum();
        }
        double averageMoney = (double) sumMoney / sumBoss;
        double needBoss = (double) sumBoss * percent;
        double needMoney = needBoss * averageMoney;
        System.out.println(tupleList);
        System.out.println("--------");
        System.out.println(StrUtil.format(" 总金额 {}\n 总boss数 {}\n arpu {}\n 需要boss数 {}\n 需要金额 {}", sumMoney, sumBoss, averageMoney, needBoss, needMoney));
        System.out.println("--------");



    }


    public static void main(String[] args) {
        List<Tuple> tuples = Arrays.asList(
                new Tuple(4, 120),
                new Tuple(5, 200),
                new Tuple(2, 20),
                new Tuple(3, 150),
                new Tuple(3, 10),
                new Tuple(9, 300)
        );

        Ntest1 test = new Ntest1();
        test.setTupleList(tuples);
        test.setPercent(0.2d);

        test.distribute();

    }

}
