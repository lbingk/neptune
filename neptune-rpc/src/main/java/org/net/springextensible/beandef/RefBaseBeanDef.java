package org.net.springextensible.beandef;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @Classname RefBeanDef
 * @author: LUOBINGKAI
 * @Description TODO
 * @Date 2019/11/3 18:13
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RefBaseBeanDef<T> {
    private String ref;
    private int timeout;

}
