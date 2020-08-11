package moe.ahao.sonar;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MainTests {
    @Test
    public void test() {
        Main main = new Main();
        Assertions.assertEquals(0, main.add(null, null));
        Assertions.assertEquals(1, main.add(1, null));
        Assertions.assertEquals(2, main.add(null, 2));
        Assertions.assertEquals(3, main.add(1, 2));
    }
}
