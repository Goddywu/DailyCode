package com.example.demospring2.c对象池;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * @author Goddy <wuchuansheng@kanzhun.com> 2023-10-11
 */
public class StudentPool extends GenericObjectPool<Student> {

    public StudentPool(PooledObjectFactory<Student> factory) {
        super(factory);
    }


}
