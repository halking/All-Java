package com.hal.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.core.io.ClassPathResource;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class XPathUtilities {

	public static String getXmlString(Node node) {
		return XmlUtilities.nodeToString(node);
	}

	public static Node getXmlNode(String xpathExpression, String xmlContent) {
		try {
//			XPathFactory xPathfactory = new XPathFactoryImpl();
			XPathFactory xPathfactory = XPathFactory.newInstance(); 
			XPath xpath = xPathfactory.newXPath();
			xpath.setNamespaceContext(new EsiNamespaceContext());
			XPathExpression expr = xpath.compile(xpathExpression);
			Document document = XmlUtilities.parseDocument(xmlContent);
			return (Node) expr.evaluate(document, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}

	public static Node getXmlNode(String xpathExpression, Document document) {
		try {
//			XPathFactory xPathfactory = new XPathFactoryImpl();s
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			xpath.setNamespaceContext(new EsiNamespaceContext());
			XPathExpression expr = xpath.compile(xpathExpression);
			return (Node) expr.evaluate(document, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getXmlString(String xpathExpression, String xmlContent) {
		Document document = XmlUtilities.parseDocument(xmlContent);
		return getXmlString(xpathExpression, document);
	}

	public static String getXmlString(String xpathExpression, Document document) {
		try {
//			XPathFactory xPathfactory = new XPathFactoryImpl();s
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			xpath.setNamespaceContext(new EsiNamespaceContext());
			XPathExpression expr = xpath.compile(xpathExpression);
			return (String) expr.evaluate(document, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getXmlContent(String xpathExpression, Document document) {
		try {
//			XPathFactory xPathfactory = new XPathFactoryImpl();s
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			xpath.setNamespaceContext(new EsiNamespaceContext());
			XPathExpression expr = xpath.compile(xpathExpression);
			return getXmlString((Node) expr.evaluate(document,
					XPathConstants.NODE));
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getXmlString(String xpathExpression, Node template) {
		String xmlContent = getXmlString(template);
		Node xmlNode = getXmlNode(xpathExpression, xmlContent);
		return getXmlString(xmlNode);
	}

	public static NodeList getNodeList(Document doc, String expr) {
		try {
//			XPathFactory xPathfactory = new XPathFactoryImpl();s
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			xpath.setNamespaceContext(new EsiNamespaceContext());
			XPathExpression pathExpr = xpath.compile(expr);
			return (NodeList) pathExpr.evaluate(doc, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}

	}

	public static Node getNode(Document doc, String expr) {
		try {
//			XPathFactory xPathfactory = new XPathFactoryImpl();s
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			xpath.setNamespaceContext(new EsiNamespaceContext());
			XPathExpression pathExpr = xpath.compile(expr);
			return (Node) pathExpr.evaluate(doc, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}

	}

	public static NodeList getNodeList(Node rootNode, String expr) {
		try {
//			XPathFactory xPathfactory = new XPathFactoryImpl();s
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			xpath.setNamespaceContext(new EsiNamespaceContext());
			XPathExpression pathExpr = xpath.compile(expr);
			return (NodeList) pathExpr.evaluate(rootNode,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}

	}

	// extracts the String value for the given expression
	public static String getValue(Node n, String expr) {
		try {
//			XPathFactory xPathfactory = new XPathFactoryImpl();s
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			xpath.setNamespaceContext(new EsiNamespaceContext());
			XPathExpression pathExpr = xpath.compile(expr);
			return (String) pathExpr.evaluate(n, XPathConstants.STRING);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}

	public static Node getNode(InputStream in, String xpathExpression)
			throws Exception {
//		XPathFactory xPathfactory = new XPathFactoryImpl();s
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		xpath.setNamespaceContext(new EsiNamespaceContext());
		InputSource inputSource = new InputSource(in);
		return (Node) xpath.evaluate(xpathExpression, inputSource,
				XPathConstants.NODE);
	}

	public static Node getNode(String content, String xpathExpression)
			throws Exception {

//		XPathFactory xPathfactory = new XPathFactoryImpl();s
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		xpath.setNamespaceContext(new EsiNamespaceContext());
		InputSource inputSource = new InputSource(new StringReader(content));
		return (Node) xpath.evaluate(xpathExpression, inputSource,
				XPathConstants.NODE);
	}

	public static Node getNode(InputStream in, String xpathExpression,
			boolean removeNamespace) throws Exception {
		if (removeNamespace) {
			StringWriter sw = new StringWriter();
			ClassPathResource resource = new ClassPathResource(
					"remove-namespace.xsl");
			XsltHelper.transform(in, resource.getInputStream(), sw);
			return getNode(sw.toString(), xpathExpression);
		} else {
			return getNode(in, xpathExpression);
		}
	}

	public static Node getNode(String content, String xpathExpression,
			boolean removeNamespace) {
		try {
			if (removeNamespace) {
				StringWriter sw = new StringWriter();
				ClassPathResource resource = new ClassPathResource(
						"remove-namespace.xsl");
				XsltHelper.transform(
						new ByteArrayInputStream(content.getBytes()),
						resource.getInputStream(), sw);
				return getNode(sw.toString(), xpathExpression);
			} else {
				return getNode(content, xpathExpression);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @param isWatchExp
	 * @param product
	 * @return
	 */
	public static boolean isTrue(String isWatchExp, Node n) {
		try {
//			XPathFactory xPathfactory = new XPathFactoryImpl();s
			XPathFactory xPathfactory = XPathFactory.newInstance();
			XPath xpath = xPathfactory.newXPath();
			xpath.setNamespaceContext(new EsiNamespaceContext());
			XPathExpression pathExpr = xpath.compile(isWatchExp);
			return (Boolean) pathExpr.evaluate(n, XPathConstants.BOOLEAN);
		} catch (XPathExpressionException e) {
			throw new RuntimeException(e);
		}
	}
}
