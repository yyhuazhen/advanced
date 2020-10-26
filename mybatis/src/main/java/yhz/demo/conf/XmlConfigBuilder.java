package yhz.demo.conf;

import org.apache.ibatis.io.Resources;
import org.dom4j.Document;
import org.dom4j.Element;
import yhz.demo.utils.XmlDocumentUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class XmlConfigBuilder {
    private Configration configration;
    //存储别的配置文件中的配置内容
    private Properties properties;

    public XmlConfigBuilder() {
        this.configration = new Configration();
    }

    public Configration phaseConfigration(InputStream in) {
        Document document = XmlDocumentUtils.phaseDocument(in);
        Element rootElement = document.getRootElement();
        //解析根元素的properties
        phaseProperties(rootElement.element("properties"));
        //解析根元素enviroments
        phaseEnvironments(rootElement.element("environments"));
        phaseMappers(rootElement.element("mappers"));
        return configration;
    }

    private void phaseProperties(Element propertiesElement) {
        if (propertiesElement != null) {
            String resourcePath = propertiesElement.attributeValue("resource");
            try (InputStream in = Resources.getResourceAsStream(resourcePath)) {
                this.properties = new Properties();
                properties.load(in);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void phaseMappers(Element mappers) {
        //解析mappers标签中引入的文件，并将文件加载后由XmlMapperBuilder解析每个SQL Mapper 文件

    }

    private void phaseEnvironments(Element environments) {
        String defaultValue = environments.attributeValue("default");
        //若defulat为null或者为空的时候 取defualt
        defaultValue = defaultValue == null && defaultValue.equals("") ? "defualt" : defaultValue;
        List<Element> elements = environments.elements();
        for (Element element : elements) {
            String id = element.attributeValue("id");
            //判断是否为指定环境，是就解析 不是跳过
            if (defaultValue.equals(id)) {
                phaseEnviroment(element);
            }
        }
    }

    private void phaseEnviroment(Element element) {
        phaseTransactionManager(element.element("transactionManager"));
        phaseDataSource(element.element("dataSource"));
    }

    private void phaseTransactionManager(Element trsManagerElenment) {
        String type = trsManagerElenment.attributeValue("type");
        configration.getDataSource().setTrsManagerType(type);
    }

    private void phaseDataSource(Element dataSourceElement) {
        String type = dataSourceElement.attributeValue("type");
        configration.getDataSource().setDataSourceType(type);
        List<Element> elements = dataSourceElement.elements();
        pharseProperty(elements);
    }

    private void pharseProperty(List<Element> elements) {
        Properties pro = new Properties();
        for (Element element : elements) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            if (value.contains("${")) {
                //取出${}中的内容，根据内容查询Properties的值，将value替换
                value = (String) this.properties.get(doRePlace(value));
            }
            pro.put(name, value);
        }
        //将解析的属性设置到datasource中
        configration.getDataSource().setDriver((String) pro.get("driver"));
        configration.getDataSource().setUrl((String) pro.get("url"));
        configration.getDataSource().setUserName((String) pro.get("username"));
        configration.getDataSource().setPassword((String) pro.get("password"));
    }

    private String doRePlace(String value) {
        if (!value.contains("}")) {
            throw new RuntimeException("表达式格式有误:" + value);
        }
        int start = value.indexOf("${");
        int end = value.indexOf("}");
        return value.substring(start + 2, end);

    }
}
