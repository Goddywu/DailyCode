package com.example.demospring2.bean;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-01-19
 */
@Slf4j
@Component
public class BeanFactory {

    @Getter
    private MyBean myBean;

    @PostConstruct
    public void init() {
        String name = UUID.randomUUID().toString();
        log.info("[init] mybean {}", name);
        myBean = new MyBean(name);
    }

    @Scheduled(cron = "0 * * * * ?")
    public void sch() {
        init();
    }
}
