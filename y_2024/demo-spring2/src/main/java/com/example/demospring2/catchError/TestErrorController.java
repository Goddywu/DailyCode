package com.example.demospring2.catchError;

import org.apache.tomcat.util.http.fileupload.MultipartStream;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-04-12
 */
@RestController
public class TestErrorController {

    @GetMapping("/throwError")
    public String throwError() throws MultipartStream.MalformedStreamException {
        throw new MultipartStream.MalformedStreamException("Stream ended unexpectedly");
    }

    @GetMapping("/throwError2")
    public String throwError2() {
        throw new IllegalArgumentException(
                "URLDecoder: Illegal hex characters in escape (%) pattern - "
                        + "e.getMessage()");
    }
}
