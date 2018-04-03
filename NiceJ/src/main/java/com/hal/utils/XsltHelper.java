package com.hal.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;

import org.apache.commons.codec.language.bm.Languages;


public class XsltHelper {
	/**
	 * Please don't remove the file from the current location.
	 */
	private static final String LANG_EN_XSL = "lang-en.xsl";
	/**
	 * Please don't remove the file from the current location.
	 */
	private static final String LANG_FR_XSL = "lang-fr.xsl";

/*	public static String getContent(InputStream in, Languages languages) {
		try {
			InputStream xslIn;
			switch (languages) {
			case EN:
				xslIn = XsltHelper.class.getResourceAsStream(
						LANG_EN_XSL);
				return XsltHelper.getTransformedContent(in, xslIn);
			case FR:
				xslIn = XsltHelper.class.getResourceAsStream(
						LANG_FR_XSL);
				return XsltHelper.getTransformedContent(in, xslIn);
			default:
				xslIn = XsltHelper.class.getResourceAsStream(
						LANG_EN_XSL);
				return XsltHelper.getTransformedContent(in, xslIn);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}*/

	public static String getTransformedContent(InputStream in, InputStream xsl) {

		StringWriter sw = new StringWriter();
		try {
			javax.xml.transform.Source xmlSource =
					new javax.xml.transform.stream.StreamSource(in);
			javax.xml.transform.Source xsltSource =
					new javax.xml.transform.stream.StreamSource(xsl);
			javax.xml.transform.Result result =
					new javax.xml.transform.stream.StreamResult(sw);

			// create an instance of TransformerFactory
			javax.xml.transform.TransformerFactory transFact =
					javax.xml.transform.TransformerFactory.newInstance();

			javax.xml.transform.Transformer trans =
					transFact.newTransformer(xsltSource);

			trans.transform(xmlSource, result);
			return sw.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void transform(InputStream in, InputStream xsl,
			StringWriter sw) throws Exception {

		javax.xml.transform.Source xmlSource =
				new javax.xml.transform.stream.StreamSource(in);
		javax.xml.transform.Source xsltSource =
				new javax.xml.transform.stream.StreamSource(xsl);
		javax.xml.transform.Result result =
				new javax.xml.transform.stream.StreamResult(sw);

		// create an instance of TransformerFactory
		javax.xml.transform.TransformerFactory transFact =
				javax.xml.transform.TransformerFactory.newInstance();

		javax.xml.transform.Transformer trans =
				transFact.newTransformer(xsltSource);

		trans.transform(xmlSource, result);
	}

	public static void transform(InputStream in, InputStream xsl,
			OutputStream out) throws Exception {

		javax.xml.transform.Source xmlSource =
				new javax.xml.transform.stream.StreamSource(in);
		javax.xml.transform.Source xsltSource =
				new javax.xml.transform.stream.StreamSource(xsl);
		javax.xml.transform.Result result =
				new javax.xml.transform.stream.StreamResult(out);

		// create an instance of TransformerFactory
		javax.xml.transform.TransformerFactory transFact =
				javax.xml.transform.TransformerFactory.newInstance();
		javax.xml.transform.Transformer trans =
				transFact.newTransformer(xsltSource);
		trans.transform(xmlSource, result);
	}

}
