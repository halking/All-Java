package com.hal.entity;

public class EntityMain {

	public static void main(String[] args) {
		Student student = new Student();
		student.setName("huang");
		student.setAge(20);
		setSaa(student);
		System.out.println(student.toString());
	}
	public static void setSaa(Student student) {
		student.setDepart("IT一");
	}
}
