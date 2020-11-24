package org.example.beans.factory.config;

import lombok.Data;

import java.util.List;
@Data
public class BeanDefinition {
    private String id;
    private String classType;
    private List<PropertyValue> properties;
}
