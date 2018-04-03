package com.hal.thread.threads.article.second;

public class SynThreadB extends Thread {
	private SynObject object;
	public SynThreadB(SynObject  object) {
		// TODO Auto-generated constructor stub
		super();
		this.object=object;
	}
	@Override
	public void run() {
	// TODO Auto-generated method stub
	super.run();
	object.methodB();
	}
}
