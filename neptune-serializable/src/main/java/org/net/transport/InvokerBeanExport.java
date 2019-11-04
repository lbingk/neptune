package org.net.transport;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


/**
 * @Classname InvokerBeanExport
 * @author: LUOBINGKAI
 * @Description 暴露服务实体信息
 * @Date 2019/11/2 23:55
 */


@Getter
@Setter
@ToString
@NoArgsConstructor
public class InvokerBeanExport {
    private String interfaceClass;
    private StringBuilder methods = new StringBuilder(16);

    public InvokerBeanExport(Class<?> interfaceClass) {
        this.interfaceClass = interfaceClass.getName();
        for (int i = interfaceClass.getMethods().length - 1; i >= 0; i--) {
            methods.append(interfaceClass.getMethods()[i]);
            if (i != 0) {
                methods.append("&");
            }
        }
    }
}
