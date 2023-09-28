package com.example.demospring2.misc;

import java.util.Optional;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-03-01
 */
public class OptionalTest {

    public static void main(String[] args) {
        Optional<Object> o = Optional.ofNullable(1).map(a -> null);
        System.out.println(o.isPresent());
        System.out.println(o.get());
    }
}
