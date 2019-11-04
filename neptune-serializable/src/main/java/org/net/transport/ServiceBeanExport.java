package org.net.transport;

import lombok.*;


/**
 * @Classname ServiceBeanExport
 * @author: LUOBINGKAI
 * @Description 暴露服务实体信息
 * @Date 2019/11/2 23:55
 */


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ServiceBeanExport {
    private String interfaceClass;
    private StringBuilder methods = new StringBuilder(16);
}
