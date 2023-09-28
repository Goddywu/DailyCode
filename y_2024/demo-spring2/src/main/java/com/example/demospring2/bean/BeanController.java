package com.example.demospring2.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-01-18
 */
@RestController
@RequestMapping("/bean")
@RequiredArgsConstructor
public class BeanController {

    private final MyBean myBean;

    @GetMapping("/print")
    public String print() {
        myBean.print();
        return "ok";
    }
}
