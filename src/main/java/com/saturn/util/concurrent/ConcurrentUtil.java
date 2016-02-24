package com.saturn.util.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

/**
 * 提供统一线程池入口
 * 
 * @author ericcoderr@gmail.com
 */
public class ConcurrentUtil {

    public static final CallerRunsPolicy callerRunsPolicy = new CallerRunsPolicy();
    public static final int CORE_PROCESSOR_NUM = Runtime.getRuntime().availableProcessors();
    private static ScheduledExecutorService daemonExecutor;
    private static ExecutorService defaultExecutor;
    private static ExecutorService defaultFixExecutor;
    private static final String PREFIX = "";// ConcurrentUtil.class.getSimpleName() +"-";

    private static final ScheduledExecutorService watchdog = new ScheduledThreadPoolExecutor(1, new NamedThreadFactory(PREFIX + "Watchdog(Sche)", Thread.NORM_PRIORITY, true));

    public static boolean threadSleep(long millis) {
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException e1) {
            return false;
        }
    }

    /**
     * <pre>
     * 后台调度线程池 注意:通过这个线程创建的线程都是后台线程,如果没有非后台线程存在了,程序会立即退出
     */
    public static ScheduledExecutorService getDaemonExecutor() {
        if (daemonExecutor == null) {
            synchronized (ConcurrentUtil.class) {
                if (daemonExecutor == null) {
                    daemonExecutor = Executors.newScheduledThreadPool(CORE_PROCESSOR_NUM, new NamedThreadFactory(PREFIX + "Daemon(Sche)", Thread.NORM_PRIORITY, true));
                }
            }
        }
        return daemonExecutor;
    }

    public static ExecutorService getDefaultExecutor() {
        if (defaultExecutor == null) {
            synchronized (ConcurrentUtil.class) {
                if (defaultExecutor == null) {
                    defaultExecutor = Executors.newCachedThreadPool(new NamedThreadFactory(PREFIX + "Default", Thread.NORM_PRIORITY));
                }
            }
        }
        return defaultExecutor;
    }

    public static ExecutorService getDefaultFixExecutor(int threads) {
        if (defaultFixExecutor == null) {
            synchronized (ConcurrentUtil.class) {
                if (defaultFixExecutor == null) {
                    defaultFixExecutor = Executors.newFixedThreadPool(threads, new NamedThreadFactory(PREFIX + "Default", Thread.NORM_PRIORITY));
                }
            }
        }
        return defaultFixExecutor;
    }

    public static ScheduledExecutorService getWatchdog() {
        return watchdog;
    }

    /**
     * 私有的构造方法
     */
    private ConcurrentUtil() {
    }
}
