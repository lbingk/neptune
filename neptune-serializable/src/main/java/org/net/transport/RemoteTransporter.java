package org.net.transport;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.msgpack.annotation.Message;

import java.io.Serializable;

/**
 * @Classname RemoteTransporter
 * @Description 定义通信实体
 * @Date 2019/11/1 23:50
 * @Created by admin
 */
@Message
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RemoteTransporter implements Serializable {

    /**
     * 消息ID
     */
    private String transId;

    /**
     * 消息实体
     */
    private String transContent = "transContent";

    /**
     * 消息类型
     */
    private Integer transType;

    /**
     * 地址
     */
    private String ipAddrAndPort;


    public RemoteTransporter(String transId, String ipAddrAndPort, String transContent, Integer transType) {
        this.transId = transId;
        this.ipAddrAndPort = ipAddrAndPort;
        this.transContent = transContent;
        this.transType = transType;
    }

    private RemoteTransporter(String transId, String ipAddrAndPort, Integer transType) {
        this.transId = transId;
        this.ipAddrAndPort = ipAddrAndPort;
        this.transType = transType;
    }

    /**
     * 返回消息实例对象
     *
     * @param transId
     * @param transContent
     * @param transType
     * @return
     */
    public static RemoteTransporter create(String transId, String ipAddrAndPort, String transContent, Integer transType) {
        return new RemoteTransporter(transId, ipAddrAndPort, transContent, transType);
    }

    public static RemoteTransporter create(String transId, String ipAddrAndPort, Integer transType) {
        return new RemoteTransporter(transId, ipAddrAndPort, transType);
    }
}
