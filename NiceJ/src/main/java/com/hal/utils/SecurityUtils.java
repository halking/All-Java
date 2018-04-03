package com.hal.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Random;

/**
 * 加密工具
 * @author tongc
 *
 */
public class SecurityUtils {
	
	/**
	 * 加密DB密码字段和登录密码
	 * @param userId 用户ID
	 * @param password 明文密码
	 * @return
	 */
	public static final String encodeDBPassword(int userId, String password) {
		
		SecurityEncoderUtils shaSecurityEncoder = SecurityEncoderUtils.getInstance("SHA");
		//SHA明文加密
		String encodedPasswd = shaSecurityEncoder.encodePassword(password);
		
		SecurityEncoderUtils securityEncoderUtils = SecurityEncoderUtils.getInstance("MD5");

		// 2. MD5加密（第一步加密后结果）
		encodedPasswd = securityEncoderUtils.encodePassword(encodedPasswd + userId);
		return encodedPasswd;
	}
	
	
	/**
	 * 加密支付密码
	 * 
	 * @param userId 用户ID
	 * @param password 明文密码
	 * @return
	 */
	public static final String encodePayPassword(Integer userId, String password) {
		SecurityEncoderUtils securityEncoderUtils = SecurityEncoderUtils.getInstance("MD5");
		// 1. md5加密支付密码
		String step1 = securityEncoderUtils.encodePassword(password);
		
		// 2. MD5加密（第一步加密后结果 + 用户ID）
		String encodedPasswd = securityEncoderUtils.encodePassword(step1 + userId);
		return encodedPasswd;
	}
	
	/**
	 * 生成抽奖页面token
	 * @return
	 */
	public static final String genLuckyDrawToken() {
		Random random = new Random();
		long l = random.nextLong();
		String token = MD5Util.MD5(String.valueOf(l));
		return token;
	}
	
	/**
	 * 参数按照utf-8 url加密
	 * @param para
	 * @return
	 */
	public static String encode(String para) {
		String result = "";
		if (para == null || "".equals(para)) {
			return result;
		}
		try {
			result = URLEncoder.encode(para, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 参数按照utf-8 url解密
	 * @param para
	 * @return
	 */
	public static String decode(String para) {
		String result = "";
		if (para == null || "".equals(para)) {
			return result;
		}
		try {
			result = URLDecoder.decode(para, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) {
//		System.out.println(SecurityUtils.encodeDBPassword(5, "111111"));
//		System.out.println(SecurityUtils.encodeDBPassword(6, "111111"));
		System.out.println(SecurityUtils.encodeDBPassword(20, "111111"));
	}
}
