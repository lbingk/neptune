package org.net.io.reference;

import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.net.io.common.Response;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: neptune
 * @description: 处理消费端并发访问以及超时问题
 * @author: LUOBINGKAI
 * @create: 2019-11-05 19:58
 */
@Setter
@Getter
@ToString
public class DefaultFuture {

    private final Lock lock = new ReentrantLock();

    private final Condition done = lock.newCondition();

    public Request request;

    public Response response;

    public long startTimestamp = System.currentTimeMillis();

    public int timeout;

    private DefaultFuture() {
    }

    public static final Map<Long, DefaultFuture> DEFAULT_FUTURE_MAP = new ConcurrentHashMap<>();

    public static final Map<Long, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();

    public DefaultFuture(Request request, Channel channel) {
        DefaultFuture defaultFuture = new DefaultFuture();
        defaultFuture.setRequest(request);
        DEFAULT_FUTURE_MAP.put(request.mid, this);
        CHANNEL_MAP.put(request.mid, channel);

        // 创建时间任务具体内容
        TimeoutFutureTask timeoutTask = new TimeoutFutureTask(request.mid);
        timeoutFutureScanner.newTimeout(timeoutTask, timeout, TimeUnit.NANOSECONDS);
    }

    /**
     * 匹配netty提供的时间轮回监控timeoutFutureScanner，时间在规定时间还没有返回结果的future，构建返回结果
     */
    class TimeoutFutureTask implements TimerTask {
        private long mid;

        protected TimeoutFutureTask(long mid) {
            this.mid = mid;
        }

        @Override
        public void run(Timeout timeout) {
            DefaultFuture defaultFuture = DEFAULT_FUTURE_MAP.get(mid);
            if (defaultFuture.isDone() || (System.currentTimeMillis() - defaultFuture.getStartTimestamp()) < defaultFuture.getTimeout()) {
                return;
            }
            // 构建 response 对象
            Response response = new Response();
            response.setMid(mid);
            response.setStatus(Response.SERVER_TIMEOUT);
            response.setErrorMessage(getErrorMessage(defaultFuture));
            defaultFuture.receive(response);
        }
    }

    /**
     * 创建netty提供的时间轮回监控时间
     */
    private static HashedWheelTimer timeoutFutureScanner = new HashedWheelTimer(30, TimeUnit.MILLISECONDS);

    /**
     * 异步转同步设定返回结果,唤醒客户端请求时的线程，并且将返回结果设定到当前的对象:存在返回结果read时调用以及自身定时扫描并发的问题
     *
     * @param response
     */
    public void receive(Response response) {
        if (!isDone()) {
            try {
                lock.lock();
                this.response = response;
                done.signal();
            } finally {
                lock.unlock();
            }
        }
    }

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
            DefaultFuture defaultFuture = DEFAULT_FUTURE_MAP.get(res.getMid());
            Request request = defaultFuture.getRequest();
            return JSON.parseObject(res.getContent(), request.getReturnType());
        }
        if (res.getStatus() == Response.SERVER_TIMEOUT) {
            throw new RuntimeException(response.errorMessage);
        }
        throw new RuntimeException(res.getErrorMessage());
    }

    /**
     * 判断是否已经有返回结果
     *
     * @return
     */
    private boolean isDone() {
        return response != null;
    }

    /**
     * 创建返回超时信息
     *
     * @param defaultFuture
     * @return
     */
    private String getErrorMessage(DefaultFuture defaultFuture) {
        Request request = defaultFuture.getRequest();
        String req = request.getInterfaceClassName() + "." + request.getMethodName() + "." + request.getArgs();
        String errorMsg = req + "执行时间过长：" + (System.currentTimeMillis() - defaultFuture.getStartTimestamp()) + ",已经超出设定值：" + defaultFuture.getTimeout();
        return errorMsg;
    }

}
