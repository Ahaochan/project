package com.ahao.util.commons.concurrent;

import com.ahao.util.commons.lang.ArrayHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Ahaochan on 2017/10/23.
 * 线程池工具类
 */
public class ExecutorHelper {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorHelper.class);
    private static final ExecutorService executor = Executors.newCachedThreadPool();

    //=============================执行Runnable============================================
    public static boolean submitOne(Collection<Runnable> runs){
        return submitList(ArrayHelper.toArray(runs));
    }
    public static boolean submitList(Runnable... runs){
        if(ArrayUtils.isEmpty(runs)){
            return false;
        }
        for(Runnable call : runs){
            executor.submit(call);
        }
        return true;
    }
    //=============================执行Runnable============================================


    //=============================执行Callable获取Future==================================
    public static <T> Future<T> submitOne(Callable<T> call){
        List<Future<T>> futures = submitList(call);
        if(CollectionUtils.isEmpty(futures)){
            return null;
        }
        return futures.get(0);
    }
    public static <T> List<Future<T>> submitList(Collection<Callable<T>> calls){
        return submitList(ArrayHelper.toArray(calls));
    }
    @SafeVarargs
    public static <T> List<Future<T>> submitList(Callable<T>... calls){
        if(ArrayUtils.isEmpty(calls)){
            return Collections.emptyList();
        }
        List<Future<T>> futures = new ArrayList<>(calls.length);
        for(Callable<T> call : calls){
            futures.add(executor.submit(call));
        }
        return futures;
    }
    //=============================执行Callable获取Future==================================


    //=============================解析Future获取数据======================================
    public static <T> T getOne(Future<T> future){
        List<T> result = getList(future);
        if(CollectionUtils.isEmpty(result)){
            return null;
        }
        return result.get(0);
    }
    public static <T> List<T> getList(Collection<Future<T>> futures){
        return getList(ArrayHelper.toArray(futures));
    }
    @SafeVarargs
    public static <T> List<T> getList(Future<T>... futures){
        if(ArrayUtils.isEmpty(futures)){
            return Collections.emptyList();
        }
        List<T> result = new ArrayList<>(futures.length);
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
        return Collections.emptyList();
    }
    //=============================解析Future获取数据======================================
}
