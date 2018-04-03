package com.hal.base;

public class C extends B {
    
	public int x;
	public String s = "a";
	Integer   integer  = 1; 
	public C() {
		// TODO Auto-generated constructor stub
		System.out.println("C");
	}
	public void say() {
		System.out.println("hello C!");
	}
	 public static void main(String[] args) {
	  		// TODO Auto-generated method stub
		 int i = 5;
		 int j = 10;
		 System.out.println(i + ~j);
	  		A   a = new A();
	  		B   b = new B();
	  		B  c = new C();
	  		A a1 = b;
	  		c.say();
	  	}

}
