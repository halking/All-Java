package com.hal.thread.threads.article.three.article2.crossbackup;

public class BackupMain {

	public static void main(String[] args) {
		DBackup backup = new DBackup();
		for (int i = 0; i < 20; i++) {
			BackupA backupA = new BackupA(backup);
			backupA.start();
			BackupB backupB = new BackupB(backup);
			backupB.start();
		}
	}

}
