package org.example.beans.resource;

import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;

public class Resources {
    public static InputStream genInputStream(String location){
        if(StringUtils.isNotBlank(location)){
           return Resources.class.getClassLoader().getResourceAsStream(location);
        }
        return null;
    }
}
