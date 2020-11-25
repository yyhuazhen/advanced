package org.example.beans.factory.support;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.example.beans.factory.ConfigurableListableBeanFactory;
import org.example.beans.factory.config.BeanDefinition;
import org.example.beans.factory.config.PropertyValue;
import org.example.beans.factory.resource.Resources;
import org.example.beans.factory.utils.DocumentReader;
import org.example.beans.factory.xml.XmlBeanDefinitionBuilder;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultListableBeanFactory implements ConfigurableListableBeanFactory {
    private Map<String, BeanDefinition> beanDefinitions = new HashMap<>();
    //TODO 如果用map缓存，如果设置多例的时候该怎么缓存，所以需要仔细想想用什么缓存，并可以根据key从缓存中获取对应的bean实例
    private Map<String, Object> beans = new HashMap<>();

    public DefaultListableBeanFactory(String location) {
        //1 解析xml配置文件,通过一个专门的类来解析
        XmlBeanDefinitionBuilder builder = new XmlBeanDefinitionBuilder(this);
        InputStream inputStream = Resources.genInputStream(location);
        Document document = DocumentReader.genDocument(inputStream);
        List<BeanDefinition> results = builder.parseBeanDefinition(document);
        if (CollectionUtils.isNotEmpty(results)) {
            results.stream().forEach(each -> {
                if (!beanDefinitions.containsKey(each.getId())) {
                    beanDefinitions.put(each.getId(), each);
                } else {
                    throw new RuntimeException("配置文件有误，请检查您的配置文件");
                }
            });
        }
        System.out.println(beanDefinitions.toString());
    }

    @Override
    public Object getBean(String student) {
        //读取缓存
        Object o = beans.get(student);
        if (o == null) {
            //当从缓存中没有找到想要的bean对象，再去执行创建流程
            Object bean = genBean(student);
            this.beans.put(student, bean);
            return bean;
        }
        return null;
    }

    private Object genBean(String student) {
        //创建流程（根据BeanDefinition对象去创建）	---	Bean实例对象
        BeanDefinition beanDefinition = this.beanDefinitions.get(student);
        if (beanDefinition != null) {
            //根据classType通过反射创建对象，问题是？？？---单例是怎么保证的,创建的过程中会不会存在并发的问题
            Class<?> classType = beanDefinition.getClassType();
            try {
                Object o = classType.newInstance();
                //TODO 根据构造方法创建实例对象bean
//                Constructor<?> constructor = classType.getConstructor();
                List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();
                //bean的属性填充（调用set方法设置属性）
                fixFiled(classType, o, propertyValues);
                //bean的初始化（调用初始化方法，完成一些操作）
                doSomeMethod(classType, o, beanDefinition);
                return o;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    private void doSomeMethod(Class<?> classType, Object o, BeanDefinition beanDefinition) {
        if (StringUtils.isNotBlank(beanDefinition.getInitMethod())) {
            Method method = null;
            try {
                method = classType.getMethod(beanDefinition.getInitMethod());
            } catch (NoSuchMethodException e) {
                try {
                    method = classType.getDeclaredMethod(beanDefinition.getInitMethod());
                } catch (NoSuchMethodException noSuchMethodException) {
                    noSuchMethodException.printStackTrace();
                }
                method.setAccessible(true);
            }
            if (method != null) {
                try {
                    method.invoke(o);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        if (StringUtils.isNotBlank(beanDefinition.getDestroyMethod())) {
            //TODO bean的销毁方法应该在什么时机执行，应该不是在创建的时候，所以不应该在此处处理
        }
    }

    private void fixFiled(Class<?> classType, Object o, List<PropertyValue> propertyValues) {
        if (CollectionUtils.isNotEmpty(propertyValues)) {
            propertyValues.stream().forEach(each -> {
                fixFiled(classType, o, each);
            });
        }

    }

    private void fixFiled(Class<?> classType, Object o, PropertyValue propertyValue) {
        String name = propertyValue.getName();
        //根据属性名获取值，如果是基础类型则根据属性类型设置，如果是应用类型则引用,问题是？？？---如果字段是枚举等该怎么处理？
        if (StringUtils.isNotBlank(name)) {
            if (StringUtils.isNotBlank(propertyValue.getValue())) {
                fixFiledByValue(classType, o, propertyValue);
            } else if (StringUtils.isNotBlank(propertyValue.getRef())) {
                fixFiledByRef(classType, o, propertyValue);
            }
        }

    }

    private void fixFiledByValue(Class<?> classType, Object o, PropertyValue propertyValue) {
        //通过属性值设置filed
        Field field = null;
        try {
            field = classType.getField(propertyValue.getName());
            if (field == null) {
                field = classType.getDeclaredField(propertyValue.getName());
                field.setAccessible(true);
            }
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
            Object obj = parseValueByType(type, propertyValue.getValue());
            try {
                field.set(o, obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void fixFiledByRef(Class<?> classType, Object o, PropertyValue propertyValue) {
        //根据引用类型设置filed
        Object bean = getBean(propertyValue.getRef());
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

    private Object parseValueByType(Class<?> type, String value) {
        //TODO 判断是否为基础类型
        if (type == Integer.class) {
            return Integer.valueOf(value);
        } else if (type == Character.class) {
            if (value.length() > 1) {
                throw new RuntimeException("配置文件中的属性值和类的实际属性不符,实际属性类型为:" + type + "，您配置的value=" + value);
            }
            return Character.valueOf(value.toCharArray()[0]);
        } else if (type == Short.class) {
            return Short.valueOf(value);
        } else if (type == Long.class) {
            return Long.valueOf(value);
        } else if (type == Boolean.class) {
            return Boolean.valueOf(value);
        } else if (type == Float.class) {
            return Float.valueOf(value);
        } else if (type == Double.class) {
            return Double.valueOf(value);
        } else if (type == String.class) {
            return value;
        } else {
            throw new RuntimeException("配置文件中的属性值和类的实际属性不符,实际属性类型为:" + type + "，您配置的value=" + value);
        }
    }
}
