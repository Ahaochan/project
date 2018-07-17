package com.ahao.core.util.concurrent;

import com.ahao.core.util.lang.ArrayHelper;
import com.ahao.core.util.lang.CollectionHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Ahaochan on 2017/10/23.
 * 线程池工具类
 */
public abstract class ExecutorHelper {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorHelper.class);
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    private ExecutorHelper() {
    }

    //=============================执行Runnable============================================
    public static boolean submitRunnable(Collection<Runnable> runs) {
        return submitRunnable(ArrayHelper.toArray(runs));
    }

    public static boolean submitRunnable(Runnable... runs) {
        if (ArrayUtils.isEmpty(runs)) {
            return false;
        }
        for (Runnable call : runs) {
            executor.submit(call);
        }
        return true;
    }
    //=============================执行Runnable============================================

    //=============================执行Callable获取Future==================================
    public static <T> Future<T> submitCallable(Callable<T> call) {
        return executor.submit(call);
    }

    public static <T> List<Future<T>> submitCallable(Collection<Callable<T>> calls) {
        if (CollectionUtils.isEmpty(calls)) {
            return null;
        }
        List<Future<T>> futures = new ArrayList<>(calls.size());
        for (Callable<T> call : calls) {
            futures.add(executor.submit(call));
        }
        return futures;
    }
    //=============================执行Callable获取Future==================================

    //=============================解析Future获取数据======================================
    public static <T> T getResult(Future<T> future) {
        List<T> result = getResult(Arrays.asList(future));
        if (CollectionHelper.isEmpty(result)) {
            return null;
        }
        return result.get(0);
    }

    public static <T> List<T> getResult(Collection<Future<T>> futures) {
        if (CollectionUtils.isEmpty(futures)) {
            return CollectionHelper.emptyList();
        }
        List<T> result = new ArrayList<>(futures.size());
        try {
            for (Future<T> future : futures) {
                result.add(future.get());
            }
            return result;
        } catch (InterruptedException e) {
            logger.error("线程池中断异常", e);
        } catch (ExecutionException e) {
            logger.error("线程池执行异常", e);
        }
        return CollectionHelper.emptyList();
    }
    //=============================解析Future获取数据======================================
}
