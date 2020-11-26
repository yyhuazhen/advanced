package org.example.beans.factory;

import org.example.beans.factory.registry.SingletonBeanRegistry;

/**
 * 此接口提供了提供了增加接口的功能
 * */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {
}
