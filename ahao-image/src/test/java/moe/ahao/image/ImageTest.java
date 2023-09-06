package moe.ahao.image;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.core.ImageCommand;
import org.im4java.process.ArrayListOutputConsumer;
import org.im4java.process.ProcessStarter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;

public class ImageTest {
    public static final String SOURCE = File.separator + "img" + File.separator + "source.jpg";
    public static final String TARGET = File.separator + "img" + File.separator + "%s.jpg";

    public static final boolean useGM = true;

    @BeforeAll
    static void beforeAll() {
        // 设置gm的命令路径
        ProcessStarter.setGlobalSearchPath("D:/GraphicsMagick-1.3.41-Q16");
    }

    @Test
    @DisplayName("旋转图片")
    void rotateImage(TestInfo testInfo) throws Exception {
        // 1. 设置图片信息
        String methodName = testInfo.getTestMethod().map(Method::getName).orElse("");
        String target = String.format(TARGET, methodName);

        // 2. 设置处理图片参数
        Double degree = 90d; // 顺时针旋转90度

        // 3. 处理图片
        IMOperation op = new IMOperation();
        op.addImage(this.getPath(SOURCE));
        op.rotate(degree);
        op.addImage(this.getPath(target));
        ImageCommand convert = new ConvertCmd(useGM);
        convert.run(op);
        System.out.println("旋转图片成功");
    }

    @Test
    @DisplayName("裁剪图片")
    void cutImage(TestInfo testInfo) throws Exception {
        // 1. 设置图片信息
        String methodName = testInfo.getTestMethod().map(Method::getName).orElse("");
        String target = String.format(TARGET, methodName);

        // 2. 设置处理图片参数
        Integer width = 200;  // 裁剪后的宽度
        Integer height = 300; // 裁剪后的高度
        Integer x = 400;      // 起始横坐标
        Integer y = 500;      // 起始纵坐标

        // 3. 处理图片
        IMOperation op = new IMOperation();
        op.addImage(this.getPath(SOURCE));
        op.crop(width, height, x, y);
        op.addImage(this.getPath(target));
        ImageCommand convert = new ConvertCmd(useGM);
        convert.run(op);
        System.out.println("裁剪图片成功");
    }

    @Test
    @DisplayName("缩放图片")
    void zoomImage(TestInfo testInfo) throws Exception {
        // 1. 设置图片信息
        String methodName = testInfo.getTestMethod().map(Method::getName).orElse("");
        String target = String.format(TARGET, methodName);

        // 2. 设置处理图片参数
        Integer width = 200;  // 缩放后的宽度
        Integer height = 300; // 缩放后的高度
        // 高度和宽度需符合图片比例，不符合的情况下，以其中最小值为准。

        // 3. 处理图片
        IMOperation op = new IMOperation();
        op.addImage(this.getPath(SOURCE));
        op.resize(width, height);
        op.addImage(this.getPath(target));
        ImageCommand convert = new ConvertCmd(useGM);
        convert.run(op);
        System.out.println("缩放图片成功");
    }

    @Test
    @DisplayName("压缩图片")
    void compressImage(TestInfo testInfo) throws Exception {
        // 1. 设置图片信息
        String methodName = testInfo.getTestMethod().map(Method::getName).orElse("");
        String target = String.format(TARGET, methodName);

        // 2. 设置处理图片参数
        Double quality = 50d;  // 图片压缩比，有效值范围是0.0-100.0，数值越大，缩略图越清晰。

        // 3. 处理图片
        IMOperation op = new IMOperation();
        op.addImage(this.getPath(SOURCE));
        op.quality(quality);
        op.addImage(this.getPath(target));
        ImageCommand convert = new ConvertCmd(useGM);
        convert.run(op);
        System.out.println("压缩图片成功");
    }

    @Test
    @DisplayName("文字水印")
    void textImage(TestInfo testInfo) throws Exception {
        // 1. 设置图片信息
        String methodName = testInfo.getTestMethod().map(Method::getName).orElse("");
        String target = String.format(TARGET, methodName);

        // 2. 设置处理图片参数
        String font = "Microsoft-YaHei-&-Microsoft-YaHe"; // 字体
        String gravity = "southeast"; // 文字水印在图片的位置
        Integer pointsize = 60;
        String fill = "#F2F2F2";
        String content = "hello world"; // 文字水印内容

        // 3. 处理图片
        IMOperation op = new IMOperation();
        op.addImage(this.getPath(SOURCE));
        op.font(font).gravity(gravity).pointsize(pointsize).fill(fill).draw("text 10,10 " + content);
        op.addImage(this.getPath(target));
        ImageCommand convert = new ConvertCmd(useGM);
        convert.run(op);
        System.out.println("文字水印成功");
    }

    @Test
    @DisplayName("获取图片信息")
    void compositeImage(TestInfo testInfo) throws Exception {
        // 1. 设置图片信息
        String methodName = testInfo.getTestMethod().map(Method::getName).orElse("");
        String target = String.format(TARGET, methodName);

        // 2. 设置处理图片参数
        IMOperation op = new IMOperation();
        op.format("%w,%h,%d,%f,%Q,%b,%e");
        op.addImage();
        ImageCommand identifyCmd = new IdentifyCmd(useGM);
        ArrayListOutputConsumer output = new ArrayListOutputConsumer();
        identifyCmd.setOutputConsumer(output);
        identifyCmd.run(op, this.getPath(SOURCE));

        // 3. 获取图片信息
        List<String> cmdOutput = output.getOutput();
        System.out.println(cmdOutput);

        String[] result = cmdOutput.get(0).split(",");
        System.out.println("宽度:" + result[0]);
        System.out.println("高度:" + result[1]);
        System.out.println("路径:" + result[2]);
        System.out.println("文件:" + result[3]);
        System.out.println("质量:" + result[4]);
        System.out.println("大小:" + result[5]);
        System.out.println("格式:" + result[6]);

        System.out.println("获取图片信息成功");
    }

    private String getPath(String path) throws FileNotFoundException {
        ClassLoader classLoader = ImageTest.class.getClassLoader();
        URL resourceURL = classLoader.getResource("");

        if (resourceURL != null) {
            // 从URL中获取文件的绝对路径
            String absolutePath = new File(resourceURL.getFile()).getAbsolutePath() + path;
            System.out.println("绝对路径：" + absolutePath);
            return absolutePath;
        } else {
            throw new FileNotFoundException("classpath路径未找到");
        }
    }
}
