package com.example.demospring2;

import com.alibaba.nacos.api.annotation.NacosProperties;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.spring.context.annotation.EnableNacos;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@Slf4j
@EnableScheduling
@SpringBootApplication
//@EnableNacos(globalProperties = @NacosProperties)
//@NacosPropertySource(groupId = "tyche-video", dataId = "config", autoRefreshed = true, type = ConfigType.PROPERTIES)
public class DemoSpring2Application {

//    @Value("${spring.mongodb.uri}")
//    private String MONGO_DB;
//
//    @PostConstruct
//    public void warmup() {
//        log.info("+++++++" + MONGO_DB);
//    }

    public static void main(String[] args) {
        SpringApplication.run(DemoSpring2Application.class, args);
    }

}
