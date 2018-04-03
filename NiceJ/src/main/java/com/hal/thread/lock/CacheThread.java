package com.hal.thread.lock;

public class CacheThread extends Thread {
		private CacheData cacheData;
		
		public CacheThread(CacheData cacheData) {
			super();
			this.cacheData = cacheData;
		}
		@Override
		public void run() {
			for (int i = 0; i < 3; i++) {
				cacheData.processData("i-"+i);
			}
		}
}
