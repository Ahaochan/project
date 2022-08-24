package com.ruyuan.process.engine.demo.springboot;

import org.springframework.stereotype.Service;

/**
 * @author zhonghuashishan
 * @version 1.0
 */
@Service
public class DemoServiceImpl implements DemoService {
    @Override
    public void sayHello() {
        System.out.println("hello");
    }
}
