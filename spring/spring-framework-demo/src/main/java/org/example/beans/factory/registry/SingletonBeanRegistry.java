package org.example.beans.factory.registry;

/**
 * 管理单例bean注册的接口
 */
public interface SingletonBeanRegistry {
    Object getBean(String id);

    void registryBean(String id, Object bean);
}
