package yhz.demo.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

public class XmlDocumentUtils {

    public static Document phaseDocument(InputStream in) {
        SAXReader saxReader = new SAXReader();
        try {
            return saxReader.read(in);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }
}
