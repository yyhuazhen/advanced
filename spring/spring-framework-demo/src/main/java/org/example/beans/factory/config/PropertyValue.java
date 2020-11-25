package org.example.beans.factory.config;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PropertyValue {
    private String name;
    private String value;
    private String ref;
}
