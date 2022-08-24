package com.ruyuan.process.engine.annoations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(value = {"com.ruyuan.process.engine.instance"})
public class ComponentScanConfig {
}
