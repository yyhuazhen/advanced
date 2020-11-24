package org.example.beans.factory;
/**
 * 模拟spring 顶级接口向BeanFactory靠拢
 * */
public interface BeanFactory {
    Object getBean(String student);
}
