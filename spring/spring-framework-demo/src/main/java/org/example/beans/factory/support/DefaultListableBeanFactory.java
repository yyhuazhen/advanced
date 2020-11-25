package org.example.beans.factory.support;

import org.apache.commons.collections.CollectionUtils;
import org.dom4j.Document;
import org.example.beans.factory.ConfigurableListableBeanFactory;
import org.example.beans.factory.config.BeanDefinition;
import org.example.beans.factory.resource.Resources;
import org.example.beans.factory.utils.DocumentReader;
import org.example.beans.factory.xml.XmlBeanDefinitionBuilder;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultListableBeanFactory implements ConfigurableListableBeanFactory {
    private Map<String, BeanDefinition> beanDefinitions =new HashMap<>();

    public DefaultListableBeanFactory(String location) {
        //1 解析xml配置文件,通过一个专门的类来解析
        XmlBeanDefinitionBuilder builder = new XmlBeanDefinitionBuilder(this);
        InputStream inputStream = Resources.genInputStream(location);
        Document document = DocumentReader.genDocument(inputStream);
        List<BeanDefinition> results = builder.parseBeanDefinition(document);
        if (CollectionUtils.isNotEmpty(results)) {
            results.stream().forEach(each->{
                if(!beanDefinitions.containsKey(each.getId())){
                    beanDefinitions.put(each.getId(), each);
                }else {
                    throw new RuntimeException("配置文件有误，请检查您的配置文件");
                }
            });
        }
        System.out.println(beanDefinitions.toString());
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
