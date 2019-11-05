package org.net.io.reference;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @program: neptune
 * @description:
 * @author: luobingkai
 * @create: 2019-11-05 20:06
 */
@Setter
@Getter
public class Response implements Serializable {


    /**
     * ok.
     */
    public static final byte OK = 20;

    /**
     * SERVER side timeout.
     */
    public static final byte SERVER_TIMEOUT = 30;

    public Object content;

    public String errorMessage;

    /**
     * 对应请求唯一id
     */
    public long mid = 0;

    public byte Status;
}
