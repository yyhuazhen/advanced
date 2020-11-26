package org.example.test;


import org.example.beans.factory.support.DefaultListableBeanFactory;
import org.example.beans.xml.XmlBeanDefinitionReader;
import org.example.pojo.Student;

public class TestSpringFramework {

    public static void main(String[] args) {
        String location = "beans.xml";
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.locationBeanDefinition(location);
        Student student = (Student) beanFactory.getBean("student");
        System.out.println(student);
    }

}
