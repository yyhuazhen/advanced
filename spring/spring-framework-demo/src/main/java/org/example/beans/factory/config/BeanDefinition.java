package org.example.beans.factory.config;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BeanDefinition {
    private String id;
    private Class<?> classType;
    private String initMethod;
    private String destroyMethod;
    private List<PropertyValue> propertyValues;
}
