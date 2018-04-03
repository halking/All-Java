package com.hal.thread.threads.article.second;

public class MethodThreadB extends Thread {
		private WithinMethodVariable variable;
		public MethodThreadB(WithinMethodVariable variable) {
			// TODO Auto-generated constructor stub
			super();
			this.variable = variable;
		}
		@Override
		public void run() {
		// TODO Auto-generated method stub
			super.run();
			variable.add("b");
		}
}
