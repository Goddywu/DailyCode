package com.example.demoflink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.apache.log4j.Logger;


/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-12-18
 */
public class MyTest {

    private static final Logger log = Logger.getLogger(MyTest.class.getName());

    private static void testSocketStream() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // nc -lk 9999
        SingleOutputStreamOperator<Tuple2<String, Integer>> dataStream = env
                .socketTextStream("localhost", 9999)
                .flatMap(new Splitter())
                .keyBy(value -> value.f0)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(5)))
                .sum(1);
        log.info("2222222222");
        dataStream.print();

        env.execute("window word count");
    }

    private static class Splitter implements FlatMapFunction<String, Tuple2<String, Integer>> {
        @Override
        public void flatMap(String s, Collector<Tuple2<String, Integer>> collector) throws Exception {
            for (String word : s.split(" ")) {
                log.info("11111111");
                collector.collect(new Tuple2<>(word, 1));
            }
        }
    }

    private static void testLocal() throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

        DataStream<Integer> dataStream = env.fromElements(1, 2, 3);
        dataStream.print();

        env.execute("window word count");
    }

    public static void main(String[] args) throws Exception {
        testSocketStream();
//        testLocal();
    }

}
