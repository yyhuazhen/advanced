package org.example.beans.xml;

import org.dom4j.Document;
import org.example.beans.factory.registry.BeanDefinitionRegistry;
import org.example.beans.resource.Resources;
import org.example.beans.utils.DocumentReader;

import java.io.InputStream;

public class XmlBeanDefinitionReader {
    private BeanDefinitionRegistry registry;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    public BeanDefinitionRegistry getBeanDefinitionRegistry() {
        return registry;
    }


    public void locationBeanDefinition(String location) {
        //获取配置文件流
        InputStream inputStream = Resources.genInputStream(location);
        //根据流获取Document对象
        Document document = DocumentReader.genDocument(inputStream);
        //解析Document文件
        XmlBeanDefinitionDocumentReader reader = new XmlBeanDefinitionDocumentReader(registry);
        reader.locationBeanDefinition(document);
    }

}
