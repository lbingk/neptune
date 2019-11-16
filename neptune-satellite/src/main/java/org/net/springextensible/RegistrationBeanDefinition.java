package org.net.springextensible;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 自定义bean,匹配spi扩展
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationBeanDefinition {
    private int port;
    private int timeout;
}
