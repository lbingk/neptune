package org.net.transport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @program: neptune
 * @description:
 * @author: luobingkai
 * @create: 2019-11-07 17:28
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubscribeResult implements Serializable {
    private String interfaceClassName;
    private Set<String> ipAddrAndPortSets = new CopyOnWriteArraySet<>();
}
