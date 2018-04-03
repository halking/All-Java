package com.hal.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.hal.entity.Person.Sex;

public class Student implements Serializable {
	private Integer id;
	private String name;
	private int age;
	private String depart;
	private Date birthday;
	private String gender;
	private  Person person;
	
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Student() {
		// TODO Auto-generated constructor stub
	}
	public	 Student(Integer id,String name,int age,String depart,Date bir){
		this.id = id;
		this.name= name;
		this.age = age;
		this.depart = depart;
		this.birthday = bir;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getDepart() {
		return depart;
	}

	public void setDepart(String depart) {
		this.depart = depart;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	public String toString(){
		return "name:"+this.name+
				"\n age:"+this.age+"\n depart:"+this.depart;
	}

	public void writeSome(String name){
		System.out.println("name:"+name);
	}
	public void writeSome(String name,String depart){
		System.out.println("name:"+name+"\t depart:"+depart);
	}
	
	public static List<Student> createStudentList(){
		Student student = new Student(new Random().nextInt(), "huang", 22, "IT", new Date());
		Student student1 = new Student(new Random().nextInt(), "wang", 20, "IT", new Date());
		Student student2 = new Student(new Random().nextInt(), "zhang", 40, "IT", new Date());
		Student student3 = new Student(new Random().nextInt(), "li", 30, "IT", new Date());
		Person p1 = new Person("huang",LocalDate.now(), Sex.MALE, "112@qq.com", 21);
		Person p2 = new Person("zhang",LocalDate.now(), Sex.FEMALE, "112@qq.com", 21);
		Person p3 = new Person("wang",LocalDate.now(), Sex.MALE, "112@qq.com", 29);
		student1.setPerson(p1);
		student2.setPerson(p2);
		List<Student> list = new ArrayList<Student>();
		list.add(student);
		list.add(student1);
		list.add(student2);
		list.add(student3);
		return list;
	}
}
