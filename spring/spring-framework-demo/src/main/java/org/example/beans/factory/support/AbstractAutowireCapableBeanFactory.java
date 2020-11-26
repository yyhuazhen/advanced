package org.example.beans.factory.support;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.beans.constant.PrimitiveEnum;
import org.example.beans.definition.BeanDefinition;
import org.example.beans.definition.PropertyValue;
import org.example.beans.definition.RuntimeBeanReference;
import org.example.beans.definition.TypedStringValue;
import org.example.beans.factory.AutowireCapableBeanFactory;
import org.example.beans.utils.ReflectUtis;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    @Override
    public Object genBean(BeanDefinition definition) {
        if (definition != null) {
            String className = definition.getClassName();
            Class<?> aClass = ReflectUtis.getClass(className);
            Object o = genBean(aClass);
            //bean的属性填充（调用set方法设置属性）
            populateBean(aClass, o, definition);
            //bean的初始化（调用初始化方法，完成一些操作）
            initBean(aClass, o, definition);
            return o;
        }
        return null;
    }

    private void populateBean(Class<?> classType, Object o, PropertyValue propertyValue) {
        String name = propertyValue.getName();
        //根据属性名获取值，如果是基础类型则根据属性类型设置，如果是应用类型则引用,问题是？？？---如果字段是枚举等该怎么处理？
        if (StringUtils.isNotBlank(name)) {
            if (propertyValue.getValue() instanceof TypedStringValue) {
                fixFiledByValue(classType, o, propertyValue);
            } else if (propertyValue.getValue() instanceof RuntimeBeanReference) {
                fixFiledByRef(classType, o, propertyValue);
            }
        }

    }

    private void fixFiledByValue(Class<?> classType, Object o, PropertyValue propertyValue) {
        //通过属性值设置filed
        Field field = null;
        try {
            field = classType.getField(propertyValue.getName());
        } catch (NoSuchFieldException e) {
            try {
                field = classType.getDeclaredField(propertyValue.getName());
            } catch (NoSuchFieldException noSuchFieldException) {
                noSuchFieldException.printStackTrace();
            }
            field.setAccessible(true);
        }
        if (field != null) {
            Class<?> type = field.getType();
            Object obj = parseValueByTypeedAndString(type, (TypedStringValue) propertyValue.getValue());
            try {
                field.set(o, obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void fixFiledByRef(Class<?> classType, Object o, PropertyValue propertyValue) {
        //根据引用类型设置filed
        Object bean = getBean(((RuntimeBeanReference) propertyValue.getValue()).getRef());
        Field field = null;
        try {
            field = classType.getField(propertyValue.getName());

        } catch (NoSuchFieldException e) {
            try {
                field = classType.getDeclaredField(propertyValue.getName());
            } catch (NoSuchFieldException noSuchFieldException) {
                noSuchFieldException.printStackTrace();
            }
            field.setAccessible(true);

        }
        if (field != null) {
            Class<?> type = field.getType();
            if (bean.getClass() == type) {
                try {
                    field.set(o, bean);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object parseValueByTypeedAndString(Class<?> type, TypedStringValue value) {
        PrimitiveEnum primitiveEnum = PrimitiveEnum.fromClassType(type.getName());
        return primitiveEnum.getPrimitiveValue(value);
    }

    private Object genBean(Class<?> type) {
        Object obj = ReflectUtis.newInstance(type);
        return obj;
    }


    protected void initBean(Class<?> aClass, Object bean, BeanDefinition definition) {
        //TODO BeanPostProcessor bean初始化前后流程，Spring AOP就是通过这个实现的
        if (StringUtils.isNotBlank(definition.getInitMethod())) {
            Method method = null;
            try {
                method = aClass.getMethod(definition.getInitMethod());
            } catch (NoSuchMethodException e) {
                try {
                    method = aClass.getDeclaredMethod(definition.getInitMethod());
                } catch (NoSuchMethodException noSuchMethodException) {
                    noSuchMethodException.printStackTrace();
                }
                method.setAccessible(true);
            }
            if (method != null) {
                try {
                    method.invoke(bean);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        if (StringUtils.isNotBlank(definition.getDestroyMethod())) {
            //TODO bean的销毁方法应该在什么时机执行，应该不是在创建的时候，所以不应该在此处处理
        }
    }

    protected void populateBean(Class<?> aClass, Object bean, BeanDefinition definition) {
        List<PropertyValue> propertyValues = definition.getPropertyValues();
        if (CollectionUtils.isNotEmpty(propertyValues)) {
            propertyValues.stream().forEach(each -> {
                populateBean(aClass, bean, each);
            });
        }
    }

    public static void main(String[] args) {
        String name = Integer.class.getName();
        System.out.println(name);
    }
}
