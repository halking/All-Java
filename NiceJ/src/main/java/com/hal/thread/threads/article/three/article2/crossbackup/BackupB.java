package com.hal.thread.threads.article.three.article2.crossbackup;

public class BackupB extends Thread {
	private DBackup backup;

	public BackupB(DBackup backup) {
		super();
		this.backup = backup;
	}

	@Override
	public void run() {
		backup.backupB();
	}
}
