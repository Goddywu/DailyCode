package c_加权抽奖;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class Main {

    @Data
    private static class UserScore {
        private Long uid;
        private Integer score;

        public UserScore(long uid, double doubleScore) {
            this.uid = uid;
            this.score = (int) Math.ceil(doubleScore); // 向上取整
        }
    }

    private static List<Long> randomDrawLucky(final LinkedList<UserScore> userScoreList, final int luckyNum) {
        if (userScoreList.size() <= luckyNum) {
            return userScoreList.stream().map(UserScore::getUid).collect(Collectors.toList());
        }

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
                    luckyGeekIds.add(next.getUid());
                    sum -= next.getScore();
                    break;
                }
                curSum += willAddScore;
            }
        }
        return luckyGeekIds;
    }

    private static List<Long> randomDrawLucky2(final LinkedList<UserScore> userScoreList, final int luckyNum) {
        List<UserScore> array = new ArrayList<>();
        for (UserScore userScore : userScoreList) {
            for (int i = userScore.score; i > 0; i--) {
                array.add(userScore);
            }
        }
        Collections.shuffle(array);

        Set<Long> geekIds = new HashSet<>();
        for (UserScore userScore : userScoreList) {
            geekIds.add(userScore.getUid());
            if (geekIds.size() >= luckyNum) {
                break;
            }
        }
        return new ArrayList<>(geekIds);
    }


    public static void main(String[] args) {
        final int ORIGIN_NUM = 10_0000;
        final int MAX_SCORE = 100;
        final int LUCKY_NUM = 100;
        final int RANDOM_TIME = 10;

        System.out.println("################ randomDrawLucky ################");

        for (int j = 0; j < RANDOM_TIME; j++) {

            LinkedList<UserScore> userScoreList = new LinkedList<>();
            Random random = new Random();
            for (int i = 0; i < ORIGIN_NUM; i++) {
                userScoreList.add(new UserScore(i, random.nextInt(MAX_SCORE)));
            }
            long startMs = System.currentTimeMillis();
            randomDrawLucky(userScoreList, LUCKY_NUM);
            System.out.println("======= 第 " + j + " 次 " + (System.currentTimeMillis() - startMs) + "ms ========");
        }

        System.out.println("################ randomDrawLucky2 ################");

        for (int j = 0; j < RANDOM_TIME; j++) {

            LinkedList<UserScore> userScoreList = new LinkedList<>();
            Random random = new Random();
            for (int i = 0; i < ORIGIN_NUM; i++) {
                userScoreList.add(new UserScore(i, random.nextInt(MAX_SCORE)));
            }
            long startMs = System.currentTimeMillis();
            randomDrawLucky2(userScoreList, LUCKY_NUM);
            System.out.println("======= 第 " + j + " 次 " + (System.currentTimeMillis() - startMs) + "ms ========");
        }
    }
}
