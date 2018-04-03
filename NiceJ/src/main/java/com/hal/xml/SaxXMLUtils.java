package com.hal.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hal.utils.FileUtils;

public class SaxXMLUtils {
	private static List<String> list = new ArrayList<>();
	private static Map<String,?> map = new HashMap<>();
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(new File(FileUtils.PATH), new SaxXMLUtils().new SaxHandle());
			for (Iterator it = list.iterator(); it.hasNext();) {
				System.out.println(it.next());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	private class SaxHandle extends DefaultHandler{
		@Override
		public void startElement(String uri, String loacalname, String qname, Attributes paramAttributes)
				throws SAXException {
			// TODO Auto-generated method stub
			System.out.println("start element "+qname);
			if (StringUtils.isNotBlank(qname)) {
				
			}
		}
		@Override
		public void endElement(String paramString1, String paramString2, String paramString3) throws SAXException {
			// TODO Auto-generated method stub
			System.out.println("end element "+paramString2);
		}
		@Override
		public void characters(char[] ch, int start, int len) throws SAXException {
			String temp = new String(ch,start,len);
			if (StringUtils.isNotBlank(temp)) {
				list.add(temp);
			}
 		}
		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			System.out.println("end document ");
		}
		@Override
		public void startDocument() throws SAXException {
			// TODO Auto-generated method stub
			System.out.println("start document ");
		}
	}
}
