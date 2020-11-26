package org.example.beans.utils;

import org.apache.commons.lang3.StringUtils;

public class ReflectUtis {

    public static Class<?> getClass(String className) {
        if(StringUtils.isNotBlank(className)){
            Class<?> aClass = null;
            try {
                aClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return aClass;
        }
        return null;
    }

    public static Object newInstance(Class<?> type) {
        if(type!=null){
            try {
                return type.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
