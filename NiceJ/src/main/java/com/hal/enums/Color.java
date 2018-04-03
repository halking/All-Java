package com.hal.enums;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public enum Color {
	RED, GREEN, BLUE;
	static final Map<String, Color> map = new HashMap<String, Color>();
	static {
		for (Color c : Color.values()) {
			map.put(c.toString(), c);
		}
	}
	public static void main(String[] args) {
		Set<Map.Entry<String, Color>> entries = map.entrySet();
		for (Iterator item = entries.iterator(); item.hasNext();) {
			Entry<String, Color> entry = (Entry<String, Color>) item.next();
			System.out.println("key="+entry.getKey());
			System.out.println("value="+entry.getValue());
		}
	}
}
