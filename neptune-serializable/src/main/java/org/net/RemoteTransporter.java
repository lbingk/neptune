package org.net;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class RemoteTransporter implements Serializable {

    /**
     * 消息ID
     */
    private String transId;

    /**
     * 消息实体
     */
    private String transContent;

    /**
     * 消息类型
     */
    private Integer transType;


    public RemoteTransporter(String transId, String transContent, Integer transType) {
        this.transId = transId;
        this.transContent = transContent;
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
    public static RemoteTransporter create(String transId, String transContent, Integer transType) {
        return new RemoteTransporter(transId, transContent, transType);
    }
}
