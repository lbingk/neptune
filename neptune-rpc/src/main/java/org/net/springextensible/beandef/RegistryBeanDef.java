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
public class RegistryBeanDef {
    private String ip;
    private int port;
    private int timeout;

}
