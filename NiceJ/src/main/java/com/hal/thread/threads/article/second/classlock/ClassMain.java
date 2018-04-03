package com.hal.thread.threads.article.second.classlock;

public class ClassMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClassService service1 = new ClassService();
		ClassService service2 = new ClassService();
		ClassA classA = new ClassA(service1);
		classA.setName("ClassA");
		classA.start();
		ClassB classB = new ClassB(service1);
		classB.setName("ClassB");
		classB.start();
		ClassC classC = new ClassC(service1);
		classC.setName("ClassC");
		classC.start();
	}

}
