package moe.ahao.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.SECONDS)
@Threads(Threads.MAX)
@Warmup(iterations = 10) // 预热10轮
@Measurement(iterations = 10) // 代表做10轮正式计量测试, 每次先预热完再执行
@Fork(3) // 所以通过3轮测试较为全面的测试, 每次先预热完再执行
public class JMH {
    private static final int SIZE = 10000;
    private List<String> list = new LinkedList<>();
    @Setup
    public void setUp() {
        for (int i = 0; i < SIZE; i++) {
            list.add(String.valueOf(i));
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void forIndexIterate() {
        for (int i = 0; i < list.size(); i++) {
            list.get(i);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void forEachIterate() {
        for (String s : list) {
        }
    }

    /**
     * 仅限于IDE中运行
     * 命令行模式 则是 build 然后 java -jar 启动
     *
     * 1. 这是benchmark 启动的入口
     * 2. 这里同时还完成了JMH测试的一些配置工作
     * 3. 默认场景下，JMH会去找寻标注了@Benchmark的方法，可以通过include和exclude两个方法来完成包含以及排除的语义
     */
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(JMH.class.getSimpleName())
            .forks(1)
            .output("./Benchmark.log")
            .build();
        new Runner(opt).run();
    }
}
