package com.hal.thread.threads.article.second;

public class WithinMethodMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WithinMethodVariable variable1 = new WithinMethodVariable();
		WithinMethodVariable variable2 = new WithinMethodVariable();
		MethodThreadB variableThread = new MethodThreadB(variable1);
		variableThread.start();
		MethodThreadA methodThreadA = new MethodThreadA(variable2);
		methodThreadA.start();
		
	}

}
