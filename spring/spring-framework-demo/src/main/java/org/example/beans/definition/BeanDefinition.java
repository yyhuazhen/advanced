package org.example.beans.definition;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
@Builder
public class BeanDefinition {
    private static final String SINGLETON = "singleton";
    private static final String PROTOTYPE = "prototype";
    private static final String THREAD = "thread";
    private String id;
    private String className;
    private String initMethod;
    private String destroyMethod;
    private String scope;
    private List<PropertyValue> propertyValues;

    public boolean isSingleton() {
        //默认为singleton
        return StringUtils.isNotBlank(this.scope) || StringUtils.equals(this.scope, SINGLETON);
    }

    public boolean isScope() {
        return StringUtils.equals(this.scope, PROTOTYPE);
    }

    public boolean isThread(){
        return StringUtils.equals(this.scope, THREAD);
    }
}
