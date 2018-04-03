package com.d1m.wechat.util;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by d1m on 2016/9/2.
 */
public class JacksonUtils {
    /**
     * can reuse, share globally
     */
    private static final ObjectMapper object = new ObjectMapper();

    /**
     * can reuse, share globally
     */
    private static final XmlMapper xml = new XmlMapper();

    /**
     * return a ObjectMapper that is singleton
     * @return
     */
    public static ObjectMapper getObjectMapper() {
        return object;
    }

    /**
     * return a XmlMapper that is singleton
     * @return
     */
    public static XmlMapper getXmlMapper() {
        return xml;
    }

    /**
     * Object to XML, default root name is 'root'
     *
     * @param object
     * @return
     */
    public static String toXML(Object object) {
        return toXML(object, "root");
    }

    /**
     * Object to XML
     *
     * @param object
     * @param rootName
     * @return
     */
    public static String toXML(Object object, String rootName) {
        try {
            XmlMapper xml = getXmlMapper();
            // Object to XML
            String xmlStr = xml.writeValueAsString(object);
            String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
            // Object Class Name
            String mapClassName = object.getClass().getSimpleName();
            String beginStr = "<" + mapClassName + " xmlns=\"\">";
            String endStr = "</" + mapClassName + ">";
            int beginNum = beginStr.length();
            int endNum = xmlStr.indexOf(endStr);
            String subStr = xmlStr.substring(beginNum, endNum);
            StringBuffer sb = new StringBuffer();
            sb.append(xmlHeader).append("<" + rootName + ">").append(subStr)
                    .append("</" + rootName + ">");
            return sb.toString();
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Object to Json
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        try {
            ObjectMapper mapper = getObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Map nameValuePair = new TreeMap();
        nameValuePair.put("Account", "2222222");
        nameValuePair.put("pUserID", "123456");
        System.out.println(JacksonUtils.toJson(nameValuePair));
        System.out.println(JacksonUtils.toXML(nameValuePair,"member"));
    }
}
