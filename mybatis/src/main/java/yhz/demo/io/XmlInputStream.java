package yhz.demo.io;

import java.io.InputStream;

public class XmlInputStream {
    public static InputStream getInputStreamAsResources(String xmlConfigPath){
        if(xmlConfigPath.isEmpty()||xmlConfigPath==null){
            throw new RuntimeException("xml配置文件路径不能为空或者不能为null");
        }
        return XmlInputStream.class.getClassLoader().getResourceAsStream(xmlConfigPath);
    }

}
