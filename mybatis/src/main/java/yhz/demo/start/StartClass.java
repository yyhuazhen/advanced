package yhz.demo.start;

import org.junit.Test;
import yhz.demo.conf.Configration;
import yhz.demo.conf.DataSource;
import yhz.demo.conf.XmlConfigBuilder;
import yhz.demo.io.XmlInputStream;

import java.io.InputStream;

public class StartClass {
    @Test
    public void test() {
        //1 读取全局配置文件
        String xmlConfigPath = "source/mybatis-config.xml";
        InputStream in = XmlInputStream.getInputStreamAsResources(xmlConfigPath);
        //3 根据流解析XML 并将解析到的xml文件内容封装在xmlConfigration对象中并返回
        XmlConfigBuilder builder = new XmlConfigBuilder();
        Configration configration = builder.phaseConfigration(in);
        DataSource dataSource = configration.getDataSource();
        System.out.println(configration.toString());
        System.out.println(dataSource.toString());
    }

    @Test
    public void testRegex() {
        String str = "${db.name}";
        int start = str.indexOf("${");
        int end = str.indexOf("}");
        String substring = str.substring(start + 2, end);
        System.out.println(substring);
    }
}
