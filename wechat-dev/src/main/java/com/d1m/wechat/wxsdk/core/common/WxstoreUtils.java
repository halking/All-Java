package com.d1m.wechat.wxsdk.core.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import com.d1m.wechat.service.WechatService;
import com.d1m.wechat.service.impl.RefreshAccessTokenJob;
import com.d1m.wechat.util.AppContextUtils;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.util.Message;

/**
 * 
 * @author xiaoqingfeng 微信url请求公用方法 @
 */
public class WxstoreUtils {

	private static Logger logger = LoggerFactory.getLogger(WxstoreUtils.class);

	public static boolean httpRequestExist(String accesstoken,String requestUrl,
			String requestMethod, String outputStr) {
		logger.debug("*********HTTPREQUEST START********");
		logger.debug("*********requestUrl is " + requestUrl
				+ " END AND requestMethod IS" + requestMethod
				+ " END AND  outputStr" + outputStr + " END ********");
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new SecureRandom());

			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();

				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			InputStream inputStream = httpUrlConn.getInputStream();

			String contentType = httpUrlConn.getContentType();// image,video，radio为null,其他为text/plain
			logger.info("file type:" + contentType);
			if (StringUtils.isNotBlank(contentType)) {
				InputStreamReader inputStreamReader = new InputStreamReader(
						inputStream, "utf-8");
				BufferedReader bufferedReader = new BufferedReader(
						inputStreamReader);
				String str = null;
				while ((str = bufferedReader.readLine()) != null) {
					buffer.append(str);
				}
				bufferedReader.close();
				inputStreamReader.close();

				inputStream.close();
				inputStream = null;
				httpUrlConn.disconnect();
				logger.info("response : {}", buffer.toString());
				jsonObject = JSONObject.fromObject(buffer.toString());
				logger.debug("jsonObject : {}", jsonObject);
				if (jsonObject.containsKey("errcode")
						&& jsonObject.getInt("errcode") != 0) {
					logger.debug(
							"httpRequest Method！errcode = {}, errmsg = {}",
							jsonObject.getString("errcode"),
							jsonObject.getString("errmsg"));
					logger.debug("********* ERROR********{}", buffer.toString());
					String errcode = jsonObject.getString("errcode");
					if (StringUtils.equals(errcode, "40001")
							|| StringUtils.equals(errcode, "40014")
							|| StringUtils.equals(errcode, "42001")) {
						logger.error("access_token error, Refresh AccessToken.");
						WechatService wechatService = AppContextUtils
								.getBean(WechatService.class);
						RefreshAccessTokenJob.getNewAccessToken(wechatService
								.getById(RefreshAccessTokenJob.accessTokenWechatMap
										.get(accesstoken)));
						RefreshAccessTokenJob.accessTokenWechatMap
								.remove(accesstoken);
					}
					logger.error("req weixin error:{}", jsonObject.toString());
					logger.debug("*********HTTPREQUEST END********");
					return false;
				} else {
					logger.debug("********* SUCCESS END********");
					return true;
				}
			} else {
				return true;
			}

		} catch (ConnectException ce) {
			logger.error(ce.getMessage());
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public static JSONObject httpRequest(String accesstoken, String requestUrl,
										 String requestMethod, String outputStr) {
		logger.debug("*********HTTPREQUEST START********");
		logger.debug("*********requestUrl is " + requestUrl
				+ " END AND requestMethod IS" + requestMethod
				+ " END AND  outputStr" + outputStr + " END ********");
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new SecureRandom());

			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();

				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.close();
			}

			InputStream inputStream = httpUrlConn.getInputStream();

			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();

			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			logger.info("response : {}", buffer.toString());
			jsonObject = JSONObject.fromObject(buffer.toString());
			logger.debug("jsonObject : {}", jsonObject);
			if (jsonObject.containsKey("errcode")
					&& jsonObject.getInt("errcode") != 0) {
				logger.debug("httpRequest Method！errcode = {}, errmsg = {}",
						jsonObject.getString("errcode"),
						jsonObject.getString("errmsg"));
				logger.debug("********* ERROR********{}", buffer.toString());
				String errcode = jsonObject.getString("errcode");
				if (StringUtils.equals(errcode, "40001")
						|| StringUtils.equals(errcode, "40014")
						|| StringUtils.equals(errcode, "42001")) {
					logger.error("access_token error, Refresh AccessToken.");
					WechatService wechatService = AppContextUtils
							.getBean(WechatService.class);
					RefreshAccessTokenJob.getNewAccessToken(wechatService
							.getById(RefreshAccessTokenJob.accessTokenWechatMap
									.get(accesstoken)));
					RefreshAccessTokenJob.accessTokenWechatMap
							.remove(accesstoken);
				}
				logger.error("req weixin error:{}", jsonObject.toString());
				logger.debug("*********HTTPREQUEST END********");
			} else {
				logger.debug("********* SUCCESS END********");
			}
		} catch (ConnectException ce) {
			logger.error(ce.getMessage());
			throw new WechatException(
					Message.WEIXIN_SERVER_CONNECTION_TIMED_OUT);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new WechatException(Message.WEIXIN_HTTPS_REQUEST_ERROR);
		}
		return jsonObject;
	}

	public static JSONObject httpRequest2(String accesstoken,String requestUrl,
			String requestMethod, byte[] outputStr) {
		JSONObject jsonObject = null;
		StringBuffer buffer = new StringBuffer();
		try {
			TrustManager[] tm = { new MyX509TrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new SecureRandom());

			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			HttpsURLConnection httpUrlConn = (HttpsURLConnection) url
					.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);

			httpUrlConn.setRequestMethod(requestMethod);

			if ("GET".equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			if (null != outputStr) {
				OutputStream outputStream = httpUrlConn.getOutputStream();

				outputStream.write(outputStr);
				outputStream.close();
			}

			InputStream inputStream = httpUrlConn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			bufferedReader.close();
			inputStreamReader.close();

			inputStream.close();
			inputStream = null;
			httpUrlConn.disconnect();
			jsonObject = JSONObject.fromObject(buffer.toString());
			logger.debug("jsonObject : {}", jsonObject);
			if (jsonObject.containsKey("errcode")
					&& jsonObject.getInt("errcode") != 0) {
				logger.debug(
						"httpRequest Method！errcode = {}, errmsg = {}",
						jsonObject.getString("errcode"),
						jsonObject.getString("errmsg"));
				logger.debug("********* ERROR********{}", buffer.toString());
				String errcode = jsonObject.getString("errcode");
				if (StringUtils.equals(errcode, "40001")
						|| StringUtils.equals(errcode, "40014")
						|| StringUtils.equals(errcode, "42001")) {
					logger.error("access_token error, Refresh AccessToken.");
					WechatService wechatService = AppContextUtils
							.getBean(WechatService.class);
					RefreshAccessTokenJob.getNewAccessToken(wechatService
							.getById(RefreshAccessTokenJob.accessTokenWechatMap
									.get(accesstoken)));
					RefreshAccessTokenJob.accessTokenWechatMap
							.remove(accesstoken);
				}
				logger.error("req weixin error:{}", jsonObject.toString());
				logger.debug("*********HTTPREQUEST END********");
			} else {
				logger.debug("********* SUCCESS END********");
			}
		} catch (ConnectException ce) {
			System.out.print("Weixin server connection timed out.");
		} catch (Exception e) {
			System.out.print("https request error:{}" + e.getMessage());
		}
		return jsonObject;
	}

	public static void saveHttpImage(String requestUrl, String requestMethod,
			String outputStr, File target) {
		try {
			URL url = new URL(requestUrl);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod(requestMethod);

			conn.setConnectTimeout(5000);

			InputStream inStream = conn.getInputStream();

			byte[] data = readInputStream(inStream);

			FileOutputStream outStream = new FileOutputStream(target);

			outStream.write(data);

			outStream.close();
		} catch (Exception e) {
		}
	}

	public static String encode(byte[] bstr) {
		return new BASE64Encoder().encode(bstr);
	}

	public static byte[] decode(String str) {
		byte[] bt = null;
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			bt = decoder.decodeBuffer(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bt;
	}

	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1) {
			outstream.write(buffer, 0, len);
		}
		outstream.close();
		inStream.close();

		return outstream.toByteArray();
	}

	/**
	 * 上传多媒体文件到微信公众平台
	 *
	 * @param requestUrl
	 * 
	 * @param file
	 *            文件
	 * @param content_type
	 *            文件类型
	 * @return 返回的字符串
	 * @throws Exception
	 */
	public static JSONObject uploadMediaFile(String accesstoken, String requestUrl, File file,
			String content_type) {
		logger.debug("requestUrl : {}", requestUrl);
		JSONObject jsonObject = null;
		StringBuffer bufferStr = new StringBuffer();
		String end = "\r\n";
		String twoHyphens = "--"; // 用于拼接
		String boundary = "*****"; // 用于拼接 可自定义
		URL submit = null;
		DataOutputStream dos = null;
		BufferedInputStream bufin = null;
		BufferedReader bufferedReader = null;
		try {
			submit = new URL(requestUrl);
			HttpURLConnection conn = (HttpURLConnection) submit
					.openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			// 获取输出流对象，准备上传文件
			dos = new DataOutputStream(conn.getOutputStream());
			dos.write(("--" + boundary + "\r\n").getBytes());
			dos.write(String
					.format("Content-Disposition: form-data; name=\"media\"; filename=\"%s\"; filelength=\"%s\"\r\n",
							file.getName(), file.length()).getBytes());
			dos.write(("Content-Type: " + content_type + end).getBytes());
			dos.writeBytes(end);
			// 对文件进行传输
			bufin = new BufferedInputStream(new FileInputStream(file));
			byte[] buffer = new byte[8192]; // 8k
			int count = 0;
			while ((count = bufin.read(buffer)) != -1) {
				dos.write(buffer, 0, count);
			}

			bufin.close(); // 关闭文件流

			dos.writeBytes(end);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();

			// 读取URL链接返回字符串
			InputStream inputStream = conn.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				bufferStr.append(str);
			}

			jsonObject = JSONObject.fromObject(bufferStr.toString());
			logger.debug("jsonObject : {}", jsonObject);
			if (jsonObject.containsKey("errcode")
					&& jsonObject.getInt("errcode") != 0) {
				logger.debug(
						"httpRequest Method！errcode = {}, errmsg = {}",
						jsonObject.getString("errcode"),
						jsonObject.getString("errmsg"));
				logger.debug("********* ERROR********{}", buffer.toString());
				String errcode = jsonObject.getString("errcode");
				if (StringUtils.equals(errcode, "40001")
						|| StringUtils.equals(errcode, "40014")
						|| StringUtils.equals(errcode, "42001")) {
					logger.error("access_token error, Refresh AccessToken.");
					WechatService wechatService = AppContextUtils
							.getBean(WechatService.class);
					RefreshAccessTokenJob.getNewAccessToken(wechatService
							.getById(RefreshAccessTokenJob.accessTokenWechatMap
									.get(accesstoken)));
					RefreshAccessTokenJob.accessTokenWechatMap
							.remove(accesstoken);
				}
				logger.error("req weixin error:{}", jsonObject.toString());
				logger.debug("*********HTTPREQUEST END********");
			} else {
				logger.debug("********* SUCCESS END********");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new WechatException(Message.WEIXIN_HTTPS_REQUEST_ERROR);
		} finally {
			try {
				if (dos != null) {
					dos.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (Exception e2) {
				logger.error(e2.getMessage());
			}
		}
		// 获取到返回Json请自行根据返回码获取相应的结果
		return jsonObject;
	}
}
