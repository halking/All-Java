package com.hal.concurrent.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.hal.common.utils.Global;

public class ThreadPoolMain {

	static Runnable runnable=new Runnable(){public void run(){try{System.out.println(Thread.currentThread().getName()+"--running--"+System.currentTimeMillis());Thread.sleep(1000);}catch(InterruptedException e){
	// TODO: handle exception
	}}};

	public static void main(String[] args) throws Exception {

		// TODO Auto-generated method stub
		// cacheTest();
		// LinkedDequeTestA();
		// SynchronousQueueTestA();
		// CallableService<String> service = new CallableService<String>(100);
		// String t = service.call();
		ExecutorService executorService = Executors.newCachedThreadPool();
		try {
	
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 100000; j++) { }
					
			List<Future<Integer>> list = CallableTest(i,executorService);
			}	

			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			executorService.shutdown();
		}
	}

	public static void cacheTest() {
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < Global.FIVE_COUNT; i++) {
			executorService.execute(new CachePoolRunnable("i--" + i));
		}
		try {
			Thread.sleep(2000);
			System.out.println("\n\n");
			for (int i = 0; i < Global.FIVE_COUNT; i++) {
				// executorService.execute(new CachePoolRunnable("i--" + i));
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
		}
	}

	/**
	 * ----queue:LinkedBlockingDeque ----1、Thread <corePool
	 * ----2、corePool<Thread <max ----3、Thread >corePool && thread>max
	 */
	public static void LinkedDequeTestA() {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 5, 5, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>());
		executor.execute(runnable);
		executor.execute(runnable);
		executor.execute(runnable);
		executor.execute(runnable);
		executor.execute(runnable);
		executor.execute(runnable);
		try {
			Thread.sleep(300);
			System.out.println("A:" + executor.getCorePoolSize());
			System.out.println("A:" + executor.getPoolSize());
			System.out.println("A:" + executor.getQueue().size());
			Thread.sleep(10000);
			System.out.println("B:" + executor.getCorePoolSize());
			System.out.println("B:" + executor.getPoolSize());
			System.out.println("B:" + executor.getQueue().size());
		} catch (InterruptedException e) {
			// TODO: handle exception
		}
	}

	/**
	 * ----queue:SynchronousQueue ----1、Thread <corePool ----2、corePool<Thread
	 * <max ----3、Thread >corePool && thread>max
	 */
	public static void SynchronousQueueTestA() {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 5, 5, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		executor.execute(runnable);
		executor.execute(runnable);
		executor.execute(runnable);
		executor.execute(runnable);
		executor.execute(runnable);
		executor.execute(runnable);
		try {
			Thread.sleep(300);
			System.out.println("A:" + executor.getCorePoolSize());
			System.out.println("A:" + executor.getPoolSize());
			System.out.println("A:" + executor.getQueue().size());
			Thread.sleep(10000);
			System.out.println("B:" + executor.getCorePoolSize());
			System.out.println("B:" + executor.getPoolSize());
			System.out.println("B:" + executor.getQueue().size());
		} catch (InterruptedException e) {
			// TODO: handle exception
		}
	}

	public static <T> List<Future<Integer>> CallableTest(final int i, ExecutorService executorService) throws InterruptedException {

		List<Future<Integer>> list = new ArrayList<Future<Integer>>();
		try {
			try {
				CallableService<Integer> service = new CallableService<Integer>(i);
				// ThreadPoolExecutor executor = new
				// ThreadPoolExecutor(3,4,5L,TimeUnit.SECONDS,new
				// LinkedBlockingDeque());
				Future<Integer> future = executorService.submit(service);
				// System.out.println(executorService.getQueue().size() +
				// ">>>"+executorService.getCompletedTaskCount()+">>>>"+executorService.getTaskCount()+">>>"+executorService.getActiveCount());
//				System.out.println(future.get());
//				System.out.println(">>>>>>>> " + i);
				list.add(future);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			// TODO: handle exception
		} finally {

		}
		return list;
	}

	public static <T> void CallableTest1() throws InterruptedException {
		try {
			CallableService<T> service = new CallableService<T>(100);
			T t = service.call();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
