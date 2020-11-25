package org.example.test;

import org.example.beans.factory.BeanFactory;
import org.example.beans.factory.support.DefaultListableBeanFactory;
import org.example.pojo.Student;

public class TestSpringFramework {

    public static void main(String[] args) {
        String location = "beans.xml";
        BeanFactory beanFactory = new DefaultListableBeanFactory(location);

        Student student = (Student) beanFactory.getBean("student");
        System.out.println(student);
    }

}
