package moe.ahao.tend.consistency.core.infrastructure.repository.impl.rocksdb;

import lombok.extern.slf4j.Slf4j;
import moe.ahao.tend.consistency.core.infrastructure.exceptions.ConsistencyException;
import moe.ahao.tend.consistency.core.infrastructure.exceptions.ConsistencyExceptionEnum;
import moe.ahao.tend.consistency.core.infrastructure.repository.impl.mybatis.data.ConsistencyTaskInstance;
import moe.ahao.util.commons.io.JSONHelper;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.rocksdb.*;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * RocksDB本地KV存储
 */
@Slf4j
public class RocksLocalStorage {
    private static RocksDB rocksDB;
    /**
     * 数据库列族(表)集合
     */
    public static ConcurrentMap<String, ColumnFamilyHandle> columnFamilyHandleMap = new ConcurrentHashMap<>();
    /**
     * 小顶堆
     * queue，他是跟我们的rocksdb里的数据有对应关系的，配合使用关系的内存里的queue数据结构
     */
    public Queue<String> priorityQueue = new PriorityBlockingQueue<>();

    static {
        RocksDB.loadLibrary();
    }

    public RocksLocalStorage(String path) throws RocksDBException {
        // 1. 初始化目录
        File dir = new File(path);
        // 如果指定的路径是不是文件夹，而是文件
        boolean isDir = dir.isDirectory();
        if (!isDir) {
            throw ConsistencyExceptionEnum.ROCKSDB_PATH_IS_NOT_DIR.msg(path);
        }
        // 如果指定的RocksDB的存储目录不存在,则进行创建
        boolean isExists = dir.exists();
        if (!isExists) {
            boolean success = dir.mkdirs();
            if (!success) {
                throw ConsistencyExceptionEnum.ROCKSDB_PATH_MKDIR_FAIL.msg(path);
            }
        }

        // 2. 初始化数据库
        Options options = new Options();
        options.setCreateIfMissing(true);
        DBOptions dbOptions = new DBOptions();
        dbOptions.setCreateIfMissing(true);
        // 列族描述器集合
        // rocksdb，kv存储，基于key-values，存储数据
        // 他底层，是基于列族的存储格式，他是有多个列族，每个列族里是可以有不同的列
        // 会把列族的数据存储在一起，一个底层磁盘文件里，列族存储
        List<ColumnFamilyDescriptor> columnFamilyDescriptors = Arrays.asList(
            new ColumnFamilyDescriptor(RocksDB.DEFAULT_COLUMN_FAMILY, new ColumnFamilyOptions())
        );
        // ColumnFamilyHandle集合
        List<ColumnFamilyHandle> columnFamilyHandles = new ArrayList<>();
        // 在open操作，他其实是会恢复db里数据，wal日志加载，memtable数据恢复
        // 你可以通过rocksdb实例，可以看到系统重启之前的数据视图
        rocksDB = RocksDB.open(dbOptions, path, columnFamilyDescriptors, columnFamilyHandles);

        for (int i = 0; i < columnFamilyDescriptors.size(); i++) {
            ColumnFamilyHandle columnFamilyHandle = columnFamilyHandles.get(i);
            String cfName = new String(columnFamilyDescriptors.get(i).getName());
            columnFamilyHandleMap.put(cfName, columnFamilyHandle);
        }
        log.info("RocksDB 初始化成功 path:{}", path);

        // 如果RocksDB中有数据，则将数据放入优先队列
        loadPriorityQueue();
    }

    /**
     * 加载所有RocksDB中的Key到优先队列
     */
    private void loadPriorityQueue() {
        try {
            RocksIterator rocksIterator = rocksDB.newIterator(columnFamilyHandleMap.get("default"));
            for (rocksIterator.seekToFirst(); rocksIterator.isValid(); rocksIterator.next()) {
                priorityQueue.add(new String(rocksIterator.key()));
            }
        } catch (Exception e) {
            log.error("获取所有键值对时，发生异常", e);
        }
    }

    /**
     * 添加一致性任务实例到RocksDB
     *
     * @param taskInstance 一致性任务实例
     */
    public void put(ConsistencyTaskInstance taskInstance) {
        try {
            // 稍微讲一点rocksdb的原理
            // 他刚开始做一个写入，会把你的数据写入memtable，写入内存数据结构里去，同时会去做一个write ahead log预写日志的追加，磁盘里去
            // 当你的memtable写满了，会把数据flush到磁盘文件sstfile里去，会把这批数据对应的WAL日志清理
            // 多讲一点点rocksdb的原理，随着你的每次memtable进行flush，sstfile，会越来越多
            // rocksdb后台会去执行一个动作，compaction，很多小文件合并，就是说对你的数据删除，他不是说在sstfile里进行删除，而是对数据加一个删除标记
            // 在多个文件进行合并的时候，此时把一些标记为删除的数据，就可以清理掉，物理删除这样子
            rocksDB.put(columnFamilyHandleMap.get("default"), getRocksKey(taskInstance), getRocksValue(taskInstance));

            // 系统出现了重启，rocksdb数据必须进行恢复，读取你的WAL日志，恢复memtable里的数据视图
            // 对于我们的priorityQueue里的数据，也必须去进行恢复

            if (!priorityQueue.contains(getRocksKeyStr(taskInstance))) {
                // 放入优先队列，方便后面取出来的时候，直接取
                priorityQueue.add(getRocksKeyStr(taskInstance));
            }
            log.info("完成任务一致性任务的本地存储，任务信息为 {}", JSONHelper.toString(taskInstance));
        } catch (Exception e) {
            log.error("删除列族时，发生异常", e);
        }
    }

    public String putIfAbsent(ConsistencyTaskInstance taskInstance) {
        String s = this.get(taskInstance);
        if (StringUtils.isEmpty(s)) {
            // 记录到RocksDB
            this.put(taskInstance);
        }
        return s;
    }

    /**
     * 删除RocksDB中的任务实例
     *
     * @param taskInstance 任务实例信息
     */
    public void delete(ConsistencyTaskInstance taskInstance) {
        String rocksKeyStr = getRocksKeyStr(taskInstance);
        try {
            rocksDB.delete(columnFamilyHandleMap.get("default"), getRocksKey(taskInstance));
            priorityQueue.remove(rocksKeyStr);
            log.info("删除的key为 {}", rocksKeyStr);
        } catch (Exception e) {
            log.error("删除key={}时，发生异常", rocksKeyStr, e);
        }
    }

    /**
     * 查询一致性任务实例
     *
     * @param taskInstance 一致性任务实例
     * @return 任务实例
     */
    public String get(ConsistencyTaskInstance taskInstance) {
        String value = null;
        try {
            byte[] bytes = rocksDB.get(columnFamilyHandleMap.get("default"), getRocksKey(taskInstance));
            if (!ObjectUtils.isEmpty(bytes)) {
                value = new String(bytes);
            }
            return value;
        } catch (Exception e) {
            log.error("获取key={}的值时，发生异常", getRocksKeyStr(taskInstance), e);
            return null;
        }
    }

    /**
     * 根据给定的key的集合查询多个键值对
     *
     * @param keys key集合
     * @return 一致性任务
     */
    public List<ConsistencyTaskInstance> multiGetAsList(List<String> keys) {
        List<ConsistencyTaskInstance> consistencyTaskInstances = new ArrayList<>(keys.size());
        try {
            List<byte[]> keyBytes = new ArrayList<>(keys.size() + 1);
            List<ColumnFamilyHandle> columnFamilyHandles = new ArrayList<>(keys.size() + 1);
            for (String key : keys) {
                keyBytes.add(key.getBytes());
                columnFamilyHandles.add(columnFamilyHandleMap.get("default"));
            }
            List<byte[]> bytes = rocksDB.multiGetAsList(columnFamilyHandles, keyBytes);
            for (byte[] valueBytes : bytes) {
                if (ArrayUtils.isNotEmpty(valueBytes)) {
                    String value = new String(valueBytes);
                    ConsistencyTaskInstance instance = JSONHelper.parse(value, ConsistencyTaskInstance.class);
                    if (instance == null) {
                        continue;
                    }
                    consistencyTaskInstances.add(instance);
                }
            }
            return consistencyTaskInstances;
        } catch (Exception e) {
            log.error("批量获取多个键值对时，发生异常, keys={}", keys, e);
            return consistencyTaskInstances;
        }
    }

    /**
     * 从优先队列中获取前TOP N个任务实例
     *
     * @param n 获取的条数
     * @return 一致性任务列表
     */
    public List<ConsistencyTaskInstance> getTopN(Integer n) {
        if (n == null) {
            throw new ConsistencyException("未指定要获取多少个一致性任务");
        }
        // 计数器
        int count = 0;
        List<String> taskInstanceKeys = new ArrayList<>(n);
        // 获取指定的前N条实例
        try {
            while (!priorityQueue.isEmpty()) {
                if (count >= n) {
                    break;
                }
                String key = priorityQueue.poll();
                if (StringUtils.isEmpty(key)) {
                    continue;
                }
                taskInstanceKeys.add(key);
                count++;
            }
            // 拿到key的list
            return multiGetAsList(taskInstanceKeys);
        } catch (Exception e) {
            return new ArrayList<>(0);
        }
    }


    /**
     * 根据一致性任务信息获取key
     *
     * @param taskInstance 任务实例信息
     * @return 任务key
     */
    private String getRocksKeyStr(ConsistencyTaskInstance taskInstance) {
        return taskInstance.getExecuteTime() + "_" + taskInstance.getShardKey() + "_" + taskInstance.getId();
    }

    /**
     * 根据一致性任务信息获取key
     *
     * @param taskInstance 任务实例信息
     * @return 任务key
     */
    private byte[] getRocksKey(ConsistencyTaskInstance taskInstance) {
        String key = taskInstance.getExecuteTime() + "_" + taskInstance.getShardKey() + "_" + taskInstance.getId();
        return key.getBytes();
    }

    /**
     * 根据一致性任务信息获取key
     *
     * @param taskInstance 任务实例信息
     * @return 任务key
     */
    private byte[] getRocksValue(ConsistencyTaskInstance taskInstance) {
        return JSONHelper.toString(taskInstance).getBytes();
    }
}
