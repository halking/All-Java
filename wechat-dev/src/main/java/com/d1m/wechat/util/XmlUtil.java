package com.d1m.wechat.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.d1m.wechat.common.Constant;
import com.d1m.wechat.exception.XmlConvertException;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * The utility class provider methods doing transfer between object and XML.
 * @author Huang Jiaming
 */
public class XmlUtil {

    /**
     * Transfer object to xml string
     * @param obj
     * @return string
     */
    public static String toXML(Object obj) throws XmlConvertException {
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            //encode format
            marshaller.setProperty(Marshaller.JAXB_ENCODING, Constant.CHARSET_UTF_8);
            //whether using format output
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //ignore header clarification
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
            StringWriter writer = new StringWriter();
            marshaller.marshal(obj, writer);
            return writer.toString();
        } catch (JAXBException e) {
            throw new XmlConvertException(XmlConvertException.OBJECT_CONVERT_TO_XML_ERROR);
        }
    }

    /**
     * Transfer xml string to object
     * @param xml, class
     * @return object
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromXML(String xml, Class<T> valueType) throws XmlConvertException {
        try {
            JAXBContext context = JAXBContext.newInstance(valueType);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (T) unmarshaller.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            throw new XmlConvertException(XmlConvertException.XML_CONVERT_TO_OBJECT_ERROR);
        }
    }

    public static void getXPathStringValue(Map<String, String> map, Document doc) throws XmlConvertException {
        if (CollectionUtils.isEmpty(map) || doc == null) {
            return;
        }
        XPathFactory xfactory = XPathFactory.newInstance();
        XPath xpath = xfactory.newXPath();
        try {
            for (String key : map.keySet()) {
                String path = map.get(key);
                if (StringUtils.isNotBlank(path)) {
                    String value = (String)xpath.compile(path).evaluate(doc, XPathConstants.STRING);
                    map.put(key, value);
                }
            }
        } catch (XPathExpressionException e) {
            throw new XmlConvertException(XmlConvertException.XML_CONVERT_TO_OBJECT_ERROR);
        }
    }

    public static String getXPathStringValue(String path, Document doc) throws XmlConvertException {
        if (StringUtils.isBlank(path) || doc == null) {
            return null;
        }
        XPathFactory xfactory = XPathFactory.newInstance();
        XPath xpath = xfactory.newXPath();
        try {
            return (String)xpath.compile(path).evaluate(doc, XPathConstants.STRING);
        } catch (XPathExpressionException e) {
            throw new XmlConvertException(XmlConvertException.XML_CONVERT_TO_OBJECT_ERROR);
        }
    }
    
    /**
     * Convert Xml to Document
     * @param xml
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Document convertXmlToDocument(String xml) throws ParserConfigurationException, SAXException, IOException{
    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(new ByteArrayInputStream(xml.getBytes("utf-8")));
        return doc;
    }
}
