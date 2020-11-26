package org.example.beans.factory.registry;

import org.example.beans.definition.BeanDefinition;

/**
 * 管理BeanDefinition注册的接口
 * */
public interface BeanDefinitionRegistry extends AliasRegisty {
    BeanDefinition getBeanDefinition(String id);

    void registryBeanDefinition(String id, BeanDefinition definition);
}
