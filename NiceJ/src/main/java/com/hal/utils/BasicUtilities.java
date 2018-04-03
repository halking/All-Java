/**
 * Copyright 2012 Richemont
 * Cambridge Technology Partners
 */
package com.hal.utils;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;
import org.springframework.util.StringUtils;

/**
 * Description
 * 
 * @author smorei
 */
public class BasicUtilities {

	public static String getUsername(String authorization) {
		if (StringUtils.hasText(authorization)) {
			authorization = authorization.split(" ")[1];
			String userAndPassword = new String(
					DatatypeConverter.parseBase64Binary(authorization));

			return userAndPassword.split(":").length >= 2 ? userAndPassword
					.split(":")[0] : null;
		} else {
			return null;
		}
	}

	public static String getPassword(String authorization) {
		if (StringUtils.hasText(authorization)) {
			authorization = authorization.split(" ")[1];
			String userAndPassword = new String(
					DatatypeConverter.parseBase64Binary(authorization));

			return userAndPassword.split(":").length >= 2 ? userAndPassword
					.split(":")[1] : null;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public static String encode(String username, String password) {
		String authString = username + ":" + password;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);
		return "Basic " + authStringEnc;
	}

}
