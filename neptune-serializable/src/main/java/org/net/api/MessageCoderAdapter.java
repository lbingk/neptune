package org.net.api;

import io.netty.channel.ChannelInboundHandler;
import org.net.api.hessian.HessianDecoder;
import org.net.api.hessian.HessianEncoder;
import org.net.api.jdk.JdkDecoder;
import org.net.api.jdk.JdkEncoder;
import org.net.api.msgpack.MsgpackDecoder;
import org.net.api.msgpack.MsgpackEncoder;

/**
 * @Classname MessageDecoderAdpter
 * @author: LUOBINGKAI
 * @Description 解码器适配器
 * @Date 2019/11/14 23:21
 */
public class MessageCoderAdapter {
    /**
     * 默认走JDK
     */
    private MessageDecoderHandler messageDecoderHandler = new JdkDecoder();
    private MessageEncoderHandler messageEncoderHandler = new JdkEncoder();

    public MessageCoderAdapter(String protocolType) {
        if ("msgpack".equals(protocolType)) {
            messageDecoderHandler = new MsgpackDecoder();
            messageEncoderHandler = new MsgpackEncoder();
        }
        if ("hessian".equals(protocolType)) {
            messageDecoderHandler = new HessianDecoder();
            messageEncoderHandler = new HessianEncoder();
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
