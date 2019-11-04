package org.net.constant;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-02 17:17
 */
public enum TransportTypeEnum {
    /**
     * 服务心跳检测
     */
    REGISTRY_HEART_BEAT(0, "心跳检测"),
    /**
     * 订阅心跳检测
     */
    SUBSCRIBE_HEART_BEAT(1, "心跳检测"),
    /**
     * 服务注册
     */
    REGISTRY(10, "服务注册"),
    /**
     * 服务订阅
     */
    SUBSCRIBE(20, "服务订阅"),
    ;


    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    private Integer type;
    private String desc;

    TransportTypeEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

}
