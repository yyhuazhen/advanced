package org.example.beans.factory.config;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.example.beans.factory.BeanFactory;
import org.example.beans.factory.support.DefaultListableBeanFactory;

import java.io.InputStream;

public class XmlBeanDefinitionBuilder {
    private BeanFactory beanFactory;

    public XmlBeanDefinitionBuilder(DefaultListableBeanFactory defaultListableBeanFactory) {
        this.beanFactory = defaultListableBeanFactory;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public BeanDefinition parseBeanDefinition(String location) {
        if (StringUtils.isNotBlank(location)) {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream(location);
            Document document = getDocument(is);
            BeanDefinition beanDefinition = parseBeanDefinition(document);
            return beanDefinition;
        }
        return null;
    }

    private BeanDefinition parseBeanDefinition(Document document) {
        return null;
    }

    private Document getDocument(InputStream is) {
        SAXReader reader = new SAXReader();
        try {
            return reader.read(is);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}
