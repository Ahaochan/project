package com.ahao.commons.util.lang;

import org.junit.Test;

import static com.ahao.commons.util.lang.StringHelper.containChinese;
import static com.ahao.commons.util.lang.StringHelper.isChinese;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringHelperTest {
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