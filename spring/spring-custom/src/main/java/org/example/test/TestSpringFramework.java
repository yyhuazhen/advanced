package org.example.test;

import org.example.beans.factory.BeanFactory;
import org.example.beans.factory.support.DefaultListableBeanFactory;
import org.example.pojo.Student;
import org.junit.jupiter.api.Test;

public class TestSpringFramework {

        @Test
        public void test() throws Exception {
            String location = "beans.xml";
            BeanFactory beanFactory = new DefaultListableBeanFactory(location);

            Student student = (Student) beanFactory.getBean("student");
            System.out.println(student);
        }

}
