package com.hal.xml;

import java.io.File;
import java.util.Map;

import org.w3c.dom.Node;
import org.xml.sax.helpers.DefaultHandler;

public interface BaseXML<K,V> {
	
	public void parse(File file,DefaultHandler handler);
	public Map<K,V> parse(Node node);
	public void parse(String path);
	public void transform(String path);
	
}
