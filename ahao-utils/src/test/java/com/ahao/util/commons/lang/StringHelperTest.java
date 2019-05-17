package com.ahao.util.commons.lang;

import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static com.ahao.util.commons.lang.StringHelper.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringHelperTest {

    @Test
    public void urlEncodeTest() {
        Assert.assertEquals("hello+world", urlEncode("hello world", StandardCharsets.UTF_8));
        Assert.assertEquals("%E4%BD%A0%E5%A5%BD", urlEncode("你好", StandardCharsets.UTF_8));
        Assert.assertEquals("%E4%B8%96%E7%95%8C", urlEncode("世界", StandardCharsets.UTF_8));
        Assert.assertEquals("%3C%2Fxml%3E", urlEncode("</xml>", StandardCharsets.UTF_8));
        Assert.assertEquals("%7B%22abc%22%3A%22123%22%7D", urlEncode("{\"abc\":\"123\"}", StandardCharsets.UTF_8));
    }


    // ====================================== 汉字处理相关 ==================================================
    @Test
    public void containChineseTest() {
        assertTrue(containChinese("你好, world"));
        assertTrue(containChinese("你好, 世界"));
        assertTrue(containChinese("hello world！"));
        assertTrue(containChinese("！￥……（）——：；“”‘’《》，。？、"));


        assertFalse(containChinese("やめて"));
        assertFalse(containChinese("한글"));
        assertFalse(containChinese("!@#$%^&*()_+{}[]|\"'?/:;<>,."));
        assertFalse(containChinese("www.micmiu.com"));
        assertFalse(containChinese("hello world!"));
        assertFalse(containChinese("hello world"));
    }

    @Test
    public void isChineseTest() {
        assertTrue(isChinese('我'));
        assertTrue(isChinese('錒'));
        assertTrue(isChinese('櫂'));
        assertTrue(isChinese('呢'));
        assertTrue(isChinese('。'));
        assertTrue(isChinese('，'));
        assertTrue(isChinese('　'));
        assertTrue(isChinese('！'));
        assertTrue(isChinese('？'));

        assertFalse(isChinese('a'));
        assertFalse(isChinese('j'));
        assertFalse(isChinese('&'));
        assertFalse(isChinese('-'));
        assertFalse(isChinese('='));
        assertFalse(isChinese('/'));
        assertFalse(isChinese('`'));
        assertFalse(isChinese('~'));
        assertFalse(isChinese('+'));
        assertFalse(isChinese(' '));
    }
    // ====================================== 汉字处理相关 ==================================================

}