package org.example.beans.factory.xml;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.example.beans.factory.BeanFactory;
import org.example.beans.factory.config.PropertyValue;

import java.util.ArrayList;
import java.util.List;

public class XmlPropertyValueBuilder {
    private BeanFactory beanFactory;

    public XmlPropertyValueBuilder(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public List<PropertyValue> parsePropertys(List<Element> property) {
        //TODO 如果property为空则继续创建list有点浪费资源，用流处理并待优化
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
            if (StringUtils.isNotBlank(value) && StringUtils.isNotBlank(ref)) {
                throw new RuntimeException("property error beacause ref and value only one, please your property");
            }
            return PropertyValue.builder().name(name).value(value).ref(ref).build();
        }
        return null;
    }
}
