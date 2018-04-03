package com.hal.spring.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hal.structure.linked.StackLinked;

public class IocTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:bean.xml");
		StackLinked stack = (StackLinked)context.getBean("stack");
		stack.push("first");
		String element = (String)stack.pop();
		System.out.println(element);
	}

}
