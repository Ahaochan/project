package moe.ahao.tend.consistency.core.spi.shard.shardkey;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.Calendar;
import java.util.Properties;

/**
 * 任务分片键生成器实现类
 * 如业务服务在配置文件中，没有配置任务分片键生成器的实现类，则使用该类作为分片键生成器
 *
 * @author zhonghuashishan
 **/
public final class SnowflakeShardingKeyGenerator implements ShardingKeyGenerator {

    private static volatile SnowflakeShardingKeyGenerator instance;

    public static final long EPOCH;

    private static final long SEQUENCE_BITS = 12L;

    private static final long WORKER_ID_BITS = 10L;

    private static final long SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1;

    private static final long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;

    private static final long TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS;

    private static final long WORKER_ID_MAX_VALUE = 1L << WORKER_ID_BITS;

    private static final long WORKER_ID = 0;

    private static final int MAX_TOLERATE_TIME_DIFFERENCE_MILLISECONDS = 10;

    private SnowflakeShardingKeyGenerator() {
    }

    @Getter
    @Setter
    private Properties properties = new Properties();

    private byte sequenceOffset;

    private long sequence;

    private long lastMilliseconds;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, Calendar.NOVEMBER, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        EPOCH = calendar.getTimeInMillis();
    }

    /**
     * 生产一致性任务分片键
     *
     * @return 一致性任务分片键
     */
    @Override
    public synchronized long generateShardKey() {
        long currentMilliseconds = System.currentTimeMillis();;
        if (waitTolerateTimeDifferenceIfNeed(currentMilliseconds)) {
            currentMilliseconds = System.currentTimeMillis();;
        }
        if (lastMilliseconds == currentMilliseconds) {
            if (0L == (sequence = (sequence + 1) & SEQUENCE_MASK)) {
                currentMilliseconds = waitUntilNextTime(currentMilliseconds);
            }
        } else {
            vibrateSequenceOffset();
            sequence = sequenceOffset;
        }
        lastMilliseconds = currentMilliseconds;
        return ((currentMilliseconds - EPOCH) << TIMESTAMP_LEFT_SHIFT_BITS) | (getWorkerId() << WORKER_ID_LEFT_SHIFT_BITS) | sequence;
    }

    @SneakyThrows
    private boolean waitTolerateTimeDifferenceIfNeed(final long currentMilliseconds) {
        if (lastMilliseconds <= currentMilliseconds) {
            return false;
        }
        long timeDifferenceMilliseconds = lastMilliseconds - currentMilliseconds;
        if(timeDifferenceMilliseconds >= getMaxTolerateTimeDifferenceMilliseconds()) {
            throw new IllegalStateException(String.format("Clock is moving backwards, last time is %d milliseconds, current time is %d milliseconds", lastMilliseconds, currentMilliseconds));
        }
        Thread.sleep(timeDifferenceMilliseconds);
        return true;
    }

    private long getWorkerId() {
        long result = Long.valueOf(properties.getProperty("worker.id", String.valueOf(WORKER_ID)));
        if(result < 0 || result >= WORKER_ID_MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        return result;
    }

    private int getMaxTolerateTimeDifferenceMilliseconds() {
        return Integer.valueOf(properties.getProperty("max.tolerate.time.difference.milliseconds", String.valueOf(MAX_TOLERATE_TIME_DIFFERENCE_MILLISECONDS)));
    }

    private long waitUntilNextTime(final long lastTime) {
        long result = System.currentTimeMillis();;
        while (result <= lastTime) {
            result = System.currentTimeMillis();;
        }
        return result;
    }

    private void vibrateSequenceOffset() {
        sequenceOffset = (byte) (~sequenceOffset & 1);
    }

    /**
     * 获取单例对象
     *
     * @return 单例对象
     */
    public static SnowflakeShardingKeyGenerator getInstance() {
        if (instance == null) {
            synchronized (SnowflakeShardingKeyGenerator.class) {
                if (instance == null) {
                    instance = new SnowflakeShardingKeyGenerator();
                }
            }
        }
        return instance;
    }

    public void setWorkerId(String value) {
        properties.put("worker.id", value);
    }

}
