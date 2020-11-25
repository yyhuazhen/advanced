package org.example.beans.factory.xml;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.example.beans.factory.BeanFactory;
import org.example.beans.factory.config.BeanDefinition;
import org.example.beans.factory.config.PropertyValue;
import org.example.beans.factory.support.DefaultListableBeanFactory;

import java.util.ArrayList;
import java.util.List;

public class XmlBeanDefinitionBuilder {
    private BeanFactory beanFactory;

    public XmlBeanDefinitionBuilder(DefaultListableBeanFactory defaultListableBeanFactory) {
        this.beanFactory = defaultListableBeanFactory;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }


    public List<BeanDefinition> parseBeanDefinition(Document document) {
        List<BeanDefinition> beanDefinitions = new ArrayList<>();
        if (document != null) {
            Element rootElement = document.getRootElement();
            List<Element> beans = rootElement.elements("bean");
            beans.stream().forEach(each->{
                BeanDefinition beanDefinition = parseBeanDefinition(each);
                beanDefinitions.add(beanDefinition);
            });
        }
        return beanDefinitions;
    }

    private BeanDefinition parseBeanDefinition(Element element) {
        if (element != null) {
            String id = element.attributeValue("id");
            String strClassType = element.attributeValue("class");
            Class<?> classType = null;
            if (StringUtils.isNotBlank(strClassType)) {
                try {
                    classType = Class.forName(strClassType);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            String initMethod = element.attributeValue("init-method");
            List<Element> property = element.elements("property");
            XmlPropertyValueBuilder builder = new XmlPropertyValueBuilder(this.beanFactory);
            List<PropertyValue> propertyValues = builder.parsePropertys(property);
            return BeanDefinition.builder().id(id).classType(classType).initMethod(initMethod).propertyValues(propertyValues).build();
        }
        return null;
    }

}
