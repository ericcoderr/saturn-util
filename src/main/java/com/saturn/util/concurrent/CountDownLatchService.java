/** ******CountDownLatchService.java*****/
/**
 *Copyright
 *
 **/
package com.saturn.util.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * @describe: <pre>
 * </pre>
 * @date :2015年4月9日 上午11:52:30
 * @author : ericcoderr@gmail.com
 */
public class CountDownLatchService {

    /**
     * 把任务分解成多个处理
     * 
     * @param N N 表示分解任务个数
     * @param worker 要处理的任务实现,实现类只需要实现doWork方法。要处理的任务
     */
    public static void decomposeTask(int N, AbstractWorker worker) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(N);
        worker.setCountDownLatch(latch);
        for (int i = 0; i < N; i++) {
            new Thread(worker).start();
        }
        latch.await();
    }
}
