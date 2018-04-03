package com.d1m.wechat.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.*;

public class MD5 {

	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static String byteArrayToHexString(byte[] b) {
		StringBuilder resultSb = new StringBuilder();
		for (byte temp : b) {
			resultSb.append(byteToHexString(temp));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultString;
	}

	/**
	 * generate md5 signature
	 *
	 * @param map
	 * @param key
	 * @param charset
	 * @return
	 */
	public static String md5Sign(Map<String, String> map, String key, String charset) {
		String prestr = createLinkString(map, null, true, false);
		return DigestUtils.md5Hex(getContentBytes(prestr + key, charset));
	}
	/**
	 * convert Map to link string
	 *
	 * @param params
	 * @return
	 */
	public static String createLinkString(Map<String, String> params, String host, boolean needFilter,
										  boolean needEncode) {

		List<String> keys;
		if (needFilter) {
			keys = new ArrayList<String>(paraFilter(params).keySet());
		} else {
			keys = new ArrayList<String>(params.keySet());
		}

		Collections.sort(keys);
		String prestr = "";
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = "";
			try {
				value = params.get(key);

				if (needEncode) {
					value = URLEncoder.encode(params.get(key), "utf-8");
				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			if (i == keys.size() - 1) {
				prestr = prestr + key + "=" + value;
			} else {
				prestr = prestr + key + "=" + value + "&";
			}
		}

		if (!StringUtils.isEmpty(host)) {
			return host + prestr;
		}

		return prestr;
	}

	/**
	 * remove the empty value and sign
	 *
	 * @param map
	 * @return
	 */
	public static Map<String, String> paraFilter(Map<String, String> map) {

		Map<String, String> result = new HashMap<String, String>();

		if (map == null || map.size() <= 0) {
			return result;
		}

		for (String key : map.keySet()) {
			String value = map.get(key);
			if (value == null || value.equals("") || key.equalsIgnoreCase("sign") || key.equalsIgnoreCase("sign_type")) {
				continue;
			}
			result.put(key, value);
		}

		return result;
	}

	private static byte[] getContentBytes(String content, String charset) {
		if (charset == null || "".equals(charset)) {
			return content.getBytes();
		}
		try {
			return content.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return null;
	}

}
