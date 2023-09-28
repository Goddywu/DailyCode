package com.example.demospring2.web;

import io.micrometer.core.instrument.Metrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-07-18
 */
@Slf4j
@RestController
@RequestMapping("/metrics")
public class MetricsController {

    private AtomicInteger count = new AtomicInteger();
    private int simpleCount = 0;

    @GetMapping("/gauge")
    public String gauge() {
        count.addAndGet(50);
        simpleCount += 50;
        log.info("count: " + count);
        Metrics.gauge("F1_PUSH", new AtomicInteger(simpleCount));
        return "ok";
    }
}
