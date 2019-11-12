package org.net.util;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @program: neptune
 * @description:
 * @author: luobingkai
 * @create: 2019-11-09 16:12
 */
public class ReflectionUtil {

    class Student{}
    class Teacher{}

    public Map<Student,Teacher> getXXPP(){
        return null;
    }


    public static Class<?>[] getReturnType(Class<?> clz, Method method) {
        Type type = method.getGenericReturnType();
        if (type instanceof ParameterizedType) {
            System.out.println(type);
            Type[] typesto = ((ParameterizedType) type).getActualTypeArguments();

            Class<?>[] classes = new Class[typesto.length];
            for (int i = 0; i < typesto.length; i++) {
                classes[i] = (Class) typesto[i];
            }
            return classes;
        }
        return new Class[]{method.getReturnType()};
    }


    public static void main(String[] args) throws NoSuchMethodException {
        Class<?>[] getXXPPS = ReflectionUtil.getReturnType(ReflectionUtil.class, ReflectionUtil.class.getMethod("getXXPP"));
    }

}
