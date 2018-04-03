package com.hal.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.hal.exception.XmlValidationException;

public class XmlUtilities {

	public static void validate(InputStream file, URL xsdUrl)
			throws XmlValidationException {
		Document document = parseDocument(file);
		validate(document, xsdUrl);
	}

	public static void validate(Document document, URL xsdUrl)
			throws XmlValidationException {
		SchemaFactory xsFact = SchemaFactory
				.newInstance(XMLConstants.XML_NS_URI);
		Schema schema;
		Validator validator;
		try {
			schema = xsFact.newSchema(xsdUrl);
			validator = schema.newValidator();
			// validator.setResourceResolver(resourceResolver);
			validator.validate(new DOMSource(document));
		} catch (SAXException e) {
			throw new XmlValidationException(e);
		} catch (IOException e) {
			throw new XmlValidationException(e);
		} finally {
			xsFact = null;
			schema = null;
			validator = null;
		}
	}

	public static Document parseDocument(String xmlContent) throws XmlValidationException {
		ByteArrayInputStream in = new ByteArrayInputStream(
				xmlContent.getBytes());
		Document parseDocument = parseDocument(in);
		in = null;
		return parseDocument;
	}

	public static Document parseDocument(InputStream inputStream)
			throws XmlValidationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		factory.setValidating(false);
		DocumentBuilder builder;
		try {
			// factory.setCoalescing(true);
			builder = factory.newDocumentBuilder();
			return builder.parse(inputStream);
		} catch (ParserConfigurationException e) {
			throw new XmlValidationException(e);
		} catch (SAXException e) {
			throw new XmlValidationException(e);
		} catch (IOException e) {
			throw new XmlValidationException(e);
		} finally {
			factory = null;
			builder = null;
		}

	}

	public static String nodeToString(Node node) throws XmlValidationException {
		Transformer t;
		StringWriter sw;
		try {
			sw = new StringWriter();
			t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
			return sw.toString();
		} catch (TransformerException te) {
			throw new XmlValidationException(te);
		} finally {
			t = null;
			sw = null;
		}

	}

	public static String streamToString(InputStream in) throws IOException {
		StringBuilder out = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		for (String line = br.readLine(); line != null; line = br.readLine()) {
			out.append(line);
		}
		br.close();
		return out.toString();
	}
}
