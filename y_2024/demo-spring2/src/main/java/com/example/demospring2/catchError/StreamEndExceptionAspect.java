package com.example.demospring2.catchError;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-04-11
 */
@Slf4j
@ControllerAdvice
public class StreamEndExceptionAspect {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<String> handleSecurityScanException(HttpServletRequest request, Exception ex) throws Exception {
        String scanHeader = request.getHeader("SEC-DAST-SCAN");
        log.info("scanHeader: {}, requestUri: {}, ex.class: {}, ex.msg: {}", scanHeader, request.getRequestURI(), ex.getClass(), ex.getMessage());

        if (scanHeader != null) {
            log.warn("Scan Error ", ex);
            return new ResponseEntity<>("Scan Error", HttpStatus.OK);
        }
        throw ex;
    }
}
