package moe.ahao.spring.boot.powermock;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberMatcher;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;

/**
 * 要 Mock 私有方法、final方法、构造方法, 就不能用 Spring
 * 并且要控制 mockito-core 版本, 否则会抛出 NoSuchMethodError
 *
 * https://github.com/powermock/powermock/issues/1125
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({StringService.class})
public class Junit4FeatureTest {
    @Test
    public void mockFinal() {
        StringService stringService = PowerMockito.mock(StringService.class);

        String expect = "world";
        PowerMockito.when(stringService.toLowerCaseFinal(Mockito.anyString())).thenReturn(expect);

        String str = "HELLO";
        Assert.assertEquals(expect, stringService.toLowerCaseFinal(str));
    }

    @Test
    public void mockPrivate1() throws Exception{
        StringService stringService = PowerMockito.spy(new StringService());

        String expect = "hello";
        PowerMockito.when(stringService, "toLowerCasePrivate", Mockito.anyString()).thenReturn(expect);

        String str = "HELLO";
        Assert.assertEquals(expect, stringService.toLowerCaseFinal(str));
    }

    @Test
    public void mockPrivate2() throws Exception {
        StringService stringService = PowerMockito.spy(new StringService());

        String expect = "hello";
        PowerMockito.stub(MemberMatcher.method(StringService.class, "toLowerCasePrivate")).toReturn(expect);

        String str = "HELLO";
        Assert.assertEquals(expect, stringService.toLowerCasePrivateFacade(str));
    }

    @Test
    public void mockStatic() {
        PowerMockito.mockStatic(StringService.class);

        String expect = "hello";
        PowerMockito.when(StringService.toLowerCaseStatic(Mockito.anyString())).thenReturn(expect);

        String str = "HELLO";
        Assert.assertEquals(expect, StringService.toLowerCaseStatic(str));
    }

    @Test
    public void mockConstruct() throws Exception {
        BigDecimal mock = new BigDecimal("1000");
        PowerMockito.whenNew(BigDecimal.class).withArguments(Mockito.anyString()).thenReturn(mock);

        BigDecimal bigDecimal = new BigDecimal("0");
        Assert.assertEquals(0, mock.compareTo(bigDecimal));
    }
}
