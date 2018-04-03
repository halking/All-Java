/**
 * 
 */
package com.hal.base;

/**
 * @author hal
 *
 */
public class StudentTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
         Person  p1 = new Person();
         System.out.println("person的变量"+p1.str);
         System.out.print("person的输出：");
         p1.print();
         
         Student  student = new Student();
         System.out.println("student的变量："+student.str);
         System.out.print("student的输出:");
         student.print();
         
         Person ps  = new Student();
         System.out.println("变量输出："+ps.str);
         System.out.print("方法的输出：");
         ps.print();
         
	}

}
