package org.net.springextensible.beandefinition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-02 15:42
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegistryBean {
    private String ip;
    private int port;
    private int timeout;

}
