package org.example.beans.factory.registry.support;

import org.example.beans.factory.registry.SingletonBeanRegistry;

import java.util.HashMap;

public class DefaultSingletonBeanRegistry extends SimpleAliasRegistry implements SingletonBeanRegistry {
    private HashMap<String, Object> cache = new HashMap<>();

    @Override
    public Object getBean(String id) {
        return cache.get(id);
    }

    @Override
    public void registryBean(String id, Object bean) {
        this.cache.put(id, bean);
    }
}
