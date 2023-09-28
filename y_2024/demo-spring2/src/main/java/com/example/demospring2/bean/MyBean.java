package com.example.demospring2.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-01-18
 */
@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyBean {

    private String name;

    public void print() {
        log.info("myBean {} print!", name);
    }
}
