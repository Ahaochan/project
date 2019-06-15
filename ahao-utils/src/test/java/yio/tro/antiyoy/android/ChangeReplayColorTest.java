package yio.tro.antiyoy.android;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ChangeReplayColorTest {
    enum Color {
        RED(1), PURPLE(2);
        int code;
        Color(int code) { this.code = code; }
    }


    @Test
    @Ignore("业务代码")
    public void test() {
        // 1. 获取所有 replay xml 文件
        Path dir = Paths.get("C:\\yio.tro.antiyoy.android\\shared_prefs");
        if (Files.notExists(dir)) {
            Assert.fail();
        }

        try (DirectoryStream<Path> fs = Files.newDirectoryStream(dir, "[0-9]*.xml")) {
            for (Path path : fs) {
                // 2. 兼容 Apache Utils 转为 File
                File file = path.toFile();
                // 3. 读取每行字符串, 替换颜色配置
                List<String> lines = FileUtils.readLines(file, StandardCharsets.UTF_8);
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    if (StringUtils.startsWith(line, "    <int name=\"color_offset\" value=\"")) {
                        lines.set(i, "    <int name=\"color_offset\" value=\"" + Color.RED.code + "\" />");
                    }
                }
                // 4. 回写文件
                FileUtils.writeLines(file, lines);
            }
            System.out.println("success");
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
