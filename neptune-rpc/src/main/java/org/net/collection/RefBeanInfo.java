package org.net.collection;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname RefBeanInfo
 * @author: LUOBINGKAI
 * @Description 存储远程调用的对象
 * @Date 2019/11/3 18:05
 */
public class RefBeanInfo {
    private static RefBeanInfo singleton = new RefBeanInfo();

    private RefBeanInfo() {
    }

    public static RefBeanInfo getSingleton() {
        return singleton;
    }
    private List<String> refBeanDefinationClassNameList = new ArrayList<>();

    public List<String> getRefBeanDefinationClassNameList() {
        return refBeanDefinationClassNameList;
    }

    public void addRefBeanDefinationClassName(String refBeanDefinationClassName)  {
        refBeanDefinationClassNameList.add(refBeanDefinationClassName);
    }
    
    
}
