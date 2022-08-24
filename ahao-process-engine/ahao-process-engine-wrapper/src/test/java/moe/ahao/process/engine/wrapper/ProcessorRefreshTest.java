package moe.ahao.process.engine.wrapper;

import moe.ahao.process.engine.core.process.ProcessContext;
import moe.ahao.process.engine.wrapper.model.ProcessContextFactory;
import moe.ahao.process.engine.wrapper.parse.ClassPathXmlProcessParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProcessorRefreshTest {
    @Test
    @DisplayName("配置更新")
    void refresh() throws Exception {
        ProcessContextFactory factory = new ProcessContextFactory(new ClassPathXmlProcessParser("process-demo.xml").parse());
        this.assertStandard(factory);

        Assertions.assertThrows(Exception.class, () -> factory.refresh(new ClassPathXmlProcessParser("process-no-begin.xml").parse(), true)).printStackTrace();

        this.assertStandard(factory);
        factory.refresh(new ClassPathXmlProcessParser("process-refresh.xml").parse(), false);
        this.assertStandard(factory);
        this.assertRefresh(factory);

        factory.refresh(new ClassPathXmlProcessParser("process-refresh.xml").parse(), true);
        Assertions.assertThrows(IllegalArgumentException.class, () -> factory.getContext("standard")).printStackTrace();
        this.assertRefresh(factory);
    }

    private void assertStandard(ProcessContextFactory factory) {
        ProcessContext context = factory.getContext("standard");
        context.set("id", 123456);
        context.set("path", new ArrayList<>());
        context.start();
        List<String> expect = Arrays.asList("A", "B", "C");
        Assertions.assertIterableEquals(expect, context.get("path"));
    }

    private void assertRefresh(ProcessContextFactory factory) {
        ProcessContext context = factory.getContext("refresh");
        context.set("id", 123456);
        context.set("path", new ArrayList<>());
        context.start();
        List<String> expect = Arrays.asList("A", "B", "C", "D");
        Assertions.assertIterableEquals(expect, context.get("path"));
    }
}
