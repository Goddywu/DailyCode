package org.example.interview2024.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-05-23
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hi")
    public String testHi() {
        return "hi";
    }
}
