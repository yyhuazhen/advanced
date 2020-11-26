package org.example.beans.constant;

import org.apache.commons.lang3.StringUtils;
import org.example.beans.definition.TypedStringValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public enum PrimitiveEnum {
    SHORT("java.lang.Short", new Function<TypedStringValue, Object>() {
        @Override
        public Object apply(TypedStringValue typedStringValue) {
            typedStringValue.setClassType(Short.class);
            if (typedStringValue.getValue().length() > 1) {
                throw new RuntimeException("配置文件中的属性值和类的实际属性不符," + "您配置的value=" + typedStringValue.getValue());
            }
            return Short.valueOf(typedStringValue.getValue());
        }
    }),
    INTEGER("java.lang.Integer", new Function<TypedStringValue, Object>() {
        @Override
        public Object apply(TypedStringValue typedStringValue) {
            typedStringValue.setClassType(Integer.class);
            return Integer.valueOf(typedStringValue.getValue());
        }
    }),
    LONG("java.lang.Long", new Function<TypedStringValue, Object>() {
        @Override
        public Object apply(TypedStringValue typedStringValue) {
            typedStringValue.setClassType(Long.class);
            return Long.valueOf(typedStringValue.getValue());
        }
    }),
    FLOAT("java.lang.Float", new Function<TypedStringValue, Object>() {
        @Override
        public Object apply(TypedStringValue typedStringValue) {
            typedStringValue.setClassType(Float.class);
            return Float.valueOf(typedStringValue.getValue());
        }
    }),
    DOUBLE("java.lang.Double", new Function<TypedStringValue, Object>() {
        @Override
        public Object apply(TypedStringValue typedStringValue) {
            typedStringValue.setClassType(Double.class);
            return Double.valueOf(typedStringValue.getValue());
        }
    }),
    BOOLEAN("java.lang.Boolean", new Function<TypedStringValue, Object>() {
        @Override
        public Object apply(TypedStringValue typedStringValue) {
            typedStringValue.setClassType(Boolean.class);
            return Boolean.valueOf(typedStringValue.getValue());
        }
    }),
    STRING("java.lang.String", new Function<TypedStringValue, Object>() {
        @Override
        public Object apply(TypedStringValue typedStringValue) {
            typedStringValue.setClassType(String.class);
            return typedStringValue.getValue();
        }
    });
    private static Map<String, PrimitiveEnum> valueMap;
    private String classType;
    private Function<TypedStringValue, Object> func;

    PrimitiveEnum(String classType, Function<TypedStringValue, Object> func) {
        this.classType = classType;
        this.func = func;
    }

    public Object getPrimitiveValue(TypedStringValue value) {
        return this.func.apply(value);
    }

    public static PrimitiveEnum fromClassType(String classType) {
        if (valueMap == null) {
            valueMap = new HashMap<>();
            PrimitiveEnum[] values = PrimitiveEnum.values();
            Arrays.stream(values).forEach(each -> {
                valueMap.put(each.classType, each);
            });
        }
        if (StringUtils.isNotBlank(classType)) {
            return valueMap.get(classType);
        }
        return null;
    }
}
