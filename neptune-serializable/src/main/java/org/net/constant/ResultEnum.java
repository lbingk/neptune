package org.net.constant;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-02 17:17
 */
public enum ResultEnum {
    /**
     * 失败
     */
    IS_FAIL(0, "失败"),
    /**
     * 成功
     */
    IS_SUCESS(1, "成功");


    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    private Integer type;
    private String desc;

    ResultEnum(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

}
