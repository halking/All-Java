package com.hal.thread.threads.article.three.article2.crossbackup;

public class BackupA extends Thread {
		private DBackup backup;
		
		public BackupA(DBackup backup) {
			super();
			this.backup = backup;
		}
		
		@Override
		public void run() {
			backup.backupA();
		}
}
