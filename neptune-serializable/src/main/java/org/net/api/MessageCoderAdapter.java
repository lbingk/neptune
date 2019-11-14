package org.net.api;

import io.netty.channel.ChannelInboundHandler;
import org.net.api.msgpack.MsgpackDecoder;
import org.net.api.msgpack.MsgpackEncoder;

/**
 * @Classname MessageDecoderAdpter
 * @author: LUOBINGKAI
 * @Description 解码器适配器
 * @Date 2019/11/14 23:21
 */
public class MessageCoderAdapter {
    private MessageDecoderHandler messageDecoderHandler;
    private MessageEncoderHandler messageEncoderHandler;

    public MessageCoderAdapter(String protocolType) {
        if ("msgpack".equals(protocolType)) {
            messageDecoderHandler = new MsgpackDecoder();
            messageEncoderHandler = new MsgpackEncoder();
        }
        if (protocolType == null) {
            //TODO:默认走JDK
        }
    }

    public static final int MESSAGE_LENGTH = 4;

    /**
     * 获取解码器
     */
    public ChannelInboundHandler decode() {
        return messageDecoderHandler;
    }

    /**
     * 获取编码器
     */
    public ChannelInboundHandler encode() {
        return messageDecoderHandler;
    }
}
