package com.example.demospring2.c加权抽奖;

import cn.hutool.core.lang.Pair;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-03-03
 */
@Slf4j
public class Test1 {
    public static void main(String[] args) {
        int users=40000;
        int prices=3000;
        int count = 0;
        Map<Integer, Pair<Pair<Integer, Integer>, String>> candidate = new HashMap<>();
        for (int j = 0; j < users; j++) {
            int weight = new Random().nextInt(1000) + 1;
            for (int k = count; k < count + weight; k++) {
                candidate.put(k, Pair.of(Pair.of(count, count + weight - 1), "uid" + j));
            }
            count += weight;
        }
        int[] index = new int[count];
        Arrays.fill(index, 1);
        int left = count;
        List<String> selected = new ArrayList<>(3000);

        long start = System.currentTimeMillis();
        for (int i = 0; i < prices; i++) { // O(m*weight*N)
            int choice = new Random().nextInt(left)+1; // O(1)
            int sum = 0;
            for (int k = 0; k < count; k++) { // O(weight*N)
                sum += index[k];
                if (sum == choice) {
                    Pair<Pair<Integer, Integer>, String> pair = candidate.get(k);
                    selected.add(pair.getValue());
                    for (int s = pair.getKey().getKey(); s <= pair.getKey().getValue(); s++) { // O(weight)
                        index[s] = 0;
                    }
                    left-=pair.getKey().getValue()-pair.getKey().getKey()+1;
                    break;
                }
            }
        }
        System.out.println(System.currentTimeMillis() - start + "ms");
        System.out.println(selected.size() + "==" + new HashSet<>(selected).size());

    }


}
