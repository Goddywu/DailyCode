package org.example.interview2024;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-05-23
 */
@Slf4j
@EnableScheduling
@SpringBootApplication
public class Interview2024Application {

    public static void main(String[] args) {
        SpringApplication.run(Interview2024Application.class);
    }
}
