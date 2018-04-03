package com.hal.xml;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.hal.entity.Student;
import com.hal.utils.TypeReference;

public class GenerateXMLUtils {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Student student = new Student(new Random().nextInt(), "huang", 22, "IT", new Date());
		List<Student> list = Student.createStudentList();
		Class<Student> clazz = (Class<Student>) student.getClass();
		try {
			buildXMLDoc(student, "d:/tmp/" + clazz.getSimpleName() + ".xml");
			buildXMLDoc(list, "d:/tmp/" + clazz.getSimpleName() + "2.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void buildXMLDoc(Object object, String path) throws IOException{
		Class<Object> clazz = (Class<Object>) object.getClass();
		Field[] fields = clazz.getDeclaredFields();
		Element root = new Element(clazz.getSimpleName().toLowerCase() + "s");
		Document document = new Document(root);
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				buildContent(field, root, object);
			} catch (IllegalArgumentException ie) {
				ie.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		saveXml(document, path);
	}

	public static void buildXMLDoc(Collection<?> collection, String path) {
		Element root = new Element(collection.getClass().getSimpleName().toLowerCase()+"s");
		Document document = new Document();
		for (Object object : collection) {
			Class<Object> clazz = (Class<Object>) object.getClass();
			Field[] fields = clazz.getDeclaredFields();
			Element seconds = new Element(clazz.getSimpleName().toLowerCase());
			for (Field field : fields) {
				field.setAccessible(true);
				try {
					buildContent(field, seconds, object);
				} catch (IllegalArgumentException ie) {
					ie.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			root.addContent(seconds);
		}
		document.addContent(root);
		try {
			saveXml(document, path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveXml(Document document, String path) throws IOException {
		org.jdom.output.Format format = Format.getPrettyFormat();
		XMLOutputter out = new XMLOutputter(format);
		out.output(document, new FileOutputStream(path));
	}

	private static void buildContent(Field field, Element root, Object object) throws IllegalArgumentException, IllegalAccessException {
		switch (field.getType().getSimpleName()) {
		case TypeReference.INT:
			root.addContent(new Element(field.getName()).setText(String.valueOf(field.getInt(object))));
			break;
		case TypeReference.SHORT:
			root.addContent(new Element(field.getName()).setText(String.valueOf(field.getShort(object))));
			break;
		case TypeReference.BYTE:
			root.addContent(new Element(field.getName()).setText(String.valueOf(field.getByte(object))));
			break;
		case TypeReference.CHAR:
			root.addContent(new Element(field.getName()).setText(String.valueOf(field.getChar(object))));
			break;
		case TypeReference.LONG:
			root.addContent(new Element(field.getName()).setText(String.valueOf(field.getLong(object))));
			break;
		case TypeReference.FLOAT:
			root.addContent(new Element(field.getName()).setText(String.valueOf(field.getFloat(object))));
			break;
		case TypeReference.DOUBLE:
			root.addContent(new Element(field.getName()).setText(String.valueOf(field.getDouble(object))));
			break;
		case TypeReference.BOOLEAN:
			root.addContent(new Element(field.getName()).setText(String.valueOf(field.get(object))));
			break;
		default:
			root.addContent(new Element(field.getName()).setText(String.valueOf(field.get(object))));
		}
		
	}
	private static boolean isCustomReference(Field field){
		return true;
	}
	
}
