package org.net.constant;

/**
 * @program: neptune
 * @description:
 * @author: luobingkai
 * @create: 2019-11-02 17:17
 */
public enum TransportTypeEnum {
    /**
     * 服务注册
     */
    REGISTRY(1, "服务注册");

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
