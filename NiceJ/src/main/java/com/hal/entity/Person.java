package com.hal.entity;

import java.time.LocalDate;

public class Person implements BaseEntity {

	public enum Sex {
		MALE, FEMALE
	}

	String name;
	LocalDate birthday;
	public Sex gender;
	String emailAddress;
	int age;
    public Person(String name,LocalDate date, Sex sex,String email, int age){
    	this.name = name;
    	this.birthday = date;
    	this.gender = sex;
    	this.emailAddress = email;
    	this.age = age;
    }
	
	public void printPerson() {
		System.out.println(toString());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public Sex getGender() {
		return gender;
	}

	public void setGender(Sex gender) {
		this.gender = gender;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", birthday=" + birthday + ", gender=" + gender + ", emailAddress=" + emailAddress + ", age=" + age
				+ "]";
	}

}