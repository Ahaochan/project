package com.ahao.util.spring;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.env.Environment;

public class AppUtils {

    public static Environment getEnv() {
        return SpringContextHolder.getBean(Environment.class);
    }

    public static boolean isDev() {
        String[] activeProfiles = getEnv().getActiveProfiles();
        return ArrayUtils.contains(activeProfiles, "dev");
    }
}
