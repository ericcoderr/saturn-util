/** ******NameThreadFactory.java*****/
/**
 *Copyright
 *
 **/
package com.saturn.util.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @describe: <pre>
 * </pre>
 * @date :2013年7月18日 下午8:57:19
 * @author : ericcoderr@gmail.com
 */
public class NamedThreadFactory implements ThreadFactory {

    protected final ThreadGroup group;

    /**
     * 创建有前缀的线程工厂
     * 
     * @param namePrefix
     */
    public NamedThreadFactory(String namePrefix) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.namePrefix = namePrefix;
    }

    /**
     * 线程数量
     */
    protected final AtomicInteger threadNumber = new AtomicInteger(1);
    /**
     * 线程前缀
     */
    protected final String namePrefix;
    /**
     * 默认线程优先级
     */
    protected int priority = Thread.NORM_PRIORITY;
    /**
     * 是否守护线程
     */
    protected boolean daemon = false;

    /**
     * @param namePrefix
     * @param priority
     * @param daemon
     */
    public NamedThreadFactory(String namePrefix, int priority, boolean daemon) {
        this(namePrefix);
        this.daemon = daemon;
        this.priority = priority;
    }

    /**
     * @param namePrefix
     * @param priority
     */
    public NamedThreadFactory(String namePrefix, int priority) {
        this(namePrefix);
        this.priority = priority;
    }

    /**
     */
    @Override
    
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        t.setDaemon(daemon);
        t.setPriority(priority);
        return t;
    }

    public String getNamePrefix() {
        return namePrefix;
    }
}
