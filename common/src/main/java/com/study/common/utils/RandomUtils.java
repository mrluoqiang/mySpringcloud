package com.study.common.utils;

import java.util.Random;
import java.util.UUID;

public class RandomUtils {
    /**
     *生成uuid
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 随机生成三位数验证码
     * @return
     */
    public static int getRandomNum3(){
        Random r = new Random();
        return r.nextInt(900)+100;
    }
}
