package com.example.demospring2.bean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-01-18
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class BeanConfig {

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public MyBean myBean(BeanFactory beanFactory) {
        return beanFactory.getMyBean();
    }

    @ConditionalOnBean(name = "myBean")
    @Bean
    public String hhh() {
        log.info("======hhhhh");
        return "hhh";
    }
}
