package org.example.beans.definition;

import com.sun.tracing.dtrace.ArgsAttributes;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TypedStringValue {
    private String value;
    private Class<?> classType;
}
