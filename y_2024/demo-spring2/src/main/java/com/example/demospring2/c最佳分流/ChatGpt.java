//package com.example.demospring2.c最佳分流;
//
//import java.util.*;
//
///**
// * @author Goddy <wuchuansheng@kanzhun.com> 2023-02-15
// */
//public class ChatGpt {
//    public static class CompanyGroup {
//        // ARPU数据
//        private Map<Integer, Double> arpuData;
//        // 各个公司的boss数量
//        private Map<Integer, Integer> bossCountData;
//        // 公司之间的关联
//        private Map<Integer, List<Integer>> relatedData;
//
//        // 将公司分成两组
//        public List<List<Integer>> divideCompany() {
//            // 保存最终的两个分组
//            List<List<Integer>> result = new ArrayList<>();
//
//            // 保存各个公司的ARP总和
//            Map<Integer, Double> arpuSum = new HashMap<>();
//
//            // 遍历所有的公司，计算各个公司的ARP总和
//            for (Map.Entry<Integer, Double> entry : arpuData.entrySet()) {
//                int companyId = entry.getKey();
//                double arpu = entry.getValue();
//
//                // 如果该公司有关联公司
//                if (relatedData.containsKey(companyId)) {
//                    // 计算关联公司的ARP总和
//                    List<Integer> relatedCompnayList = relatedData.get(companyId);
//                    double relatedArpuSum = 0;
//                    for (Integer related : relatedCompnayList) {
//                        relatedArpuSum += arpuData.get(related);
//                    }
//
//                    // 加上该公司本身的ARP
//                    relatedArpuSum += arpu;
//
//                    // 计算ARP总和
//                    double totalArpuSum = arpu * bossCountData.get(companyId) + relatedArpuSum;
//                    arpuSum.put(companyId, totalArpuSum);
//                } else {
//                    // 无关联公司，只加上本身的ARP
//                    double totalArpuSum = arpu * bossCountData.get(companyId);
//                    arpuSum.put(companyId, totalArpuSum);
//                }
//            }
//
//            // 将公司按照ARP总和从高到低排序
//            List<Map.Entry<Integer, Double>> arpuSumList = new ArrayList<>(arpuSum.entrySet());
//            arpuSumList.sort(Comparator.comparing(Map.Entry::getValue).reversed());
//
//            // 计算平均值
//            double averageArpuSum = 0;
//            for (Map.Entry<Integer, Double> entry : arpuSumList) {
//                averageArpuSum += entry.getValue();
//            }
//            averageArpuSum /= arpuSumList.size();
//
//            // 将公司放入两个分组
//            List<Integer> group1 = new ArrayList<>();
//            List<Integer> group2 = new ArrayList<>();
//            double group1ArpuSum = 0;
//            double group2ArpuSum = 0;
//            for (Map.Entry<Integer, Double> entry : arpuSumList) {
//                int companyId = entry.getKey();
//
//                // 如果该公司有关联公司，那么关联公司也要放入同一个分组
//                List<Integer> relatedCompnayList = relatedData.get(companyId);
//                double relatedArpuSum = 0;
//                if (relatedCompnayList != null) {
//                    for (Integer related : relatedCompnayList) {
//                        relatedArpuSum += arpuData.get(related);
//                    }
//                }
//
//                // 将公司放入合适的分组，使得两个分组的arpu接近
//                double arpuSum = entry.getValue() + relatedArpuSum;
//                if (group1ArpuSum <= group2ArpuSum) {
//                    group1.add(companyId);
//                    group1ArpuSum += arpuSum;
//                } else {
//                    group2.add(companyId);
//                    group2ArpuSum += arpuSum;
//                }
//
//                // 修正平均值，使得两个分组的ARP总和接近
//                double totalArpuSum = group1ArpuSum + group2ArpuSum;
//                averageArpuSum = totalArpuSum / (group1.size() + group2.size());
//            }
//
//            // 保存结果
//            result.add(group1);
//            result.add(group2);
//            return result;
//        }
//    }
//}
