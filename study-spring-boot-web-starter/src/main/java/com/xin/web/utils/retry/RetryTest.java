package com.xin.web.utils.retry;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author creator mafh 2019/12/19 16:58
 * @author updater
 * @version 1.0.0
 */
public class RetryTest {

    public static List foo() {
        int rand = (int) (Math.random() * 10);
        // 显示抛出异常
        System.out.println(LocalDateTime.now() + " 调用方法 " + rand);
        rand = rand < 10 ? 0 : rand;
        // 模拟抛出异常
        System.out.println(1 / rand);
        List list = new ArrayList<>();
        list.add("1");
        return list;
    }

    public static void main(String[] args) {
        try {
            RetryUtils.retry(null, 2L, 2, () -> foo(), (List list, Exception e1) -> e1 != null || list == null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
