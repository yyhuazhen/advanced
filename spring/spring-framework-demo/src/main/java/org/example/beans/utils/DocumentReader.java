package org.example.beans.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

public class DocumentReader {
    public static Document genDocument(InputStream is) {
        if (is != null) {
            SAXReader reader = new SAXReader();
            try {
                org.dom4j.Document document = reader.read(is);
                return document;
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
