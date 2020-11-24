package org.example.beans.factory.support;

import org.example.beans.factory.ConfigurableListableBeanFactory;
import org.example.beans.factory.config.BeanDefinition;
import org.example.beans.factory.config.XmlBeanDefinitionBuilder;

import java.util.Map;

public class DefaultListableBeanFactory implements ConfigurableListableBeanFactory {
    private Map<String, BeanDefinition> beanDefinitions;

    public DefaultListableBeanFactory(String location) {
        //1 解析xml配置文件,通过一个专门的类来解析
        XmlBeanDefinitionBuilder builder = new XmlBeanDefinitionBuilder(this);
        BeanDefinition beanDefinition = builder.parseBeanDefinition(location);
        if (null != beanDefinitions && beanDefinition != null && !beanDefinitions.containsKey(beanDefinition.getId())) {
            beanDefinitions.put(beanDefinition.getId(), beanDefinition);
        }
    }

    @Override
    public Object getBean(String student) {
        //读取缓存
        //当从缓存中没有找到想要的bean对象，再去执行创建流程
        //创建流程（根据BeanDefinition对象去创建）	---	Bean实例对象
        //bean的实例化（调用构造方法new对象）
        //bean的属性填充（调用set方法设置属性）
        //bean的初始化（调用初始化方法，完成一些操作）
        return null;
    }

}
