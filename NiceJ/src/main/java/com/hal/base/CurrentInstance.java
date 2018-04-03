package com.hal.base;

public class CurrentInstance {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CurrentInstance.TestInstance();
	}
//get current class instance in a static method.
	public static void TestInstance() {
		//by SecurityManager method protected getClassContext();
		String instance = new SecurityManager(){
			public String getClassName(){
				return getClassContext()[1].getName();		
				}
		}.getClassName();
		System.out.println(instance);
		//by Throwable method getStackTrace();
		String  instance2 = new Throwable().getStackTrace()[1].getClassName();
		String  mName = new Throwable().getStackTrace()[1].getFileName();
		System.out.println(instance2+"---"+mName);
		//by parse anonymous class name;
		String instance3 = new Object(){
			public String getClassName(){
				String  name = this.getClass().getName();
				return name.substring(0, name.lastIndexOf('$'));
				}
		}.getClassName();
		
		System.out.println(instance3);
		
		
	}
}
