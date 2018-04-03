package com.d1m.wechat.wxsdk.core.handler.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import org.apache.log4j.Logger;

import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.util.Message;
import com.d1m.wechat.wxsdk.core.annotation.ReqType;
import com.d1m.wechat.wxsdk.core.handler.WeiXinReqHandler;
import com.d1m.wechat.wxsdk.core.req.model.UploadMedia;
import com.d1m.wechat.wxsdk.core.req.model.UploadMediaImg;
import com.d1m.wechat.wxsdk.core.req.model.WeixinReqConfig;
import com.d1m.wechat.wxsdk.core.req.model.WeixinReqParam;
import com.d1m.wechat.wxsdk.core.req.model.kfaccount.KfaccountUploadheadimg;
import com.d1m.wechat.wxsdk.core.util.HttpRequestProxy;
import com.d1m.wechat.wxsdk.core.util.WeiXinReqUtil;

public class WeixinReqMediaUploadHandler implements WeiXinReqHandler {

	private static Logger logger = Logger
			.getLogger(WeixinReqMediaUploadHandler.class);

	@SuppressWarnings({ "rawtypes", "resource" })
	public String doRequest(WeixinReqParam weixinReqParam)
			throws WechatException {
		String strReturnInfo = "";
		if (weixinReqParam instanceof UploadMedia) {
			UploadMedia uploadMedia = (UploadMedia) weixinReqParam;
			ReqType reqType = uploadMedia.getClass().getAnnotation(
					ReqType.class);
			WeixinReqConfig objConfig = WeiXinReqUtil
					.getWeixinReqConfig(reqType.value());
			if (objConfig != null) {
				String reqUrl = objConfig.getUrl();
				String fileName = uploadMedia.getFilePathName();
				File file = new File(fileName);
				InputStream fileIn = null;
				try {
					fileIn = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					logger.error(e.getMessage());
					throw new WechatException(
							Message.WEIXIN_UPLOAD_FILE_NOT_EXIST);
				}
				String extName = fileName
						.substring(fileName.lastIndexOf(".") + 1);
				String contentType = WeiXinReqUtil.getFileContentType(extName);
				Map parameters = WeiXinReqUtil
						.getWeixinReqParam(weixinReqParam);
				parameters.remove("filePathName");
				strReturnInfo = HttpRequestProxy.uploadMedia(reqUrl,
						parameters, "UTF-8", fileIn, file.getName(),
						contentType);
			}
		} else if (weixinReqParam instanceof KfaccountUploadheadimg) {
			KfaccountUploadheadimg uploadMedia = (KfaccountUploadheadimg) weixinReqParam;
			ReqType reqType = uploadMedia.getClass().getAnnotation(
					ReqType.class);
			WeixinReqConfig objConfig = WeiXinReqUtil
					.getWeixinReqConfig(reqType.value());
			if (objConfig != null) {
				String reqUrl = objConfig.getUrl();
				String fileName = uploadMedia.getFilePathName();
				File file = new File(fileName);
				InputStream fileIn = null;
				try {
					fileIn = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					logger.error(e.getMessage());
					throw new WechatException(
							Message.WEIXIN_UPLOAD_FILE_NOT_EXIST);
				}
				String extName = fileName
						.substring(fileName.lastIndexOf(".") + 1);// 扩展名
				String contentType = WeiXinReqUtil.getFileContentType(extName);
				if (contentType == null || !contentType.equals("image/jpeg")) {
					throw new WechatException(
							Message.WEIXIN_UPLOAD_KFACCOUNT_HEADIMG_MUST_JPG);
				}
				Map parameters = WeiXinReqUtil
						.getWeixinReqParam(weixinReqParam);
				parameters.remove("filePathName");
				strReturnInfo = HttpRequestProxy.uploadMedia(reqUrl,
						parameters, "UTF-8", fileIn, file.getName(),
						contentType);
			}
		}else if(weixinReqParam instanceof UploadMediaImg){
			UploadMediaImg uploadMediaImg = (UploadMediaImg) weixinReqParam;
			ReqType reqType = uploadMediaImg.getClass().getAnnotation(
					ReqType.class);
			WeixinReqConfig objConfig = WeiXinReqUtil
					.getWeixinReqConfig(reqType.value());
			if (objConfig != null) {
				String reqUrl = objConfig.getUrl();
				String fileName = uploadMediaImg.getFilePathName();
				File file = new File(fileName);
				InputStream fileIn = null;
				try {
					fileIn = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					logger.error(e.getMessage());
					throw new WechatException(
							Message.WEIXIN_UPLOAD_FILE_NOT_EXIST);
				}
				String extName = fileName
						.substring(fileName.lastIndexOf(".") + 1);
				String contentType = WeiXinReqUtil.getFileContentType(extName);
				Map parameters = WeiXinReqUtil
						.getWeixinReqParam(weixinReqParam);
				parameters.remove("filePathName");
				strReturnInfo = HttpRequestProxy.uploadMedia(reqUrl,
						parameters, "UTF-8", fileIn, file.getName(),
						contentType);
			}
		}
		else {
			throw new WechatException(Message.WEIXIN_UPLOAD_MEDIA_NO_CONFIG);
		}
		return strReturnInfo;
	}

}
