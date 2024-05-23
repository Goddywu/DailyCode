package com.example.demospring2.c加权抽奖;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-03-03
 */
@Slf4j
public class Test2 {

    @Data
    private static class UserScore {
        private Long geedId;
        private Integer score;

        public UserScore(long geedId, double doubleScore) {
            this.geedId = geedId;
            this.score = (int) Math.ceil(doubleScore); // 向上取整
        }
    }

    private static List<Long> randomDrawLucky(final LinkedList<UserScore> userScoreList, final int luckyNum) {
        List<Long> luckyGeekIds = new ArrayList<>();
        int sum = userScoreList.stream().mapToInt(UserScore::getScore).sum();
        Random random = new Random();
        while (luckyGeekIds.size() < luckyNum) {
            if (sum == 0) {
                log.error("randomDrawLucky出现sum=0");
                break;
            }
            int randomInt = random.nextInt(sum) + 1;// 随机 [1, sum]

            int curSum = 0;
            Iterator<UserScore> iterator = userScoreList.iterator();
            while (iterator.hasNext()) {
                UserScore next = iterator.next();
                int willAddScore = next.getScore();
                if (curSum < randomInt && randomInt <= curSum + willAddScore) {
                    iterator.remove();
                    luckyGeekIds.add(next.getGeedId());
                    sum -= next.getScore();
                    break;
                }
                curSum += willAddScore;
            }
        }
        return luckyGeekIds;
    }

    private static List<Long> randomDrawLucky3(final LinkedList<UserScore> userScoreList, final int luckyNum) {
        List<UserScore> array = new ArrayList<>();
        for (UserScore userScore : userScoreList) {
            for (int i = userScore.score; i > 0; i--) {
                array.add(userScore);
            }
        }
        Collections.shuffle(array);

        Set<Long> geekIds = new HashSet<>();
        for (UserScore userScore : userScoreList) {
            geekIds.add(userScore.getGeedId());
            if (geekIds.size() >= luckyNum) {
                break;
            }
        }
        return new ArrayList<>(geekIds);
    }

    public static void main(String[] args) {
        for (int j = 0; j < 10; j++) {

            LinkedList<UserScore> userScoreList = new LinkedList<>();
            Random random = new Random();
            for (int i = 0; i < 1_000000; i++) {
                userScoreList.add(new UserScore(i, random.nextInt(10000)));
            }
            long startMs = System.currentTimeMillis();
            randomDrawLucky(userScoreList, 100); // 强哥
//            randomDrawLucky3(userScoreList, 100); // 华哥
            System.out.println("======= 第 " + j + " 次 " + (System.currentTimeMillis() - startMs) + "ms ========");
        }
    }
}
