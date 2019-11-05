package org.net.io.reference;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: neptune
 * @description:
 * @author: luobingkai
 * @create: 2019-11-05 19:58
 */
@Setter
@Getter
@ToString
public class DefaultFuture {
    private DefaultFuture() {
    }

    public DefaultFuture(Request request) {
        DefaultFuture defaultFuture = new DefaultFuture();
        defaultFuture.setRequest(request);
        defaultFutureMap.put(request.mid, this);
    }

    public static final Map<Long, DefaultFuture> defaultFutureMap = new ConcurrentHashMap<>();

    private final Lock lock = new ReentrantLock();

    private final Condition done = lock.newCondition();

    public Request request;

    public Response response;

    public int timeout;

    /**
     * 异步转同步返回远程调用结果
     *
     * @param timeout
     * @return
     */
    public Object get(int timeout) throws Exception {
        if (!isDone()) {
            long currentTimeMillis = System.currentTimeMillis();
            lock.lock();
            try {
                while (!isDone()) {
                    done.await(timeout, TimeUnit.MILLISECONDS);
                    if (isDone() || timeout < (System.currentTimeMillis() - currentTimeMillis)) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
            if (!isDone()) {
                throw new Exception("请求时间过长......");
            }
            return doParseResponse();
        }
        return doParseResponse();
    }

    /**
     * 解析返回的结果
     *
     * @return
     */
    private Object doParseResponse() {
        Response res = response;
        if (res == null) {
            throw new IllegalStateException("response cannot be null");
        }
        if (res.getStatus() == Response.OK) {
            return res.getContent();
        }
        if (res.getStatus() == Response.SERVER_TIMEOUT) {
            throw new RuntimeException("服务端执行时间过长......");
        }
        throw new RuntimeException(res.getErrorMessage());
    }

    private boolean isDone() {
        return response != null;
    }


}
