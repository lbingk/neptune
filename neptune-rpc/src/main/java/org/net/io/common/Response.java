package org.net.io.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-05 20:06
 */
@Setter
@Getter
public class Response implements Serializable {
    /**
     * Ok
     */
    public static final byte OK = 20;

    /**
     * 服务端时间调用过长
     */
    public static final byte SERVER_TIMEOUT = 30;
    /**
     * 不存在此服务提供者
     */
    public static final byte NO_SERVER = 40;
    /**
     * 服务提供者不存在此方法
     */
    public static final byte NO_METHOD = 50;
    /**
     * 服务提供者抛出异常
     */
    public static final byte EXPTION = 60;

    public Object content = new Object();

    public String errorMessage;

    /**
     * 对应请求唯一id
     */
    public long mid = 0;

    public byte Status;
}
