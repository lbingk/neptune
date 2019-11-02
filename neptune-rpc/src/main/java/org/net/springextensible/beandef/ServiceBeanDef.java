package org.net.springextensible.beandef;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @program: neptune
 * @description:
 * @author: luobingkai
 * @create: 2019-11-02 15:42
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceBeanDef {
    private String service;
    private String ref;
    private String timeout;
}