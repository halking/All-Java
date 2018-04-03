package com.hal.thread.threads.article.three.article2.crossbackup;

public class DBackup {
	
		private volatile boolean flag = false;
		
		public synchronized void backupA() {
			try {
				while (flag) {
					this.wait();
				}
				for (int i = 0; i < 5; i++) {
					System.out.println("AAAAA");
				}
				this.flag=true;
				notifyAll();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		public synchronized void backupB() {
			try {
				while (!flag) {
					this.wait();
				}
				for (int i = 0; i < 5; i++) {
					System.out.println("BBBBB");
				}
				this.flag=false;
				notifyAll();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
}
