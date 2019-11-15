package org.net.thread;

import org.net.springextensible.beandefinition.ProtocolBean;
import org.net.util.SpringContextHolder;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: neptune
 * @description: 线程池工厂
 * @author: luobingkai
 * @create: 2019-11-15 11:53
 */
public class TreadPoolFactory implements ApplicationContextAware {

    public static ExecutorService getExecutorService() {
        return executorService;
    }

    private static ExecutorService executorService;
    /**
     * 默认的线程池类型是Limit,线程数是200
     */
    private static final String FIXED = "fixed";
    private static final int FIXED_THREAD_NUM = 200;

    private static final String CACHE = "cache";
    private static final String SINGLE = "single";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        executorService = create();
    }

    /**
     * 根据配置的线程池参数返回对应的线程
     *
     * @return
     */
    private ExecutorService create() {
        ProtocolBean protocolBean = SpringContextHolder.getBean("serviceProtocol");
        int threadNum = protocolBean.getThreadNum() == 0 ? FIXED_THREAD_NUM : protocolBean.getThreadNum();

        ExecutorService executorService;
        switch (protocolBean.getExecutorType()) {
            case CACHE:
                executorService = Executors.newCachedThreadPool();
                break;
            case SINGLE:
                executorService = Executors.newSingleThreadExecutor();
                break;
            default:
                executorService = Executors.newFixedThreadPool(threadNum);
                break;
        }
        return executorService;
    }
}
