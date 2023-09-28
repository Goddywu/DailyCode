package com.example.demospring2.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-02-08
 */
@Slf4j
@Configuration
public class ConditionConfig {

    @Bean
    @ConditionalOnExpression("'true'.equals('${redisson.open:true}')")
    public String locker() {
        log.info("!!!!!! start init conditionBean !!!!!!");
        return "ok";
    }

}
