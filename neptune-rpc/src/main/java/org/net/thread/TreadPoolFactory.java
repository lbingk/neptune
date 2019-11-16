package org.net.thread;

import lombok.extern.slf4j.Slf4j;
import org.net.springextensible.beandefinition.ProtocolBean;
import org.net.util.SpringContextHolder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: neptune
 * @description: 线程池工厂
 * @author: luobingkai
 * @create: 2019-11-15 11:53
 */
@Slf4j
@Component
public class TreadPoolFactory implements ApplicationListener<ContextRefreshedEvent> {

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

    /**
     * 根据配置的线程池参数返回对应的线程
     *
     * @return
     */
    private ExecutorService create() {
        ProtocolBean serviceProtocol = SpringContextHolder.getBean("serviceProtocol");
        if (serviceProtocol == null) {
            return null;
        }
        int threadNum = serviceProtocol.getThreadNum() == 0 ? FIXED_THREAD_NUM : serviceProtocol.getThreadNum();
        String threadType = serviceProtocol.getThreadType() == null ? FIXED : serviceProtocol.getThreadType();

        ExecutorService executorService;
        switch (threadType) {
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

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.executorService = create();
    }
}
