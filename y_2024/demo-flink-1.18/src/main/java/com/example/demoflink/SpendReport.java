package com.example.demoflink;

import com.example.demoflink.domain.Person;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-12-01
 */
public class SpendReport {

    public static void printPerson(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        ParameterTool params = ParameterTool.fromArgs(args);

        DataStream<Person> persons = env.fromElements(
                new Person("zhang", 12),
                new Person("li", 13),
                new Person("sun", 29),
                new Person("Fred", 35),
                new Person("Wilma", 35),
                new Person("Pebbles", 2));

        DataStream<Person> adults = persons.filter(new FilterFunction<Person>() {
            @Override
            public boolean filter(Person person) throws Exception {
                return person.getAge() >= 18;
            }
        });


//        adults.print();
        adults.writeAsText(params.get("output"), FileSystem.WriteMode.OVERWRITE);

        env.execute();
    }

    public static void main(String[] args) throws Exception {
        printPerson(args);
    }
}
