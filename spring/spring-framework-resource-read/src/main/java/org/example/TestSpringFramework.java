package org.example;


import org.example.pojo.Student;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;
import java.util.List;

public class TestSpringFramework {

    public static void main(String[] args) {
        String location = "beans.xml";
//        ApplicationContext ac = new ClassPathXmlApplicationContext(location);
//        Student c = ac.getBean("student", Student.class);
//        System.out.println(c.toString());
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinitions(new ClassPathResource(location));
        Student student = (Student) factory.getBean("student");
        System.out.println(student);
    }
}
