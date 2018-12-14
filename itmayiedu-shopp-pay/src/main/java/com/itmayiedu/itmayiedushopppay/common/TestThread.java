package com.itmayiedu.itmayiedushopppay.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.Executors.*;

/**
 * @Auther: 浪里小白龙
 * @Date: 2018/12/14 10:42
 * @Version: 1.0
 * @Description: //TODO  测试线程池的使用
 */
public class TestThread {
    public static void main(String[] args) {
        System.out.println("主线程开始");
        ExecutorService cachedThreadPool = newCachedThreadPool();
        cachedThreadPool.execute(new Runnable(){
            @Override
            public void run() {
                System.out.println("另一条线程开启,独立于主线程");
                try {
                    Thread.sleep(5000);
                    System.out.println("另一条线程结束");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("主线程结束");
    }
}
