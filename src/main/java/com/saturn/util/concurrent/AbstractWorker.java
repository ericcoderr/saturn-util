/** ******Worker.java*****/
/**
 *Copyright
 *
 **/
package com.saturn.util.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * @describe:
 * 
 *            <pre>
 *            </pre>
 * 
 * @date :2015年4月9日 上午11:48:25
 * @author : ericcoderr@gmail.com
 */
public abstract class AbstractWorker implements Runnable {

    private CountDownLatch latch;

    public void setCountDownLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            doWork();// 要处理的任务
        } finally {
            latch.countDown();// 完成工作，计数器减一
        }
    }

    // 实现类需要实现此方法，真正要处理的任务
    protected abstract void doWork();

    public void test() {
        
        
    }
}
