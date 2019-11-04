package org.net;

import org.net.collection.ServiceBeanInfo;
import org.net.springextensible.beandef.RefBeanDef;
import org.net.springextensible.beandef.ServiceBeanDef;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;

import java.util.Map;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-02 11:57
 */
@SpringBootApplication()
@ImportResource(locations = {"classpath:neptune.xml"})
public class NeptuneProviderWebApplication {
    public static void main(String[] args) throws Exception {
      SpringApplication.run(NeptuneProviderWebApplication.class, args);

    }

}
