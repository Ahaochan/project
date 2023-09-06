package moe.ahao.image;

import org.gm4java.engine.GMService;
import org.gm4java.engine.support.GMConnectionPoolConfig;
import org.gm4java.engine.support.PooledGMService;
import org.gm4java.im4java.GMBatchCommand;
import org.im4java.core.IMOperation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Gm4JavaTest {
    public static final String SOURCE = File.separator + "img" + File.separator + "source.jpg";
    public static final String TARGET = File.separator + "img" + File.separator + "%s.jpg";

    @BeforeAll
    static void beforeAll() {
        // 设置gm的命令路径
    }

    @Test
    @DisplayName("压缩图片")
    @ParameterizedTest
    @CsvSource({"1,1", "1,10", "1,20", "1,50", "1,100",
            "10,10", "10,20", "10,50", "10,100",
            "20,20", "20,50", "20,100",
            "50,50", "50,100",
            "100,100"})
    void compressImage(int threadCount, int taskCount, TestInfo testInfo) throws Exception {
        GMConnectionPoolConfig config = new GMConnectionPoolConfig();
        config.setGMPath("D:/GraphicsMagick-1.3.41-Q16/gm");
        GMService gmService = new PooledGMService(config);


        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(taskCount);

        for (int i = 0; i < taskCount; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    // 1. 设置图片信息
                    String methodName = testInfo.getTestMethod().map(Method::getName).orElse("");
                    String target = String.format(TARGET, methodName + finalI);

                    // 2. 设置处理图片参数
                    Double quality = 50d;  // 图片压缩比，有效值范围是0.0-100.0，数值越大，缩略图越清晰。

                    // 3. 处理图片
                    IMOperation op = new IMOperation();
                    op.addImage(getPath(SOURCE));
                    op.quality(quality);
                    op.addImage(getPath(target));
                    GMBatchCommand command = new GMBatchCommand(gmService, "convert");
                    command.run(op);
                    // System.out.println("压缩图片成功");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        System.out.println("执行完毕");
    }

    private String getPath(String path) throws FileNotFoundException {
        ClassLoader classLoader = Im4JavaTest.class.getClassLoader();
        URL resourceURL = classLoader.getResource("");

        if (resourceURL != null) {
            // 从URL中获取文件的绝对路径
            String absolutePath = new File(resourceURL.getFile()).getAbsolutePath() + path;
            // System.out.println("绝对路径：" + absolutePath);
            return absolutePath;
        } else {
            throw new FileNotFoundException("classpath路径未找到");
        }
    }
}
