package com.example.demospring2.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2024-02-22
 */
@Slf4j
@RestController
@RequestMapping("/tools")
@RequiredArgsConstructor
public class ToolsController {

    private final HttpServletRequest request;

    @GetMapping("/host")
    public String host() {
        return request.getServerName();
    }

    @GetMapping("/uri")
    public String uri() {
        return request.getRequestURI();
    }

    @GetMapping("/addr")
    public String addr() {
        return request.getRemoteAddr();
    }
}
