package org.example.beans.definition;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PropertyValue {
    private String name;
    private Object value;
}
