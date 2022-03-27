package com.fqh.rabbimq.utils;

/**
 * @author 海盗狗
 * @version 1.0
 * 睡眠工具类
 */
public class SleepUtils {

    public static void sleep(int seconds) {

        try {
            Thread.sleep(1000 * seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
