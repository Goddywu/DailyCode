package com.example.demospring2.misc;

import cn.hutool.core.lang.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-02-24
 */
public class Boundary {

    public static void main(String[] args) {
        List<Pair<Integer, Integer>> boundaryList = getBoundary(1000, 100);
        boundaryList.forEach(boundary -> {
            System.out.println(boundary.getKey() + " | " + boundary.getValue());
        });
        System.out.println("'---------------'");
        boundaryList = getBoundary(21, 4);
        boundaryList.forEach(boundary -> {
            System.out.println(boundary.getKey() + " | " + boundary.getValue());
        });
        System.out.println("'---------------'");
        boundaryList = getBoundary(3, 4);
        boundaryList.forEach(boundary -> {
            System.out.println(boundary.getKey() + " | " + boundary.getValue());
        });
    }

    private static List<Pair<Integer, Integer>> getBoundary(int totalSize, int maxPieceSize) {
        List<Pair<Integer, Integer>> boundaries = new ArrayList<>();
        if (totalSize <= maxPieceSize) {
            return Collections.singletonList(Pair.of(0, totalSize));
        }

        int divideNum = (int) Math.ceil((double) totalSize / maxPieceSize);
        int onePieceNum = Math.floorDiv(totalSize, divideNum);
        int left = totalSize % divideNum;
        int from = 0;
        int to = onePieceNum;
        if (left > 0) {
            to++;
            left--;
        }
        for (int i = 0; i < divideNum; i++) {
            boundaries.add(Pair.of(from, to));

            from = to;
            to = Math.min(onePieceNum + to, totalSize);
            if (i != divideNum - 1 && left > 0) {
                to++;
                left--;
            }
        }
        return boundaries;
    }
}
