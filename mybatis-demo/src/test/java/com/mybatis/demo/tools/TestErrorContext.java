package com.mybatis.demo.tools;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yanggy
 */
@Slf4j
public class TestErrorContext {

    @Test
    public void testErrorContext() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            service.execute(() -> {
                try {
                    ErrorContext instance = ErrorContext.instance();
                    instance.activity("第" + finalI + "个流程中")
                        .object(this.getClass().getName())
                        .resource("xxx 的资源");
                    if (new Random().nextInt(10) > 6) {
                        int m = 1 / 0;
                    }
                } catch (Exception e) {
                    throw ExceptionFactory.wrapException("has errors", e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
    }
}
