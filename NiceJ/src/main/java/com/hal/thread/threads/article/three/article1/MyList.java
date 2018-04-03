package com.hal.thread.threads.article.three.article1;

import java.util.ArrayList;
import java.util.List;

public class MyList {
		private static List<Integer> list = new ArrayList<Integer>();
		
		public static void add() {
			list.add(1);
		}
		public static int size() {
			return list.size();
		}
}	
