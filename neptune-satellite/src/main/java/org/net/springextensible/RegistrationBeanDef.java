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
public class RegistrationBeanDef {
    private String ip;
    private String port;
    private int timeout;

}
