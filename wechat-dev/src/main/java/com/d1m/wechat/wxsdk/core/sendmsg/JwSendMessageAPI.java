package com.d1m.wechat.wxsdk.core.sendmsg;

import java.io.File;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.util.Constants;
import com.d1m.wechat.util.Message;
import com.d1m.wechat.wxsdk.core.common.WxstoreUtils;
import com.d1m.wechat.wxsdk.core.req.model.user.Group;
import com.d1m.wechat.wxsdk.core.sendmsg.model.SendMessageReport;
import com.d1m.wechat.wxsdk.core.sendmsg.model.SendMessageResponse;
import com.d1m.wechat.wxsdk.core.sendmsg.model.WxArticle;
import com.d1m.wechat.wxsdk.core.sendmsg.model.WxArticlesRequest;
import com.d1m.wechat.wxsdk.core.sendmsg.model.WxArticlesResponse;
import com.d1m.wechat.wxsdk.core.sendmsg.model.WxMassMessage;
import com.d1m.wechat.wxsdk.core.sendmsg.model.WxMedia;
import com.d1m.wechat.wxsdk.core.sendmsg.model.WxMediaResponse;
import com.d1m.wechat.wxsdk.core.util.WeiXinReqUtil;
import com.d1m.wechat.wxsdk.user.user.model.Wxuser;

public class JwSendMessageAPI {

	private static Logger logger = LoggerFactory
			.getLogger(JwSendMessageAPI.class);

	// 消息预览URL
	private static String message_preview_url = "https://api.weixin.qq.com/cgi-bin/message/mass/preview?access_token=ACCESS_TOKEN";
	// 上传媒体资源URL
	private static String upload_media_url = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	// 上传图文素材资源URL
	private static String upload_article_url = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=ACCESS_TOKEN";
	// 根据分组进行群发URL
	private static String message_group_url = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=ACCESS_TOKEN";
	// 根据OpenID列表群发URL
	private static String message_openid_url = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN";
	// 删除群发URL
	private static String message_delete_url = "https://api.weixin.qq.com/cgi-bin/message/mass/delete?access_token=ACCESS_TOKEN";
	// 查询群发消息发送状态URL
	private static String message_get_url = "https://api.weixin.qq.com/cgi-bin/message/mass/get?access_token=ACCESS_TOKEN";

	/**
	 * 图文消息预览
	 * 
	 * @param touser
	 *            接收人openid
	 * @param wxArticles
	 *            图文集合
	 * @throws WechatException
	 */
	public static SendMessageResponse messagePrivate(String accesstoken,
			String touser, List<WxArticle> wxArticles) throws WechatException {
		String requestUrl = message_preview_url.replace("ACCESS_TOKEN",
				accesstoken);
		String mediaId = getMediaId(accesstoken, wxArticles);
		JSONObject obj = new JSONObject();
		JSONObject mpnews = new JSONObject();
		obj.put("touser", touser);
		obj.put("msgtype", "mpnews");
		mpnews.put("media_id", mediaId);
		obj.put("mpnews", mpnews);
		JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
				obj.toString());
		logger.info("weixin response：{}", result.toString());
		SendMessageResponse response = (SendMessageResponse) JSONObject.toBean(
				result, SendMessageResponse.class);
		return response;
	}

	/**
	 * 已推送图文消息预览
	 * 
	 * @param touser
	 *            接收人openid
	 * @param mediaId
	 *            图文集合
	 * @throws WechatException
	 */
	public static SendMessageResponse messagePreview(String accesstoken,
			String touser, String mediaId) throws WechatException {
		String requestUrl = message_preview_url.replace("ACCESS_TOKEN",
				accesstoken);
		JSONObject obj = new JSONObject();
		obj.put("touser", touser);
		obj.put("msgtype", "mpnews");
		JSONObject mpnews = new JSONObject();
		mpnews.put("media_id", mediaId);
		obj.put("mpnews", mpnews);
		JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
				obj.toString());
		logger.info("weixin response：{}", result.toString());
		SendMessageResponse response = (SendMessageResponse) JSONObject.toBean(
				result, SendMessageResponse.class);
		return response;
	}

	/**
	 * 文本消息预览
	 * 
	 * @param touser
	 * @param content
	 * @throws Exception
	 */
	public static String messagePrivate(String accesstoken, String touser,
			String content) throws WechatException {
		String ret = "";
		String requestUrl = message_preview_url.replace("ACCESS_TOKEN",
				accesstoken);
		JSONObject obj = new JSONObject();
		obj.put("touser", touser);
		obj.put("msgtype", "text");
		JSONObject text = new JSONObject();
		text.put("content", content);
		obj.put("text", text);
		JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
				obj.toString());
		ret = result.toString();
		return ret;
	}

	/**
	 * 语音，图片，视频消息预览
	 * 
	 * @param touser
	 * @throws Exception
	 */
	public static void messagePrivate(String accesstoken, String touser,
			WxMedia wxMedia) throws WechatException {
		String requestUrl = message_preview_url.replace("ACCESS_TOKEN",
				accesstoken);
		String mediaId = getMediaId(accesstoken, wxMedia);
		JSONObject obj = new JSONObject();
		JSONObject type = new JSONObject();
		obj.put("touser", touser);
		obj.put("msgtype", wxMedia.getType());
		type.put("media_id", mediaId);
		obj.put(wxMedia.getType(), type);
		JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
				obj.toString());
	}

	/**
	 * 群发图文消息到指定的微信分组或所有人
	 * 
	 * @param accesstoken
	 * @param is_to_all
	 *            是否发送给所有人 ，ture 发送给所有人，false 按组发送
	 * @param group
	 *            微信的用户组，如果is_to_all=false,则字段必须填写
	 * @param wxArticles
	 *            图文素材集合
	 * @return
	 * @throws WechatException
	 */
	public static SendMessageResponse sendMessageToGroupOrAllWithArticles(
			String accesstoken, boolean is_to_all, Group group,
			List<WxArticle> wxArticles) throws WechatException {
		SendMessageResponse response = null;
		String requestUrl = message_group_url.replace("ACCESS_TOKEN",
				accesstoken);
		String mediaId = getMediaId(accesstoken, wxArticles);
		JSONObject obj = new JSONObject();
		JSONObject filter = new JSONObject();
		JSONObject mpnews = new JSONObject();

		filter.put("is_to_all", is_to_all);
		if (!is_to_all) {
			filter.put("group_id", group.getId());

		}
		obj.put("filter", filter);

		mpnews.put("media_id", mediaId);
		obj.put("mpnews", mpnews);

		obj.put("msgtype", "mpnews");
		JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
				obj.toString());
		response = (SendMessageResponse) JSONObject.toBean(result,
				SendMessageResponse.class);
		return response;
	}

	/**
	 * 群发消息（图文，图片，文本）到所有人
	 * 
	 * @param accesstoken
	 * @param mediaId
	 * @param msgtype
	 * @return
	 * @throws WechatException
	 */
	public static SendMessageResponse sendMessageToAllWithArticles(
			String accesstoken, String mediaId, String msgtype, 
			String content)throws WechatException{
		String requestUrl = message_group_url.replace("ACCESS_TOKEN",
				accesstoken);
		JSONObject obj = new JSONObject();
		JSONObject filter = new JSONObject();
		JSONObject msgType = new JSONObject();

		filter.put("is_to_all", true);
		obj.put("filter", filter);

		if(content != null){
			msgType.put("content", content);
		}else{
			msgType.put("media_id", mediaId);
		}
		obj.put(msgtype, msgType);

		obj.put("msgtype", msgtype);
		JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
				obj.toString());
		SendMessageResponse response = new SendMessageResponse();
		response.setErrcode(result.getString("errcode"));
		response.setErrmsg(result.getString("errmsg"));
		response.setMsg_id(result.getString("msg_id"));
		return response;
		
	}

	/**
	 * 群发文本消息到指定的微信分组或所有人
	 * 
	 * @param accesstoken
	 * @param is_to_all
	 *            是否发送给所有人 ，ture 发送给所有人，false 按组发送
	 * @param group
	 *            微信的用户组，如果is_to_all=false,则字段必须填写
	 * @param content
	 *            文本内容
	 * @return
	 * @throws WechatException
	 */
	public static SendMessageResponse sendMessageToGroupOrAllWithText(
			String accesstoken, boolean is_to_all, Group group, String content)
			throws WechatException {
		String requestUrl = message_group_url.replace("ACCESS_TOKEN",
				accesstoken);
		JSONObject filter = new JSONObject();
		filter.put("is_to_all", is_to_all);
		if (!is_to_all) {
			filter.put("group_id", group.getId());
		}
		JSONObject obj = new JSONObject();
		obj.put("filter", filter);
		JSONObject text = new JSONObject();
		text.put("content", content);
		obj.put("text", text);
		obj.put("msgtype", "text");
		JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
				obj.toString());
		SendMessageResponse response = (SendMessageResponse) JSONObject.toBean(
				result, SendMessageResponse.class);
		return response;
	}

	/**
	 * 使用语音、图片、视频群发消息到指定的微信分组或所有人
	 * 
	 * @param accesstoken
	 * @param is_to_all
	 *            是否发送给所有人 ，ture 发送给所有人，false 按组发送
	 * @param group
	 *            微信的用户组，如果is_to_all=false,则字段必须填写
	 * @param wxMedia
	 *            多媒体资源, 语音为voice， 图片为image，视频为video
	 * @return
	 * @throws WechatException
	 */
	public static SendMessageResponse sendMessageToGroupOrAllWithMedia(
			String accesstoken, boolean is_to_all, Group group, WxMedia wxMedia)
			throws WechatException {
		String requestUrl = message_group_url.replace("ACCESS_TOKEN",
				accesstoken);
		String mediaId = getMediaId(accesstoken, wxMedia);
		JSONObject filter = new JSONObject();
		filter.put("is_to_all", is_to_all);
		if (!is_to_all) {
			filter.put("group_id", group.getId());
		}
		JSONObject obj = new JSONObject();
		obj.put("filter", filter);
		JSONObject media = new JSONObject();
		media.put("media_id", mediaId);
		obj.put(wxMedia.getType(), media);
		obj.put("msgtype", wxMedia.getType());
		JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
				obj.toString());
		SendMessageResponse response = (SendMessageResponse) JSONObject.toBean(
				result, SendMessageResponse.class);
		return response;
	}

	/**
	 * 群发图文消息到指定的微信openid数组
	 * 
	 * @param accesstoken
	 * @param wxusers
	 *            接受消息的微信用户数组
	 *            图文素材集合
	 * @return
	 * @throws WechatException
	 */
	public static SendMessageResponse sendMessageToOpenidsWithArticles(
			String accesstoken, Wxuser[] wxusers, WxMassMessage wxMassMessage)
			throws WechatException {
		String requestUrl = message_openid_url.replace("ACCESS_TOKEN",
				accesstoken);
		List<String> openids = new ArrayList<String>();
		for (Wxuser wxuser : wxusers) {
			openids.add(wxuser.getOpenid());
		}
		String mediaId = wxMassMessage.getMedia_id();
		JSONObject obj = new JSONObject();
		obj.put("touser", openids);
		JSONObject mpnews = new JSONObject();
		mpnews.put("media_id", mediaId);
		obj.put("mpnews", mpnews);
		// obj.put("msgtype", "mpnews");
		obj.put("msgtype", wxMassMessage.getMsgtype());
		logger.info("request data : {}", obj.toString());
		JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
				obj.toString());
		SendMessageResponse response = (SendMessageResponse) JSONObject.toBean(
				result, SendMessageResponse.class);
		return response;
	}

	/**
	 * 群发文本消息到指定的微信openid数组
	 * 
	 * @param accesstoken
	 * @param wxusers
	 *            接受消息的微信用户数组
	 *            文本内容
	 * @return
	 * @throws WechatException
	 */
	public static SendMessageResponse sendMessageToOpenidsWithText(
			String accesstoken, Wxuser[] wxusers, WxMassMessage wxMassMessage)
			throws WechatException {
		String requestUrl = message_openid_url.replace("ACCESS_TOKEN",
				accesstoken);
		List<String> openids = new ArrayList<String>();
		for (Wxuser wxuser : wxusers) {
			openids.add(wxuser.getOpenid());
		}
		JSONObject obj = new JSONObject();
		JSONObject text = new JSONObject();
		obj.put("touser", openids);
		text.put("content", wxMassMessage.getText());
		obj.put("text", text);
		// obj.put("msgtype", "text");
		obj.put("msgtype", wxMassMessage.getMsgtype());
		JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
				obj.toString());
		SendMessageResponse response = (SendMessageResponse) JSONObject.toBean(
				result, SendMessageResponse.class);
		return response;
	}

	/**
	 * 使用语音、图片、视频群发消息到指定的微信openid数组
	 * 
	 * @param accesstoken
	 * @param wxusers
	 *            接受消息的微信用户数组
	 *            多媒体资源, 语音为voice， 图片为image，视频为video
	 * @return
	 * @throws WechatException
	 */
	public static SendMessageResponse sendMessageToOpenidsWithMedia(
			String accesstoken, Wxuser[] wxusers, WxMassMessage wxMassMessage)
			throws WechatException {
		String requestUrl = message_openid_url.replace("ACCESS_TOKEN",
				accesstoken);
		List<String> openids = new ArrayList<String>();
		for (Wxuser wxuser : wxusers) {
			openids.add(wxuser.getOpenid());
		}
		String mediaId = wxMassMessage.getMedia_id();
		JSONObject obj = new JSONObject();
		obj.put("touser", openids);
		JSONObject media = new JSONObject();
		media.put("media_id", mediaId);
		obj.put(wxMassMessage.getMsgtype(), media);
		obj.put("msgtype", wxMassMessage.getMsgtype());
		JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
				obj.toString());
		SendMessageResponse response = (SendMessageResponse) JSONObject.toBean(
				result, SendMessageResponse.class);
		return response;
	}

	/**
	 * 根据群发的msg_id删除群发<br/>
	 * 请注意，只有已经发送成功的消息才能删除删除消息只是将消息的图文详情页失效，已经收到的用户，还是能在其本地看到消息卡片。
	 * 另外，删除群发消息只能删除图文消息和视频消息，其他类型的消息一经发送，无法删除。
	 * 
	 * @param accesstoken
	 * @param msg_id
	 *            群发消息的msg_id
	 * @return
	 * @throws WechatException
	 */
	public static String deleteSendMessage(String accesstoken, String msg_id)
			throws WechatException {
		String requestUrl = message_delete_url.replace("ACCESS_TOKEN",
				accesstoken);
		JSONObject obj = new JSONObject();
		obj.put("msg_id", msg_id);
		JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
				obj.toString());
		return result.toString();
	}

	/**
	 * 根据群发的msg_id查询群发消息发送状态
	 * 
	 * @param accesstoken
	 * @param msg_id
	 *            群发消息的msg_id
	 * @return true表示发送成功，false表示发送失败
	 * @throws WechatException
	 */
	public static boolean getSendMessageStatus(String accesstoken, String msg_id)
			throws WechatException {
		boolean response = false;
		String requestUrl = message_get_url
				.replace("ACCESS_TOKEN", accesstoken);
		JSONObject obj = new JSONObject();
		obj.put("msg_id", msg_id);
		JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
				obj.toString());
		if (result.containsKey("msg_status")) {
			if ("SEND_SUCCESS".equalsIgnoreCase(result.getString("msg_status"))) {
				response = true;
			}
		}
		return response;
	}

	/**
	 * 
	 * 根据微信事件推送群发结果获取群发消息的发送报告
	 * 
	 * @param xmlString
	 *            信事件推送群发结果xmlString
	 * @return 微信发送报告实体对象
	 * @throws WechatException
	 */
	@SuppressWarnings("rawtypes")
	public static SendMessageReport getReportBySendMessageReturnString(
			String xmlString) throws WechatException {
		SendMessageReport report = new SendMessageReport();
		SAXBuilder build = new SAXBuilder();
		Document doc = null;
		try {
			doc = build.build(new StringReader(xmlString));
		} catch (Exception e1) {
			logger.error(e1.getMessage());
			return null;
		}
		Element root = doc.getRootElement();
		Iterator itr = (root.getChildren()).iterator();
		Class<SendMessageReport> clazz = SendMessageReport.class;
		while (itr.hasNext()) {
			Element oneLevelDeep = (Element) itr.next();
			try {
				Field filed = clazz.getDeclaredField(oneLevelDeep.getName());
				filed.setAccessible(true);
				filed.set(report, oneLevelDeep.getText());
				filed.setAccessible(false);
			} catch (NoSuchFieldException e) {
				logger.error(e.getMessage());
			} catch (SecurityException e) {
				logger.error(e.getMessage());
			} catch (IllegalArgumentException e) {
				logger.error(e.getMessage());
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage());
			}
		}
		return report;
	}

	/**
	 * 获取多媒体资源的mediaId
	 * 
	 * @param accesstoken
	 * @param wxMedia
	 * @return
	 * @throws WechatException
	 */
	public static String getMediaId(String accesstoken, WxMedia wxMedia)
			throws WechatException {
		WxMediaResponse response = uploadMediaFile(accesstoken,
				wxMedia.getFilePath(), wxMedia.getFileName(), wxMedia.getType());
		if (response != null) {
			return response.getMedia_id();
		}
		return null;
	}

	public static String getMediaId(String accesstoken,
			List<WxArticle> wxArticles) throws WechatException {
		WxArticlesResponse response = uploadArticles(accesstoken, wxArticles);
		if (response != null) {
			return response.getMedia_id();
		}
		return null;
	}

	/**
	 * 上传图文消息素材
	 * 
	 * @param accesstoken
	 * @param wxArticles
	 *            图文集合，数量不大于10
	 * @return WxArticlesResponse 上传图文消息素材返回结果
	 * @throws WechatException
	 */
	public static WxArticlesResponse uploadArticles(String accesstoken,
			List<WxArticle> wxArticles) throws WechatException {
		WxArticlesResponse wxArticlesResponse = null;
		if (wxArticles.size() == 0) {
			throw new WechatException(Message.WEIXIN_UPLOAD_ARTICLES_NOT_EMPTY);
		}
		if (wxArticles.size() > Constants.MAX_UPLOAD_WX_ARTICLES_SIEZ) {
			throw new WechatException(
					Message.WEIXIN_UPLOAD_ARTICLES_OVER_MAX_SIZE);
		}
		String requestUrl = upload_article_url.replace("ACCESS_TOKEN",
				accesstoken);
		for (WxArticle article : wxArticles) {
			if (article.getFileName() != null
					&& article.getFileName().length() > 0) {
				String mediaId = getFileMediaId(accesstoken, article);
				article.setThumb_media_id(mediaId);
			}
		}
		WxArticlesRequest wxArticlesRequest = new WxArticlesRequest();
		wxArticlesRequest.setArticles(wxArticles);
		JSONObject obj = JSONObject.fromObject(wxArticlesRequest);
		JSONObject result = WxstoreUtils.httpRequest(accesstoken, requestUrl, "POST",
				obj.toString());
		if (!result.has("errcode")) {
			wxArticlesResponse = new WxArticlesResponse();
			wxArticlesResponse.setMedia_id(result.getString("media_id"));
			wxArticlesResponse.setType(result.getString("type"));
			wxArticlesResponse.setCreated_at(new Date(result
					.getLong("created_at") * 1000));
		}
		return wxArticlesResponse;
	}

	/**
	 * 获取文件上传文件的media_id
	 * 
	 * @param accesstoken
	 * @param article
	 * @return
	 * @throws WechatException
	 */
	public static String getFileMediaId(String accesstoken, WxArticle article)
			throws WechatException {
		WxMediaResponse response = uploadMediaFile(accesstoken,
				article.getFilePath(), article.getFileName(), "image");
		if (response != null) {
			return response.getMedia_id();
		}
		return null;
	}

	/**
	 * 上传媒体资源
	 * 
	 * @param filePath
	 * @param fileName
	 * @param type
	 *            媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
	 * @return
	 * @throws Exception
	 */
	public static WxMediaResponse uploadMediaFile(String accesstoken,
			String filePath, String fileName, String type)
			throws WechatException {
		WxMediaResponse mediaResource = null;
		if (accesstoken != null) {
			String requestUrl = upload_media_url.replace("ACCESS_TOKEN",
					accesstoken).replace("TYPE", type);
			File file = new File(filePath + fileName);
			String contentType = WeiXinReqUtil.getFileContentType(fileName
					.substring(fileName.lastIndexOf(".") + 1));
			JSONObject result = WxstoreUtils.uploadMediaFile(accesstoken, requestUrl, file,
					contentType);
			logger.debug("result : {}", result);
			if (!result.containsKey("errcode")) {
				// {"type":"TYPE","media_id":"MEDIA_ID","created_at":123456789}
				mediaResource = new WxMediaResponse();
				mediaResource.setMedia_id(result.getString("media_id"));
				mediaResource.setType(result.getString("type"));
				mediaResource.setCreated_at(new Date(result
						.getLong("created_at") * 1000));
			}
		}
		return mediaResource;
	}

}
