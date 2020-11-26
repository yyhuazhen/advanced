package org.example.beans.definition;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RuntimeBeanReference {
    private String ref;
}
