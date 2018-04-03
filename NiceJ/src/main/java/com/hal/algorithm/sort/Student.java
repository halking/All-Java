/**
 * 
 */
package com.hal.algorithm.sort;

/**
 * @author hal
 *
 */
public class Student {
   private  String  name ;
   private  String  sex;
    public   int age;
/**
 * @param string
 * @param string2
 * @param i
 */
public Student(String string, String string2, int i) {
	// TODO Auto-generated constructor stub
	name =string;
	sex = string2;
	age = i;
}
/**
 * @return the name
 */
public String getName() {
	return name;
}
/**
 * @param name the name to set
 */
public void setName(String name) {
	this.name = name;
}
/**
 * @return the sex
 */
public String getSex() {
	return sex;
}
/**
 * @param sex the sex to set
 */
public void setSex(String sex) {
	this.sex = sex;
}
/**
 * @return the age
 */
public int getAge() {
	return age;
}
/**
 * @param age the age to set
 */
public void setAge(int age) {
	this.age = age;
}
   
}
