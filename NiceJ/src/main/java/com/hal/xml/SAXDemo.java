package com.hal.xml;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hal.utils.ReadWriterUtils;

public class SAXDemo{

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		// TODO Auto-generated method stub
   final StringBuffer  sb  = new StringBuffer();
	String  urlString  = "http://www.w3c.org";
	 URL  url = new URL(urlString);
      SAXParserFactory  saxFactory = SAXParserFactory.newInstance();
      saxFactory.setNamespaceAware(true);
      SAXParser   saxParser = saxFactory.newSAXParser();
      DefaultHandler  handler = new DefaultHandler(){
    	 @Override
    	public void startElement(String uri, String localName, String qName,
    			Attributes attributes) throws SAXException {
    		// TODO Auto-generated method stub
    		   if (localName.equalsIgnoreCase("a")&&attributes!=null) {
				 for (int i = 0; i < attributes.getLength(); i++) {
					String  name = attributes.getLocalName(i);
					if (name.equalsIgnoreCase("href")) {
						System.out.println(attributes.getValue(i));
						sb.append(attributes.getValue(i)+"\n");
					}
				}
			}
    	}  
	  };
	  InputStream  ins = url.openStream();
	  saxParser.parse(ins,handler);
		try {
			ReadWriterUtils.WriterContentToFile(sb.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
