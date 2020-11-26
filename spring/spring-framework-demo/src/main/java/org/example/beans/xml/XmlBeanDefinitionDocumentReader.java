package org.example.beans.xml;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.example.beans.definition.BeanDefinition;
import org.example.beans.definition.PropertyValue;
import org.example.beans.definition.RuntimeBeanReference;
import org.example.beans.definition.TypedStringValue;
import org.example.beans.factory.registry.BeanDefinitionRegistry;

import java.util.ArrayList;
import java.util.List;

public class XmlBeanDefinitionDocumentReader {
    private BeanDefinitionRegistry registry;

    public XmlBeanDefinitionDocumentReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public List<PropertyValue> parsePropertys(List<Element> property) {
        List<PropertyValue> propertyValues = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(property)) {
//            propertyValues = new ArrayList<>();
            property.stream().forEach(each -> {
                PropertyValue propertyValue = parseProperty(each);
                propertyValues.add(propertyValue);
            });
        }
        return propertyValues;
    }

    private PropertyValue parseProperty(Element element) {
        if (element != null) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            String ref = element.attributeValue("ref");
            Object objValue = null;
            if (StringUtils.isNotBlank(value) && StringUtils.isNotBlank(ref)) {
                throw new RuntimeException("property error beacause ref and value only one, please your property");
            } else if (StringUtils.isNotBlank(value)) {
                //TODO 基本类型处理流程
                objValue = TypedStringValue.builder().value(value).build();
            } else if (StringUtils.isNotBlank(ref)) {
                //TODO 引用类型处理流程
                objValue = RuntimeBeanReference.builder().ref(ref).build();
            }
            return PropertyValue.builder().name(name).value(objValue).build();
        }
        return null;
    }

    public void locationBeanDefinition(Document document) {
        if (document != null) {
            Element rootElement = document.getRootElement();
            List<Element> beans = rootElement.elements("bean");
            beans.stream().forEach(each -> {
                BeanDefinition beanDefinition = parseBeanDefinition(each);
                registry.registryBeanDefinition(beanDefinition.getId(), beanDefinition);
            });
        }
    }

    private BeanDefinition parseBeanDefinition(Element element) {
        if (element != null) {
            String id = element.attributeValue("id");
            String strClassType = element.attributeValue("class");
            String initMethod = element.attributeValue("init-method");
            List<Element> property = element.elements("property");
            XmlBeanDefinitionDocumentReader builder = new XmlBeanDefinitionDocumentReader(this.registry);
            List<PropertyValue> propertyValues = builder.parsePropertys(property);
            return BeanDefinition.builder().id(id).className(strClassType).initMethod(initMethod).propertyValues(propertyValues).build();
        }
        return null;
    }
}
