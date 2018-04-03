package com.hal.thread.lock;

import com.hal.thread.lock.read.ReadThreadA;
import com.hal.thread.lock.read.ReadThreadB;
import com.hal.thread.lock.write.WriteThreadA;
import com.hal.thread.lock.write.WriteThreadB;

public class ReadWriteMain {

	public static void main(String[] args) {
//	ReadWriteService service = new ReadWriteService();
//		ReadThreadA threadA = new ReadThreadA(service);
//		threadA.setName("R-Thread-A");
//		ReadThreadB threadB = new ReadThreadB(service);
//		threadB.setName("R-Thread-B");
//		threadA.start();
//		threadB.start();
//		WriteThreadA wthreadA = new WriteThreadA(service);
//		wthreadA.setName("W-Thread-A");
//		WriteThreadB wthreadB = new WriteThreadB(service);
//		wthreadB.setName("W-Thread-B");
//		wthreadA.start();
//		wthreadB.start();
		
		CacheData data = new CacheData();
		Thread[] threads = new Thread[5];
//		CacheThread cacheThread = new CacheThread(data);
		for (int i = 0; i < 5; i++) {
			threads[i] = new CacheThread(data);
			threads[i].setName("Cache-"+i);
			threads[i].start();
		}
	}
}
