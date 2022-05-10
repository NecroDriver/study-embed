package com.xin.web.utils;

import com.xin.web.utils.crypt.SnowFlake;

/**
 * 工具测试类
 *
 * @author creator mafh 2019/11/25 16:47
 * @author updater
 * @version 1.0.0
 */
public class UtilsTest {

    public static void main(String[] args) {
        SnowFlake snowFlake = new SnowFlake(20, 12);
        for (int i =0;i< 10;i++){
            System.out.println(snowFlake.nextId());
        }
    }
}
