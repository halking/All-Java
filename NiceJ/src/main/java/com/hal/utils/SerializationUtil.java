/**
 *
 *//*
package com.hal.utils;

import com.richemont.swse.srv.order.OrderConstants;
import com.sun.org.apache.xml.internal.serialize.Method;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.wstk.core.TechnicalException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.charset.StandardCharsets;

*//**
 * @author Jimmy
 *//*
public final class SerializationUtil {

    *//**
     * Convert string to object.
     *
     * @param clazz the clazz
     * @param xml   the xml
     * @return the object
     * @throws TechnicalException the technical exception
     *//*
    @SuppressWarnings("unchecked")
    public static <T> T fromString(Class<T> clazz, String xml)
            throws TechnicalException {
        return (T) unmarshal(clazz, xml).getValue();
    }

    @SuppressWarnings("unchecked")
    public static <T> JAXBElement<T> unmarshal(Class<T> clazz, String xml)
            throws TechnicalException {
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller um = context.createUnmarshaller();
            Object obj = clazz.newInstance();
            InputStream xmlIs = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            return (JAXBElement<T>) um.unmarshal(
                    new StreamSource(xmlIs), obj.getClass());
        } catch (JAXBException e) {
            throw new TechnicalException(OrderConstants.FILE_ERR_CODE, e);
        } catch (InstantiationException e) {
            throw new TechnicalException(OrderConstants.FILE_ERR_CODE, e);
        } catch (IllegalAccessException e) {
            throw new TechnicalException(OrderConstants.FILE_ERR_CODE, e);
        } catch (UnsupportedEncodingException e) {
            throw new TechnicalException(OrderConstants.FILE_ERR_CODE, e);
        }

    }

    *//**
     * Write the object to a string.
     *
     * @param <T> the generic type
     * @param o   the o
     * @return the string
     * @throws TechnicalException the technical exception
     *//*
    @SuppressWarnings("unchecked")
    public static <T> String toString(T o, String rootElement) throws TechnicalException {
        try {
            StringWriter sw = new StringWriter();
            QName q = new QName(rootElement);
            JAXBContext jaxbContext;

            jaxbContext = JAXBContext.newInstance(o.getClass());

            JAXBElement<T> jaxbElement = new JAXBElement<T>(q,
                    (Class<T>) o.getClass(), o);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(jaxbElement, sw);
            return sw.toString();
        } catch (JAXBException e) {
            throw new TechnicalException(OrderConstants.FILE_ERR_CODE, e);
        }
    }


    public static String toStringWithCastor(Object o) throws MarshalException, ValidationException {

        String result = null;
        Writer writer = null;
        ByteArrayOutputStream stream = null;
        OutputFormat format = new OutputFormat(Method.XML, StandardCharsets.UTF_8.name(), false);

        try {
            stream = new ByteArrayOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(stream, StandardCharsets.UTF_8.name()));
            XMLSerializer serializer = new XMLSerializer(writer, format);
            org.exolab.castor.xml.Marshaller marshaller = new org.exolab.castor.xml.Marshaller(serializer.asDocumentHandler());
            marshaller.setMarshalAsDocument(false);
            marshaller.setValidation(false);
            marshaller.marshal(o);
            result = new String(stream.toByteArray());
        } catch (IOException e) {
            throw new MarshalException(e);
        } finally {
            try {
                writer.close();
            } catch (IOException ignore) {
                writer = null;
            }

            try {
                stream.close();
            } catch (IOException ignore) {
                stream = null;
            }
        }
        return result;
    }
}
*/