package com.hal.test;

public class A {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		A.InnerA innerA = new A().new  InnerA();
		A a = new A();
		a.print();
		innerA.print("A3");
	}
   
	private void print(){
		System.out.println(this);
	}
	private String a = "A1";

	private class InnerA {

		private String a = "A2";

		private void print(String a){
		      System.out.print(a);  //这里打出的方法传过来的参数
		      System.out.print(this.a); //这里打出来的是内部类中的a,也就是A2
		      System.out.print(A.this.a);//这里传的是当前内部类中外包含类的a  也就是A1
		    }
	}
}
