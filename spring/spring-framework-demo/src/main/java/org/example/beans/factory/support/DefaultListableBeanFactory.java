package org.example.beans.factory.support;

import org.example.beans.definition.BeanDefinition;
import org.example.beans.factory.ConfigurableListableBeanFactory;
import org.example.beans.factory.registry.BeanDefinitionRegistry;

import java.util.HashMap;
import java.util.Map;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry,ConfigurableListableBeanFactory {
    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();


    @Override
    public BeanDefinition getBeanDefinition(String id) {
        return this.beanDefinitions.get(id);
    }

    @Override
    public void registryBeanDefinition(String id, BeanDefinition definition) {
        this.beanDefinitions.put(id, definition);
    }

    @Override
    protected BeanDefinition getDefinition(String id) {
        return getBeanDefinition(id);
    }
}
