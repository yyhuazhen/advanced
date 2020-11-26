package org.example.beans.factory.support;

import org.example.beans.definition.BeanDefinition;
import org.example.beans.factory.ConfigurableBeanFactory;

/**
 * 模板方法，提供getbean的方法，如果有则获取 没有则创建，创建流程交给其子类完成，
 */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    @Override
    public Object getBean(String id) {
        //读取缓存  如果没有则创建，有则返回
        Object o = super.getBean(id);
        if (o == null) {
            //根据BeanDefinition创建实例bean,获取BeanDefinition
            BeanDefinition definition = getDefinition(id);
            //根据BeanDefiniton是否为配置的单例，如果是则添加缓存，如果不是则重新创建，默认为单例
            //TODO 在通过BeanDefinition对象创建bean的时候可以通过BeanFactoryPostProcessor对BeanDefinition进行修改
            if(definition.isSingleton()) {
                Object bean = genBean(definition);
                super.registryBean(id, bean);
                return bean;
            }else if(definition.isScope()){
               return genBean(definition);
            }else if(definition.isThread()){
                //TODO 需要清楚scope配为THREAD的时候是怎么处理的
            }
        }
        return o;
    }

    protected abstract BeanDefinition getDefinition(String id);

    public abstract Object genBean(BeanDefinition definition);
}
