package com.d1m.wechat.wxsdk.wxbase.wxmedia;

import java.io.File;
import java.util.List;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.util.Constants;
import com.d1m.wechat.util.Message;
import com.d1m.wechat.wxsdk.core.common.WxstoreUtils;
import com.d1m.wechat.wxsdk.core.req.WeiXinReqService;
import com.d1m.wechat.wxsdk.core.req.model.DownloadMedia;
import com.d1m.wechat.wxsdk.core.req.model.UploadMedia;
import com.d1m.wechat.wxsdk.core.req.model.UploadMediaImg;
import com.d1m.wechat.wxsdk.core.sendmsg.JwSendMessageAPI;
import com.d1m.wechat.wxsdk.core.sendmsg.model.WxArticle;
import com.d1m.wechat.wxsdk.core.sendmsg.model.WxArticlesResponse;
import com.d1m.wechat.wxsdk.core.util.WeiXinConstant;
import com.d1m.wechat.wxsdk.core.util.WeiXinReqUtil;
import com.d1m.wechat.wxsdk.wxbase.wxmedia.model.*;

/**
 * 微信--token信息
 * 
 * @author lizr
 * 
 */
public class JwMediaAPI {
	private static Logger logger = LoggerFactory.getLogger(JwMediaAPI.class);
	// 新增永久图文素材
	private static String material_add_news_url = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token=ACCESS_TOKEN";
	// 新增其他类型永久素材
	private static String material_add_material_url = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE";
	// 获取永久素材
	private static String material_get_material_url = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token=ACCESS_TOKEN";
	// 获取素材总数
	private static String material_get_materialcount_url = "https://api.weixin.qq.com/cgi-bin/material/get_materialcount?access_token=ACCESS_TOKEN";
	// 修改永久图文素材
	private static String material_update_news_url = "https://api.weixin.qq.com/cgi-bin/material/update_news?access_token=ACCESS_TOKEN";
	// 获取素材列表
	private static String material_batchget_material_url = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token=ACCESS_TOKEN";
	// 删除永久素材
	private static String material_del_material_url = "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token=ACCESS_TOKEN";

	/**
	 * 
	 * @param accessToke
	 * @param type
	 *            媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
	 * @param fileNamePath
	 *            上传的文件目录
	 * @return
	 * @throws WechatException
	 */
	public static WxUpload uploadMedia(String accessToke, String type,
			String fileNamePath) throws WechatException {
		UploadMedia uploadMedia = new UploadMedia();
		uploadMedia.setAccess_token(accessToke);
		uploadMedia.setFilePathName(fileNamePath);
		uploadMedia.setType(type);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(
				uploadMedia);
		Object error = result.get(WeiXinConstant.RETURN_ERROR_INFO_CODE);
		WxUpload wxMedia = (WxUpload) JSONObject.toBean(result, WxUpload.class);
		return wxMedia;
	}
	
	/**
	 * 上传图文消息中原文图片上传
	 * @param accessToken
	 * @param fileNamePath
	 * @return
	 * @throws WechatException
	 */
	public static WxUploadImg uploadMediaImg(String accessToken,
			String fileNamePath) throws WechatException {
		UploadMediaImg uploadMediaImg = new UploadMediaImg();
		uploadMediaImg.setAccess_token(accessToken);
		uploadMediaImg.setFilePathName(fileNamePath);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(
				uploadMediaImg);
		Object error = result.get(WeiXinConstant.RETURN_ERROR_INFO_CODE);
		WxUploadImg wxMediaImg = (WxUploadImg) JSONObject.toBean(result, WxUploadImg.class);
		return wxMediaImg;
	} 
	
	/**
	 * 下载多媒体
	 * 
	 * @param accessToke
	 * @param media_id
	 * @param filePath
	 * @return
	 * @throws WechatException
	 */
	public static WxDownload downMedia(String accessToke, String media_id,
                                       String filePath) throws WechatException {
		DownloadMedia downloadMedia = new DownloadMedia();
		downloadMedia.setAccess_token(accessToke);
		downloadMedia.setFilePath(filePath);
		downloadMedia.setMedia_id(media_id);
		JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(
				downloadMedia);
		if (result.isEmpty()) {
			logger.error("get media {} is empty", media_id);
			return null;
		}
		Object error = result.get(WeiXinConstant.RETURN_ERROR_INFO_CODE);
		if (error != null) {
			logger.error("get media {}, error : {}", media_id, result);
			return null;
		}
		WxDownload wxMedia = (WxDownload) JSONObject.toBean(result, WxDownload.class);
		return wxMedia;
	}

	/**
	 * 上传新增永久图文素材
	 * 
	 * @param accesstoken
	 * @param wxArticles
	 *            图文集合，数量不大于10
	 * @return WxArticlesResponse 上传图文消息素材返回结果
	 * @throws WechatException
	 */
	public static WxArticlesResponse uploadArticlesByMaterial(
			String accesstoken, List<WxArticle> wxArticles)
			throws WechatException {
		WxArticlesResponse wxArticlesResponse = null;

		if (wxArticles.size() == 0) {
			throw new WechatException(Message.WEIXIN_UPLOAD_ARTICLES_NOT_EMPTY);
		}
		if (wxArticles.size() > Constants.MAX_UPLOAD_WX_ARTICLES_SIEZ) {
			throw new WechatException(
					Message.WEIXIN_UPLOAD_ARTICLES_OVER_MAX_SIZE);
		}
		String requestUrl = material_add_news_url.replace("ACCESS_TOKEN",
				accesstoken);
		for (WxArticle article : wxArticles) {
			if (article.getFileName() != null
					&& article.getFileName().length() > 0) {
				String mediaId = JwSendMessageAPI.getFileMediaId(accesstoken,
						article);
				article.setThumb_media_id(mediaId);
			}
		}
		WxArticlesRequest wxArticlesRequest = new WxArticlesRequest();
		wxArticlesRequest.setArticles(wxArticles);
		JSONObject obj = JSONObject.fromObject(wxArticlesRequest);
		JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
				obj.toString());
		logger.info("weixin response：{}", result.toString());
		if (!result.has("errcode")) {
			// logger.error("上传图文消息失败！errcode=" + result.getString("errcode")
			// + ",errmsg = " + result.getString("errmsg"));
			// throw new WechatException("上传图文消息失败！errcode="
			// + result.getString("errcode") + ",errmsg = "
			// + result.getString("errmsg"));
			wxArticlesResponse = new WxArticlesResponse();
			wxArticlesResponse.setMedia_id(result.getString("media_id"));
			// wxArticlesResponse.setType(result.getString("type"));
			// wxArticlesResponse.setCreated_at(new Date(result
			// .getLong("created_at") * 1000));
		} else {
			throw new WechatException(Message.WEIXIN_HTTPS_REQUEST_ERROR);
		}
		return wxArticlesResponse;
	}

	/**
	 * 获取素材总数
	 * 
	 * @param accesstoken
	 * @param accesstoken
	 * 
	 * @return WxCountResponse 素材数目返回结果
	 * @throws WechatException
	 */
	public static WxCountResponse getMediaCount(String accesstoken)
			throws WechatException {
		WxCountResponse wxCountResponse = null;
		String requestUrl = material_get_materialcount_url.replace(
				"ACCESS_TOKEN", accesstoken);
		JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST", null);
		logger.info("weixin response：{}", result.toString());
		if (!result.has("errcode")) {
			// logger.error("上传图文消息失败！errcode=" + result.getString("errcode")
			// + ",errmsg = " + result.getString("errmsg"));
			// throw new WechatException("上传图文消息失败！errcode="
			// + result.getString("errcode") + ",errmsg = "
			// + result.getString("errmsg"));
			wxCountResponse = new WxCountResponse();
			wxCountResponse.setImage_count(result.getString("image_count"));
			wxCountResponse.setNews_count(result.getString("news_count"));
			wxCountResponse.setVideo_count(result.getString("video_count"));
			wxCountResponse.setVoice_count(result.getString("voice_count"));
		}
		return wxCountResponse;
	}

	/**
	 * 获取永久素材
	 * 
	 * @param accesstoken
	 *            图文集合，数量不大于10
	 * @return WxArticlesResponse 上传图文消息素材返回结果
	 * @throws WechatException
	 */
	public static WxArticlesRespponseByMaterial getArticlesByMaterial(
			String accesstoken, String mediaId) throws WechatException {
		WxArticlesRespponseByMaterial wxArticlesRespponseByMaterial = null;
		String requestUrl = material_get_material_url.replace("ACCESS_TOKEN",
				accesstoken);
		WxArticlesRequestByMaterial wxArticlesRequestByMaterial = new WxArticlesRequestByMaterial();
		wxArticlesRequestByMaterial.setMedia_id(mediaId);
		JSONObject obj = JSONObject.fromObject(wxArticlesRequestByMaterial);
		JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
				obj.toString());
		logger.info("weixin response：{}", result.toString());
		if (!result.has("errcode")) {
			// logger.error("获得消息失败！errcode=" + result.getString("errcode")
			// + ",errmsg = " + result.getString("errmsg"));
			// throw new WechatException("获得消息失败！errcode="
			// + result.getString("errcode") + ",errmsg = "
			// + result.getString("errmsg"));
			wxArticlesRespponseByMaterial = (WxArticlesRespponseByMaterial) JSONObject
					.toBean(result, WxArticlesRespponseByMaterial.class);
		}
		return wxArticlesRespponseByMaterial;
	}

	/**
	 * 验证永久素材是否存在
	 * 
	 * @param accesstoken
	 * @param mediaId
	 */
	public static boolean getMaterialExist(String accesstoken, String mediaId) {
		String requestUrl = material_get_material_url.replace("ACCESS_TOKEN",
				accesstoken);
		WxArticlesRequestByMaterial wxArticlesRequestByMaterial = new WxArticlesRequestByMaterial();
		wxArticlesRequestByMaterial.setMedia_id(mediaId);
		JSONObject obj = JSONObject.fromObject(wxArticlesRequestByMaterial);
		boolean result = WxstoreUtils.httpRequestExist(accesstoken, requestUrl, "POST",
				obj.toString());
		logger.info("result {}", result);
		return result;
	}

	/**
	 * 删除永久素材
	 * 
	 * @param accesstoken
	 * @param mediaId
	 *            图文集合，数量不大于10
	 * @return WxArticlesRespponseByMaterial 上传图文消息素材返回结果
	 * @throws WechatException
	 */
	public static void deleteArticlesByMaterial(String accesstoken,
			String mediaId) throws WechatException {
		if (accesstoken != null && StringUtils.isNotEmpty(mediaId)) {
			String requestUrl = material_del_material_url.replace(
					"ACCESS_TOKEN", accesstoken);
			WxArticlesRequestByMaterial wxArticlesRequestByMaterial = new WxArticlesRequestByMaterial();
			wxArticlesRequestByMaterial.setMedia_id(mediaId);
			JSONObject obj = JSONObject.fromObject(wxArticlesRequestByMaterial);
			JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
					obj.toString());
			logger.info("weixin response：{}", result.toString());
			// if (result.has("errcode") && result.get("errcode") != "0") {
			// logger.error("删除消息失败！errcode=" + result.getString("errcode")
			// + ",errmsg = " + result.getString("errmsg"));
			// throw new WechatException("删除消息失败！errcode="
			// + result.getString("errcode") + ",errmsg = "
			// + result.getString("errmsg"));
			// }
		}
	}

	/**
	 * 修改永久素材
	 * 
	 * @param accesstoken
	 * @param wxUpdateArticle
	 * @throws WechatException
	 */
	public static void updateArticlesByMaterial(String accesstoken,
			WxUpdateArticle wxUpdateArticle) throws WechatException {
		String requestUrl = material_update_news_url.replace("ACCESS_TOKEN",
				accesstoken);
		JSONObject obj = JSONObject.fromObject(wxUpdateArticle);
		JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
				obj.toString());
		logger.info("weixin response：{}", result.toString());
		// if (result.has("errcode") && result.get("errcode") != "0") {
		// logger.error("消息失败！errcode=" + result.getString("errcode")
		// + ",errmsg = " + result.getString("errmsg"));
		// throw new WechatException("消息消息失败！errcode="
		// + result.getString("errcode") + ",errmsg = "
		// + result.getString("errmsg"));
		// }
	}

	/**
	 * 获取素材列表
	 * 
	 * @param accesstoken
	 *            ,type,offset,count
	 * @throws WechatException
	 */
	public static WxNews queryArticlesByMaterial(String accesstoken,
			String type, int offset, int count) throws WechatException {
		WxNews wn = null;
		String requestUrl = material_batchget_material_url.replace(
				"ACCESS_TOKEN", accesstoken);
		JSONObject obj = new JSONObject();
		obj.put("type", type);
		obj.put("offset", offset);
		obj.put("count", count);
		JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
				obj.toString());
		logger.info("weixin response：{}", result.toString());
		if (result.has("errcode") && result.get("errcode") != "0") {
			logger.error("消息失败！errcode=" + result.getString("errcode")
					+ ",errmsg = " + result.getString("errmsg"));
			// throw new WechatException("消息消息失败！errcode="
			// + result.getString("errcode") + ",errmsg = "
			// + result.getString("errmsg"));
		} else {
			wn = (WxNews) JSONObject.toBean(result, WxNews.class);
		}
		return wn;
	}

    /**
     * 获取素材列表
     *
     * @param accessToken
     * @param type
     * @param offset
     * @param count
     * @return
     */
    public static WxMaterialPage queryMaterial(String accessToken, String type, int offset, int count) {
        String requestUrl = material_batchget_material_url.replace("ACCESS_TOKEN", accessToken);
        JSONObject obj = new JSONObject();
        obj.put("type", type);
        obj.put("offset", offset);
        obj.put("count", count);
        JSONObject result = WxstoreUtils.httpRequest(accessToken, requestUrl, "POST", obj.toString());
        WxMaterialPage WxMaterialPage = com.alibaba.fastjson.JSONObject.parseObject(result.toString(), WxMaterialPage.class);

        if (WxMaterialPage.fail()) {
            logger.error("消息失败！errcode = {},errmsg = {}",
                         WxMaterialPage.getErrcode(), WxMaterialPage.getErrmsg());
        }
        return WxMaterialPage;
    }

	/**
	 * 新增永久图文素材
	 * 
	 * @param accesstoken
	 * @param wxArticles
	 * @return
	 * @throws WechatException
	 */
	public static String getMediaIdByMaterial(String accesstoken,
			List<WxArticle> wxArticles) throws WechatException {
		WxArticlesResponse response = uploadArticlesByMaterial(accesstoken,
				wxArticles);
		if (response != null) {
			return response.getMedia_id();
		}
		return null;
	}

	/**
	 * 新增其他类型永久素材
	 *
	 *            媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
	 * @return
	 * @throws Exception
	 */
	public static WxMediaForMaterialResponse uploadMediaFileByMaterial(
			String accesstoken, WxMediaForMaterial wx) throws WechatException {
		WxMediaForMaterialResponse mediaResource = null;
		String requestUrl = material_add_material_url.replace("ACCESS_TOKEN",
				accesstoken).replace("TYPE", wx.getType());

		File file = new File(wx.getFilePath() + wx.getFileName());
		String contentType = WeiXinReqUtil.getFileContentType(wx.getFileName()
				.substring(wx.getFileName().lastIndexOf(".") + 1));
		JSONObject result = WxstoreUtils.uploadMediaFile(accesstoken, requestUrl, file,
				contentType);
		if ("video" == wx.getType()) {
			WxDescriptionRequest wr = new WxDescriptionRequest();
			wr.setDescription(wx.getWxDescription());
			JSONObject obj = JSONObject.fromObject(wr);
			WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST", obj.toString());
		}

		logger.info("weixin response：{}", result.toString());
		if (!result.containsKey("errcode")) {
			mediaResource = new WxMediaForMaterialResponse();
			mediaResource.setMedia_id(result.getString("media_id"));
			mediaResource.setUrl(result.getString("url"));
		}
		return mediaResource;
	}

	/**
	 * 永久获取多媒体资源的mediaId
	 * 
	 * @param accesstoken
	 * @return
	 * @throws WechatException
	 */
	public static String getMediaIdForMaterial(String accesstoken,
			WxMediaForMaterial wxMediaForMaterial) throws WechatException {
		WxMediaForMaterialResponse response = uploadMediaFileByMaterial(
				accesstoken, wxMediaForMaterial);
		if (response != null) {
			return response.getMedia_id();
		}
		return null;
	}

}
