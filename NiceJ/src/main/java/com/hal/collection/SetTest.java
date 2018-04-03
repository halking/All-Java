package com.hal.collection;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SetTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Set<String> col = new HashSet<String>(10);
		for (int i = 0; i < 10; i++) {
			col.add(i + "a");
			System.out.println(col.size());
		}
      
		/*
		 * for (String colSet : col) { col.remove(colSet); }
		 */
		for (Iterator iterator = col.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			if (true) {
				iterator.remove();
			}
		}
		for (String string : col) {
			System.out.println(string);
		}
	}

}
