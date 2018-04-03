package com.hal.structure.map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class MapDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
				HashMap<Integer,String> map = new HashMap<Integer,String>();
//				System.out.println(1<<30);
				for (int i = 0; i < 10; i++) {
					map.put(i, "0"+i);
				}
				Set<Map.Entry<Integer, String>> entry = map.entrySet();
				Iterator<Map.Entry<Integer, String>> iterator = entry.iterator();
				while (iterator.hasNext()) {
						Entry<Integer, String> item = iterator.next();
						System.out.println("key:"+item.getKey());
						System.out.println("value:"+item.getValue());
				}
	}

}
