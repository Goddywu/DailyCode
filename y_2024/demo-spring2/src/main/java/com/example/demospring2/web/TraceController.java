package com.example.demospring2.web;

import com.example.demospring2.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-02-05
 */
@Slf4j
@RequestMapping("/trace")
@RestController
public class TraceController {

    public final static ExecutorService chatExecutors =
            new ThreadPoolExecutor(100, 500, 10, TimeUnit.MINUTES,
                    new SynchronousQueue<>(), new CustomizableThreadFactory("chat-"),
                    new ThreadPoolExecutor.CallerRunsPolicy());

    @GetMapping("/test1")
    public String test1() throws Exception {
        log.info("1");
        LogUtil.setUsername("hi1");
        log.info("2");
        FutureTask<Void> futureTask = new FutureTask<>(() -> {
            log.info("3");
            LogUtil.setUsername("hi2");
            log.info("4");
            return null;
        });
        chatExecutors.submit(futureTask);
        log.info("5");
        Void unused = futureTask.get();
        log.info("6");

        return LogUtil.getUsername();
    }
}
