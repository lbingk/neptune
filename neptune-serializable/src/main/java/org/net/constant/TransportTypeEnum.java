package org.net.constant;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-02 17:17
 */
public enum TransportTypeEnum {
    /**
     * 心跳检测
     */
    HEART_BEAT(0, "心跳检测"),
    /**
     * 服务注册
     */
    REGISTRY(10, "服务注册"),
    /**
     * 服务订阅
     */
    SUBSCRIBE(20, "服务订阅"),
    /**
     * 服务订阅结果
     */
    SUBSCRIBE_RESULT(30, "服务订阅结果"),
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
