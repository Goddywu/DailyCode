package com.example.demospring2.misc;

import com.bosszp.itool.core.util.HashUtil;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-04-27
 */
public class 分流 {
    public static long getIdx(int idx, int lastIdxFrom, int lastIdxTo) {
        int offset = lastIdxTo - 1;
        int mod = (int) Math.pow(10, (lastIdxFrom - lastIdxTo) + 1);
        long finalIdx = HashUtil.getIdxOfValue(idx, offset, mod);
        return finalIdx;
    }


    public static void main(String[] args) {
        System.out.println(getIdx(1053800448, 2, 1));
        System.out.println(getIdx(123456789, 2, 1));
        System.out.println(getIdx(123456789, 3, 1));
        System.out.println(getIdx(123456789, 3, 2));
        System.out.println(getIdx(123456789, 4, 3));
    }
}
