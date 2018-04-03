package com.hal.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.hal.utils.FileUtils;

public class ParseXmlDemo {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		// TODO Auto-generated method stub
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(FileUtils.getOneFiles(FileUtils.PATH));
		Element root = getRoot(doc); // 得到根元素
		NodeList childres = getChildresList(root); // 根节点下的子元素 （name || size）
		// loopNode(root);
		System.out.println("childs:"+childres.getLength());
		for (int i = 0; i < childres.getLength(); i++) {
			Node child = childres.item(i);
			System.out.println(child.getNodeName()+":"+(child instanceof Element));
			if (child instanceof Element) {
				Element childElement = (Element) child;
				System.out.println(child.hasChildNodes());
				Text textNode = (Text) child.getFirstChild();
				String text = textNode.getData().trim(); 
				if (text == null)
					continue;
//				System.out.println(text);
			}
		}
		/*
		 * List<String> arraylList = getNodeText(root); for (int i = 0; i <
		 * arraylList.size(); i++) { System.out.println(arraylList.get(i)); }
		 */

	}

	public static Element getRoot(Document doc) {
		return doc.getDocumentElement();
	}

	public static NodeList getChildresList(Node node) {
		return node.getChildNodes();
	}

	public static List<String> getNodeText(Node node) {
		List<String> textList = new ArrayList<String>();
		String text = "";
		NodeList childresList = getChildresList(node);
		for (int i = 0; i < childresList.getLength(); i++) {
			Node child = childresList.item(i);
			if (child instanceof Element) {
				Element childElement = (Element) child;
				Text textNode = (Text) child.getFirstChild(); // 得到节点的第一个文本节点
				text = textNode.getData().trim(); // 得到文本节点的值 忽略空白格
				if (text == null)
					continue;
				textList.add(text);
			}
		}
		return textList;
	}

	public static Map<String, ?> getText(Node root) {
		Map<String, ?> map = new LinkedHashMap<>();
		if (null != root.getChildNodes()) {

		}
		return map;
	}

	public static void loopNode(Element root) {
		NodeList childs = root.getChildNodes();
		if (null == childs || childs.getLength() < 0) {
			return;
		}
		for (int i = 0; i < childs.getLength(); i++) {
			Node node = childs.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				loopNode(root);
			} else if (node.getNodeType() == Node.COMMENT_NODE) {
				continue;
			} else if (node.getNodeType() == Node.TEXT_NODE) {
				Text textNode = (Text) node.getFirstChild();
				if (null != textNode) {
					String text = textNode.getData().trim();
					System.out.println(text);
				}
			}
		}
	}
}
