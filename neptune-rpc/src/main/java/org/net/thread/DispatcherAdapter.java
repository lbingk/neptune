package org.net.thread;

import org.net.springextensible.beandefinition.ProtocolBean;
import org.net.thread.direct.DirectDispatcher;
import org.net.thread.message.MessageDispatcher;
import org.net.util.SpringContextHolder;

/**
 * @program: neptune
 * @description: 线程模型适配器，单例模式：懒汉式
 * @author: luobingkai
 * @create: 2019-11-15 11:43
 */
public class DispatcherAdapter {


    private static DispatcherAdapter instance;

    private DispatcherAdapter() {
    }

    public static synchronized DispatcherAdapter getInstance() {
        if (instance == null) {
            instance = new DispatcherAdapter();
        }
        return instance;
    }

    /**
     * 默认是io事件由io线程来处理，业务事件由业务线程池来处理
     */
    private static final String MESSAGE = "message";
    private static final String All = "all";
    private static final String DIRECT = "direct ";

    public Dispatcher getDispatcher() {
        ProtocolBean serviceProtocol = SpringContextHolder.getBean("serviceProtocol");
        String executorType = serviceProtocol.getExecutorType() == null ? MESSAGE : serviceProtocol.getExecutorType();
        switch (executorType) {
            case All:
                // TODO
                break;
            case DIRECT:
                this.dispatcher = new DirectDispatcher();
                break;
            default:
                this.dispatcher = new MessageDispatcher();
                break;
        }
        return dispatcher;
    }

    private Dispatcher dispatcher;

}
