package com.d1m.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.customization.zegna.ZegnaCons;
import com.d1m.wechat.dto.*;
import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.mapper.*;
import com.d1m.wechat.model.*;
import com.d1m.wechat.model.enums.*;
import com.d1m.wechat.pamametermodel.ConversationModel;
import com.d1m.wechat.pamametermodel.MassConversationModel;
import com.d1m.wechat.pamametermodel.MemberModel;
import com.d1m.wechat.schedule.TaskParam;
import com.d1m.wechat.schedule.job.BatchMassConversationJob;
import com.d1m.wechat.schedule.job.MassConversationJob;
import com.d1m.wechat.service.*;
import com.d1m.wechat.service.engine.EngineContext;
import com.d1m.wechat.service.engine.event.IEvent;
import com.d1m.wechat.util.*;
import com.d1m.wechat.websocket.WebSocketEndPoint;
import com.d1m.wechat.wxsdk.core.req.model.kfaccount.*;
import com.d1m.wechat.wxsdk.core.sendmsg.JwKfaccountAPI;
import com.d1m.wechat.wxsdk.core.sendmsg.JwSendMessageAPI;
import com.d1m.wechat.wxsdk.core.sendmsg.model.SendMessageResponse;
import com.d1m.wechat.wxsdk.core.sendmsg.model.WxMassMessage;
import com.d1m.wechat.wxsdk.user.user.model.Wxuser;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.d1m.wechat.util.IllegalArgumentUtil.notBlank;

@Service
public class ConversationServiceImpl extends BaseService<Conversation>
		implements ConversationService {

	private static Logger log = LoggerFactory
			.getLogger(ConversationServiceImpl.class);

	@Autowired
	private ConversationMapper conversationMapper;

	@Autowired
    @Lazy
    private OriginalConversationService originalConversationService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private ConversationIndexMapper conversationIndexMapper;

	@Autowired
	private MaterialService materialService;

	@Autowired
	private MaterialTextDetailMapper materialTextDetailMapper;

	@Autowired
	private ConversationImageTextDetailMapper conversationImageTextDetailMapper;

	@Autowired
	private MassConversationMapper massConversationMapper;

	@Autowired
	private QrcodeService qrcodeService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private MassConversationResultMapper massConversationResultMapper;

	@Autowired
	private MassConversationBatchResultMapper massConversationBatchResultMapper;

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskCategoryMapper taskCategoryMapper;

	@Autowired
	private ConfigService configService;

	@Autowired
	private MassConversationBatchResultService massConversationBatchResultService;

	@Autowired
	private LiveChatService liveChatService;

	@Override
	public Mapper<Conversation> getGenericMapper() {
		return conversationMapper;
	}

	@Override
	public OriginalConversation preHandleMemberToWechat(Wechat wechat,
			Document document) {
		log.info("pre handle original xml : {}", document.asXML());
		OriginalConversation originalConversation = new OriginalConversation();
		originalConversation.setContent(document.asXML());
		originalConversation.setCreatedAt(new Date());
		originalConversation.setStatus(OriginalConversationStatus.WAIT
				.getValue());
		if(wechat!=null){
			originalConversation.setWechatId(wechat.getId());
		}
		originalConversationService.save(originalConversation);
		return originalConversation;
	}

	@Override
	public boolean memberToWechat(Member member, Document document,
								  OriginalConversation originalConversation) {
		Element root = document.getRootElement();
		Integer wechatId = originalConversation.getWechatId();
		String fromUserName = ParamUtil.getElementContent(root, "FromUserName");
		String toUserName = ParamUtil.getElementContent(root, "ToUserName");
		String createTime = ParamUtil.getElementContent(root, "CreateTime");
		Long msgId = ParamUtil.getLong(ParamUtil.getElementContent(root, "MsgId"), null);
		if (msgId == null) {
			msgId = ParamUtil.getLong(ParamUtil.getElementContent(root, "MsgID"), null);
		}

		String event = ParamUtil.getElementContent(root, "Event");
		String msgType = ParamUtil.getElementContent(root, "MsgType");
		Date current = new Date(ParamUtil.getLong(createTime, null) * 1000);

		// 判断重复
		boolean isRepeat = isConversationRepeat(fromUserName, createTime, msgId, wechatId, current);
		if (isRepeat) {
			log.info("original conversation id {} repeat.", originalConversation.getId());
			originalConversation.setWechatId(wechatId);
			originalConversation.setStatus(OriginalConversationStatus.REPEAT.getValue());
			originalConversationService.updateAll(originalConversation);
			return false;
		}

		MsgType mt = MsgType.getByName(msgType);
		Event et = Event.getValueByName(event);
		// 处理系统事件
		if (et != null && et.isSystem()) {
			IEvent eventEngine = EngineContext.getEventMaps(et);
			log.info("system event : {}, eventEngine : {}", et, eventEngine);
			eventEngine.handleSystem(originalConversation, document);
			log.info("system event process end.");
			return true;
		}

		// 存储消息会话记录
		Conversation conversation = saveConversation(wechatId, member, originalConversation.getId(), msgId, mt, et, root, current);

		//第三方session是否存在，存在则转发
		MemberConversationSession memberConversationSession = liveChatService.getMemberConversationSession(fromUserName, wechatId);
		if(et==null && memberConversationSession != null && ZegnaCons.ARVATO_MESSAGE_TYPE.contains(msgType)) {
			forwardMessage(wechatId, msgType, originalConversation);
			conversation.setEvent(Event.FORWARD_TO_PARTNER.getValue());
			conversationMapper.updateByPrimaryKey(conversation);
			return true;
		}

		//for avarto
		//客户点击转发客户
		if(et != null && et.equals(Event.CLICK)){
			String eventKey = ParamUtil.getElementContent(root, "EventKey");
			Menu menu = menuService.get(wechatId, Integer.parseInt(eventKey));
			if(menu != null && StringUtils.isNotBlank(menu.getName())){
				String menuTrigger = configService.getConfigValue(wechatId, "ARVATO", "TRIGGER_MENU_NAME");
				if(menu.getName().equalsIgnoreCase(menuTrigger)){
					log.info("Customer click " + menu.getName());
					if(memberConversationSession == null) {
						liveChatService.sendOldMsg(fromUserName, toUserName, wechatId, conversation.getCreatedAt());
						liveChatService.clickKefuMenuAndStartSession(toUserName, fromUserName, wechatId, conversation.getCreatedAt());
						return true;
					}
				}
			}
		}

		// 处理自定义回复和用户事件
		if (et == null) {
			IEvent eventEngine = null;
			if (mt.equals(MsgType.LOCATION)) {
				eventEngine = EngineContext.getEventMaps(Event.LOCATION);
				log.info("user event : {}, eventEngine : {}", Event.LOCATION, eventEngine);
				eventEngine.handle(conversation, member);
			} else {
				eventEngine = EngineContext.getEventMaps(Event.AUTO_REPLY);
				log.info("auto reply event : {}, eventEngine : {}", Event.AUTO_REPLY, eventEngine);

				//转发第三方
				if(eventEngine.handle(conversation, member) == null && memberConversationSession == null){
					log.info("Auto reply not matched, partner live chat session start");
					liveChatService.sendOldMsg(fromUserName, toUserName, wechatId, conversation.getCreatedAt());
					liveChatService.startSession(fromUserName, wechatId, conversation.getCreatedAt());
					forwardMessage(wechatId, msgType, originalConversation);
					return true;
				}
			}
		} else if (!et.equals(Event.LOCATION)) {
			IEvent eventEngine = EngineContext.getEventMaps(et);
			log.info("user event : {}, eventEngine : {}", event, eventEngine);
			if (eventEngine != null) {
				eventEngine.handle(conversation, member);
			}
			log.info("user event process end.");
		}

		if(!Event.LOCATION.equals(et)) {
			// 发送消息到websocket通道
			sendToWebsocket(member, conversation);
		}

		log.info("member to wechat end.");
		return true;
	}

	private void forwardMessage(Integer wechatId, String msgType, OriginalConversation originalConversation){
		boolean isSend = liveChatService.sendMsgToLiveChat(originalConversation.getContent(), wechatId);
		if(isSend) {
			originalConversation.setStatus(OriginalConversationStatus.FORWARD.getValue());
		}else{
			originalConversation.setStatus(OriginalConversationStatus.FORWARD_FAIL.getValue());
		}
		originalConversation.setWechatId(wechatId);
		originalConversationService.updateAll(originalConversation);
	}

	private void sendToWebsocket(Member member,Conversation conversation){
		try {
			ConversationDto dto = new ConversationDto();
			dto.setId(conversation.getId());
			dto.setCreatedAt(DateUtil.formatYYYYMMDDHHMMSS(conversation.getCreatedAt()));
			dto.setContent(conversation.getContent());
			dto.setCurrent(DateUtil.formatYYYYMMDDHHMMSS(conversation.getCreatedAt()));
			dto.setDir(conversation.getDirection() ? 1 : 0);
			dto.setIsMass(conversation.getIsMass() ? 1 : 0);
			dto.setMemberId(conversation.getMemberId() + "");
			dto.setMemberNickname(member.getNickname());
			dto.setMemberPhoto(conversation.getMemberPhoto());
			dto.setMsgType(conversation.getMsgType());
			dto.setStatus(conversation.getStatus());
			dto.setEvent(conversation.getEvent());
			WebSocketEndPoint.sendMessageToUser(conversation.getOpenId(),
					JSON.toJSONString(dto));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Conversation saveConversation(Integer wechatId, Member member,Integer originalConversationId, Long msgId,MsgType mt,Event et,Element root,Date current){
		String ticket = ParamUtil.getElementContent(root, "Ticket");
		String latitude = ParamUtil.getElementContent(root, "Latitude");
		String longitude = ParamUtil.getElementContent(root, "Longitude");
		String precision = ParamUtil.getElementContent(root, "Precision");
		String content = ParamUtil.getElementContent(root, "Content");// 文本
		String picUrl = ParamUtil.getElementContent(root, "PicUrl");// 图片
		String mediaId = ParamUtil.getElementContent(root, "MediaId");// 图片,语音,视频
		String format = ParamUtil.getElementContent(root, "Format");// 语音
		String recognition = ParamUtil.getElementContent(root, "Recognition");// 语音
		String thumbMediaId = ParamUtil.getElementContent(root, "ThumbMediaId");// 视频,小视频
		String locationX = ParamUtil.getElementContent(root, "Location_X");// 地理位置
		String locationY = ParamUtil.getElementContent(root, "Location_Y");// 地理位置
		String scale = ParamUtil.getElementContent(root, "Scale");// 地理位置
		String label = ParamUtil.getElementContent(root, "Label");// 地理位置
		String title = ParamUtil.getElementContent(root, "Title");
		String description = ParamUtil.getElementContent(root, "Description");
		String url = ParamUtil.getElementContent(root, "Url");// 链接
		String eventKey = ParamUtil.getElementContent(root, "EventKey");
		String menuID = ParamUtil.getElementContent(root, "MenuId");

		ConversationIndex conversationIndex = new ConversationIndex();
		conversationIndex.setCreatedAt(current);
		conversationIndex.setMsgId(msgId);
		conversationIndex.setOpenId(member.getOpenId());
		conversationIndex.setWechatId(wechatId);
		conversationIndexMapper.insert(conversationIndex);

		Conversation conversation = new Conversation();
		conversation.setOpenId(member.getOpenId());
		conversation.setTicket(ticket);
		conversation.setWechatId(wechatId);
		conversation.setMsgId(msgId != null ? msgId.toString() : null);
		conversation.setOriginalConversationId(originalConversationId);
		conversation.setCreatedAt(current);
		conversation.setDescription(description);
		conversation.setDirection(false);
		conversation.setEventKey(eventKey);
		conversation.setMenuId(ParamUtil.getInt(menuID, null));
		conversation.setFormat(format);
		conversation.setLabel(label);
		conversation.setMemberPhoto(member.getLocalHeadImgUrl());
		Double lx = ParamUtil.getDouble(locationX, null);
		if (lx == null) {
			lx = ParamUtil.getDouble(latitude, null);
		}
		Double ly = ParamUtil.getDouble(locationY, null);
		if (ly == null) {
			ly = ParamUtil.getDouble(longitude, null);
		}
		conversation.setLocationX(lx);
		conversation.setLocationY(ly);

		conversation.setMediaId(mediaId);
		conversation.setMemberId(member != null ? member.getId() : null);

		if (mt != null) {
			conversation.setMsgType(mt.getValue());
			if (mt == MsgType.IMAGE) {
				String dateFormat = DateUtil.yyyyMMddHHmmss.format(new Date());
				String type = Constants.IMAGE + File.separator
						+ Constants.NORMAL;
				File rootPath = FileUtils.getUploadPathRoot(type);
				File dir = new File(rootPath, dateFormat.substring(0, 6));
				String suffix = FileUtils.getRemoteImageSuffix(picUrl,
						Constants.JPG);
				String newFileName = FileUtils.generateNewFileName(
						MD5.MD5Encode(""), suffix);
				FileUtils.copyRemoteFile(picUrl, dir.getAbsolutePath(),
						newFileName);
				String picurl = FileUploadConfigUtil.getInstance().getValue(
						"upload_url_base")
						+ File.separator
						+ type
						+ File.separator
						+ dir.getName() + File.separator + newFileName;
				conversation.setContent(picurl);
				conversation.setPicUrl(picurl);
			} else if (mt == MsgType.TEXT) {
				conversation.setContent(content);
			} else if (mt == MsgType.VOICE) {
				conversation.setContent(mediaId);
			} else if (mt == MsgType.VIDEO) {
				conversation.setContent(mediaId);
			} else if (mt == MsgType.SHORTVIDEO) {
				conversation.setContent(mediaId);
			} else if (mt == MsgType.LOCATION) {
				JSONObject json = new JSONObject();
				json.put("location_x", locationX);
				json.put("location_y", locationY);
				json.put("scale", scale);
				json.put("label", label);
				conversation.setContent(json.toString());
			} else if (mt == MsgType.LINK) {
				JSONObject json = new JSONObject();
				json.put("title", title);
				json.put("description", description);
				json.put("url", url);
				conversation.setContent(json.toString());
			} else if (mt == MsgType.EVENT) {
				if (et == Event.SCAN) {
					Qrcode qrcode = qrcodeService.getBySceneId(wechatId, eventKey);
					if (qrcode != null) {
						conversation.setContent(qrcode.getName());
					} else {
						log.warn("wechatId : {}, qrcode EventKey : {}, not exist.", wechatId, eventKey);
					}
				} else if (et == Event.LOCATION) {
					JSONObject json = new JSONObject();
					json.put("latitude", latitude);
					json.put("longitude", longitude);
					json.put("precision", precision);
					conversation.setContent(json.toString());
				} else if (et == Event.CLICK) {
					Integer menuId = ParamUtil.getInt(eventKey, null);
					if (menuId != null) {
						Menu menu = menuService.get(wechatId, menuId);
						if (menu != null) {
							conversation.setContent(menu.getName());
						}
					} else {
						log.warn("wechatId : {}, menu EventKey : {}, not exist.", wechatId, eventKey);
					}
				} else if (et == Event.VIEW) {
					conversation.setContent(eventKey);
				} else if (et == Event.SUBSCRIBE) {
					conversation.setContent("SUBSCRIBE");
				} else if (et == Event.UNSUBSCRIBE) {
					conversation.setContent("UNSUBSCRIBE");
				} else if (et == Event.SCANCODE_PUSH) {
					conversation.setContent("SCANCODE_PUSH");
				} else if (et == Event.SCANCODE_WAITMSG) {
					conversation.setContent("SCANCODE_WAITMSG");
				} else if (et == Event.USER_GET_CARD) {
					conversation.setContent("USER_GET_CARD");
				} else if (et == Event.USER_CONSUME_CARD) {
					conversation.setContent("USER_CONSUME_CARD");
				} else if (et == Event.USER_GIFTING_CARD) {
					conversation.setContent("USER_GIFTING_CARD");
				}
			}
		}
		conversation.setThumbMediaId(ParamUtil.getInt(thumbMediaId, null));
		conversation.setLocationPrecision(ParamUtil.getDouble(precision, null));
		conversation.setRecognition(recognition);
		conversation.setScale(ParamUtil.getDouble(scale, null));
		conversation.setStatus(ConversationStatus.UNREAD.getValue());
		conversation.setTitle(title);
		conversation.setUnionId(member != null ? member.getUnionId() : null);
		conversation.setUrl(url);
		conversation.setIsMass(false);

		if (et == null) {
			if (mt.equals(MsgType.LOCATION)) {
				conversation.setEvent(Event.LOCATION.getValue());
			} else {
				conversation.setEvent(Event.AUTO_REPLY.getValue());
			}
		} else {
			conversation.setEvent(et.getValue());
		}
		conversationMapper.insert(conversation);
		log.info("conversation insert result: {}", conversation);
		return conversation;
	}

	private boolean isConversationRepeat(String fromUserName,
			String createTime, Long msgId, Integer wechatId, Date current) {
		ConversationIndex record = new ConversationIndex();
		record.setWechatId(wechatId);
		if (msgId != null) {
			record.setMsgId(msgId);
		} else {
			record.setOpenId(fromUserName);
			record.setCreatedAt(current);
		}
		int count = conversationIndexMapper.selectCount(record);
		if (count > 0) {
			log.warn(
					"conversation : MsgId {}, CreateTime : {}, FromUserName : {} repeat.",
					msgId, createTime, fromUserName);
			return true;
		}
		return false;
	}

	@Override
	public Conversation wechatToMember(Integer wechatId, User user,
			ConversationModel conversationModel) {
		log.info("wechat to member start.");
		if (conversationModel == null) {
			conversationModel = new ConversationModel();
		}
		notBlank(conversationModel.getMemberId(), Message.MEMBER_ID_NOT_EMPTY);
		if (conversationModel.getMaterialId() == null
				&& StringUtils.isBlank(conversationModel.getContent())) {
			throw new WechatException(Message.CONVERSATION_CONTENT_NOT_BLANK);
		}
		MemberDto member = memberService.getMemberDto(wechatId,
				conversationModel.getMemberId());
		notBlank(member, Message.MEMBER_NOT_EXIST);

//		if (!member.isOnline()) {
//			throw new WechatException(Message.CONVERSATION_MEMBER_NOT_ONLINE);
//		}

		KfcustomSend kfcustomSend = null;
		MsgType msgType = null;
		Date current = new Date();
		Material material = null;
		List<ConversationImageTextDetail> conversationImageTextDetails = new ArrayList<ConversationImageTextDetail>();
		ConversationImageTextDetail conversationImageTextDetail = null;
		if (conversationModel.getMaterialId() != null) {
			material = materialService.getMaterial(wechatId,
					conversationModel.getMaterialId());
			notBlank(material, Message.MATERIAL_NOT_EXIST);
			log.info("material mediaId : {}", material.getMediaId());
			if (material.getMaterialType() == MaterialType.IMAGE_TEXT
					.getValue()) {
				MaterialDto materialDto = materialService.getImageText(
						wechatId, conversationModel.getMaterialId());
				log.info("items : {}",
						materialDto.getItems() != null ? materialDto.getItems()
								.size() : "");
				List<ImageTextDto> items = materialDto.getItems();
				if (items != null && !items.isEmpty()) {
					JSONArray itemArray = new JSONArray();
					JSONObject itemJson = null;
					for (ImageTextDto imageTextDto : items) {
						conversationImageTextDetail = new ConversationImageTextDetail();
						conversationImageTextDetail.setAuthor(imageTextDto
								.getAuthor());
						conversationImageTextDetail.setContent(imageTextDto
								.getContent());
						conversationImageTextDetail
								.setContentSourceChecked(imageTextDto
										.getContentSourceChecked());
						conversationImageTextDetail
								.setContentSourceUrl(imageTextDto
										.getContentSourceUrl());
						conversationImageTextDetail.setShowCover(imageTextDto
								.isShowCover());
						conversationImageTextDetail.setSummary(imageTextDto
								.getSummary());
						conversationImageTextDetail.setTitle(imageTextDto
								.getTitle());
						conversationImageTextDetail
								.setMaterialCoverUrl(imageTextDto
										.getMaterialCoverUrl());
						conversationImageTextDetails
								.add(conversationImageTextDetail);
						itemJson = new JSONObject();
						itemJson.put("title", imageTextDto.getTitle());
						itemJson.put("summary", imageTextDto.getSummary());
						itemJson.put("materialCoverUrl",
								imageTextDto.getMaterialCoverUrl());
						itemArray.add(itemJson);
					}
					log.info("content : {}", itemArray.toJSONString());
					conversationModel.setContent(itemArray.toJSONString());
				}
				String mediaId = material.getMediaId();
				if (mediaId != null) {
					MsgMpNews msgMpNews = new MsgMpNews();
					msgMpNews.setMedia_id(material.getMediaId());
					kfcustomSend = new KfcustomSend();
					kfcustomSend.setMpnews(msgMpNews);
					msgType = MsgType.MPNEWS;
				} else {
					MsgNews msgNews = new MsgNews();
					List<MsgArticles> articles = new ArrayList<MsgArticles>();
					for (ImageTextDto imageTextDto : items) {
						MsgArticles article = new MsgArticles();
						article.setTitle(imageTextDto.getTitle());
						article.setDescription(imageTextDto.getSummary());
						article.setPicurl(imageTextDto.getMaterialCoverUrl());
						article.setUrl(imageTextDto.getContentSourceUrl());
						articles.add(article);
					}
					msgNews.setArticles(articles);
					kfcustomSend = new KfcustomSend();
					kfcustomSend.setNews(msgNews);
					msgType = MsgType.NEWS;
				}
			} else if (material.getMaterialType() == MaterialType.IMAGE
					.getValue()) {
				MsgImage msgImage = new MsgImage();
				msgImage.setMedia_id(material.getMediaId());
				kfcustomSend = new KfcustomSend();
				kfcustomSend.setImage(msgImage);
				msgType = MsgType.IMAGE;
				conversationModel.setContent(material.getPicUrl());
			} else if (material.getMaterialType() == MaterialType.TEXT
					.getValue()) {
				MaterialTextDetail materialTextDetail = new MaterialTextDetail();
				materialTextDetail.setMaterialId(material.getId());
				materialTextDetail.setWechatId(material.getWechatId());
				materialTextDetail = materialTextDetailMapper
						.selectOne(materialTextDetail);
				notBlank(materialTextDetail,
						Message.MATERIAL_TEXT_DETAIL_NOT_EXIST);

				MsgText msgText = new MsgText();
				msgText.setContent(materialTextDetail.getContent());
				conversationModel.setContent(materialTextDetail.getContent());
				kfcustomSend = new KfcustomSend();
				kfcustomSend.setText(msgText);
				msgType = MsgType.TEXT;
			} else if (material.getMaterialType() == MaterialType.VIDEO.getValue()) {
				msgType = MsgType.VIDEO;
				MsgVideo msgVideo = new MsgVideo();
				msgVideo.setMedia_id(material.getMediaId());
				msgVideo.setThumb_media_id(material.getMediaId());
				msgVideo.setTitle(material.getTitle());
				msgVideo.setDescription(material.getDescription());
				kfcustomSend = new KfcustomSend();
				kfcustomSend.setVideo(msgVideo);
				conversationModel.setContent(material.getName());
			} else{
				throw new WechatException(
						Message.CONVERSATION_CONTENT_NOT_SUPPORT);
			}
		} else {
			MaterialTextDetail materialTextDetail = new MaterialTextDetail();
			materialTextDetail.setContent(conversationModel.getContent());
			materialTextDetail.setWechatId(wechatId);
			materialTextDetail = materialTextDetailMapper
					.selectOne(materialTextDetail);
			if (materialTextDetail == null) {
				material = new Material();
				material.setCreatedAt(current);
				if (user != null) {
					material.setCreatorId(user.getId());
				} else {
					material.setCreatorId(-1);
				}
				material.setMaterialType(MaterialType.TEXT.getValue());
				material.setStatus(MaterialStatus.INUSED.getValue());
				material.setWechatId(wechatId);
				materialService.save(material);

				materialTextDetail = new MaterialTextDetail();
				materialTextDetail.setContent(conversationModel.getContent());
				materialTextDetail.setMaterialId(material.getId());
				materialTextDetail.setWechatId(wechatId);
				materialTextDetailMapper.insert(materialTextDetail);
			} else {
				material = materialService.getMaterial(wechatId,
						materialTextDetail.getMaterialId());
			}

			MsgText msgText = new MsgText();
			msgText.setContent(conversationModel.getContent());

			kfcustomSend = new KfcustomSend();
			kfcustomSend.setText(msgText);
			msgType = MsgType.TEXT;
		}

		log.info("msgType : {}", msgType);
		// Customservice customservice = new Customservice();
		// customservice.setKf_account(user != null ? user.getEmail() : null);
		// kfcustomSend.setCustomservice(customservice);
		kfcustomSend.setTouser(member.getOpenId());
		kfcustomSend.setMsgtype(msgType.name().toLowerCase());

		Conversation conversation = new Conversation();
		conversation.setContent(conversationModel.getContent());
		conversation.setCreatedAt(current);
		conversation.setDirection(true);
		conversation.setWechatId(wechatId);
		conversation.setUserId(user != null ? user.getId() : null);
		conversation.setMemberId(member.getId());
		conversation.setOpenId(member.getOpenId());
		conversation.setUnionId(member.getUnionId());
		conversation.setMsgType(msgType.getValue());
		conversation.setPicUrl(material.getPicUrl());
		conversation.setStatus(ConversationStatus.READ.getValue());
		conversation.setIsMass(false);
		conversation.setMemberPhoto(member.getLocalHeadImgUrl());
		conversation
				.setKfPhoto(user != null ? user.getLocalHeadImgUrl() : null);
		conversation.setMaterialId(material.getId());
		log.info("conversation : {}", conversation);
		conversationMapper.insert(conversation);
		log.info("conversation id : {}", conversation.getId());
		if (!conversationImageTextDetails.isEmpty()) {
			for (ConversationImageTextDetail citd : conversationImageTextDetails) {
				citd.setConversationId(conversation.getId());
			}
			conversationImageTextDetailMapper
					.insertList(conversationImageTextDetails);
		}
		String accessToken = RefreshAccessTokenJob.getAccessTokenStr(wechatId);
		kfcustomSend.setAccess_token(accessToken);
		JwKfaccountAPI.sendKfMessage(kfcustomSend);
		log.info("send message success.");
		log.info("wechat to member end.");
		return conversation;
	}

	@Override
	public Page<ConversationDto> search(Integer wechatId,
			ConversationModel conversationModel, boolean queryCount) {
		if (conversationModel == null) {
			conversationModel = new ConversationModel();
		}
		Page<ConversationDto> conversationDtos = new Page<ConversationDto>(
				conversationModel.getPageNum(), conversationModel.getPageSize());
		if (conversationModel.pagable()) {
			PageHelper.startPage(conversationModel.getPageNum(),
					conversationModel.getPageSize(), queryCount);
		}
		Date lastConversationAt = DateUtil.parseDate(conversationModel
				.getTime());
		conversationModel.setStartAt(DateUtil.parse(DateUtil.utc2DefaultLocal(
				conversationModel.getStart(), true)));
		conversationModel.setEndAt(new Date());
		conversationDtos = conversationMapper.search(wechatId,
				conversationModel.getMemberId(), conversationModel.getType(),
				conversationModel.getDir(), conversationModel.getStatus(),
				conversationModel.getStartAt(), conversationModel.getEndAt(),
				lastConversationAt, conversationModel.getMsgType(),
				conversationModel.getKeyWords(), "id", "desc");
		if (conversationModel.isUpdateRead()) {
			Conversation conversation = null;
			for (ConversationDto conversationDto : conversationDtos) {
				if (conversationDto.getStatus().equals(
						ConversationStatus.UNREAD.getValue())) {
					conversation = get(wechatId, conversationDto.getId());
					conversation.setStatus(ConversationStatus.READ.getValue());
					conversationMapper.updateByPrimaryKey(conversation);
				}
				if (conversationDto.getIsMass() != null
						&& conversationDto.getIsMass() == 1) {
					List<MassConversation> massConversations = getMassConversations(
							wechatId, conversationDto.getId());
					for (MassConversation massConversation : massConversations) {
						if (massConversation.getStatus().equals(
								ConversationStatus.UNREAD.getValue())) {
							massConversation.setStatus(ConversationStatus.READ
									.getValue());
							massConversationMapper
									.updateByPrimaryKey(massConversation);
						}
					}
				}
			}
		}
		return conversationDtos;
	}

	@Override
	public Page<ConversationDto> searchUnread(Integer wechatId,
			ConversationModel conversationModel, boolean queryCount) {
		if (conversationModel == null) {
			conversationModel = new ConversationModel();
		}
		Page<ConversationDto> conversationDtos = new Page<ConversationDto>(
				conversationModel.getPageNum(), conversationModel.getPageSize());
		if (conversationModel.pagable()) {
			PageHelper.startPage(conversationModel.getPageNum(),
					conversationModel.getPageSize(), queryCount);
		}
		Date lastConversationAt = DateUtil.parseDate(conversationModel
				.getTime());
		conversationDtos = conversationMapper.searchUp(wechatId,
				conversationModel.getMemberId(), conversationModel.getType(),
				conversationModel.getMsgTypes(), conversationModel.getDir(),
				conversationModel.getStatus(),
				DateUtil.getDateBegin(conversationModel.getStartAt()),
				DateUtil.getDateEnd(conversationModel.getEndAt()),
				lastConversationAt, "id", "desc");
		return conversationDtos;
	}

	private List<MassConversation> getMassConversations(Integer wechatId,
			Integer conversationId) {
		MassConversation record = new MassConversation();
		record.setWechatId(wechatId);
		record.setConversationId(conversationId);
		return massConversationMapper.select(record);
	}

	private Conversation get(Integer wechatId, Integer id) {
		Conversation record = new Conversation();
		record.setId(id);
		record.setWechatId(wechatId);
		return conversationMapper.selectOne(record);
	}

	@Override
	public Page<ConversationDto> searchMass(Integer wechatId,
											ConversationModel conversationModel, boolean queryCount) {
		if (conversationModel == null) {
			conversationModel = new ConversationModel();
		}
		if (conversationModel.pagable()) {
			PageHelper.startPage(conversationModel.getPageNum(),
					conversationModel.getPageSize(), queryCount);
		}
		Page<ConversationDto> conversationDtos = conversationMapper.searchMass(
				wechatId,
				DateUtil.getDateBegin(conversationModel.getStartAt()),
				DateUtil.getDateEnd(conversationModel.getEndAt()),
				conversationModel.getStatus(), conversationModel.getSortName(),
				conversationModel.getSortDir());
		return conversationDtos;
	}

	@Override
	public Integer countMass(Integer wechatId,
			ConversationModel conversationModel) {
		return conversationMapper.countMass(wechatId,
				conversationModel.getStartAt(), conversationModel.getEndAt(),
				conversationModel.getStatus());
	}

	@Override
	public Integer countMassAvalible(Integer wechatId) {
		Date current = new Date();
		Date startAt = DateUtil.getDateFirstDayOfMonth(current);
		Date endAt = DateUtil.getDateFirstDayOfNextMonth(current);
		return Constants.MAX_MASS_SIZE_PER_MONTH
				- conversationMapper.countMass(wechatId, startAt, endAt,
						MassConversationResultStatus.SEND_SUCCESS.getValue());
	}

	@Override
	public void preMassConversation(Integer wechatId, User user,
			MassConversationModel massConversationModel) {
		if (massConversationModel == null) {
			massConversationModel = new MassConversationModel();
		}
		if (massConversationModel.getMaterialId() == null
				&& StringUtils.isBlank(massConversationModel.getContent())) {
			throw new WechatException(Message.CONVERSATION_CONTENT_NOT_BLANK);
		}
		if(!massConversationModel.emptyQuery()){
			Long massMemberSize = preMassGetMembers(wechatId, massConversationModel);
			if (null==massMemberSize ||massMemberSize.longValue()== 0) {
				if(massConversationModel.getIsForce()!=null && massConversationModel.getIsForce()){
					throw new WechatException(Message.MEMBER_SELECTED_ALL_OFFLINE);
				}
				throw new WechatException(Message.MEMBER_NOT_BLANK);
			}
		}

		MsgType msgType = null;
		Date current = new Date();
		Material material = null;
		List<ConversationImageTextDetail> conversationImageTextDetails = new ArrayList<ConversationImageTextDetail>();
		if (massConversationModel.getMaterialId() != null) {
			material = materialService.getMaterial(wechatId,
					massConversationModel.getMaterialId());
			notBlank(material, Message.MATERIAL_NOT_EXIST);
			if (material.getMaterialType() == MaterialType.IMAGE_TEXT
					.getValue()) {
				msgType = MsgType.MPNEWS;
				//非强推则检查图文是否有效
				if(massConversationModel.getIsForce()!=null && massConversationModel.getIsForce()){

				}else{
					materialService.checkMaterialInvalidInWeiXin(wechatId,
							material.getMediaId());
				}

			} else if (material.getMaterialType() == MaterialType.IMAGE
					.getValue()) {
				msgType = MsgType.IMAGE;
				//非强推则检查图文是否有效
				if(massConversationModel.getIsForce()!=null && massConversationModel.getIsForce()){

				}else{
					materialService.checkMaterialInvalidInWeiXin(wechatId,
							material.getMediaId());
				}
				massConversationModel.setContent(material.getPicUrl());
			} else if (material.getMaterialType() == MaterialType.TEXT
					.getValue()) {
				MaterialTextDetail materialTextDetail = new MaterialTextDetail();
				materialTextDetail.setMaterialId(material.getId());
				materialTextDetail.setWechatId(material.getWechatId());
				materialTextDetail = materialTextDetailMapper
						.selectOne(materialTextDetail);
				notBlank(materialTextDetail,
						Message.MATERIAL_TEXT_DETAIL_NOT_EXIST);

				msgType = MsgType.TEXT;
				massConversationModel.setContent(materialTextDetail
						.getContent());
			} else {
				throw new WechatException(
						Message.CONVERSATION_CONTENT_NOT_SUPPORT);
			}
		} else {
			MaterialTextDetail materialTextDetail = new MaterialTextDetail();
			materialTextDetail.setContent(massConversationModel.getContent());
			materialTextDetail.setWechatId(wechatId);
			List<MaterialTextDetail> materialTextDetails = materialTextDetailMapper
					.select(materialTextDetail);
			if (!materialTextDetails.isEmpty()) {
				materialTextDetail = materialTextDetails.get(0);
			}
			if (materialTextDetail == null
					|| materialTextDetail.getMaterialId() == null) {
				material = new Material();
				material.setCreatedAt(current);
				if (user != null) {
					material.setCreatorId(user.getId());
				}
				material.setMaterialType(MaterialType.TEXT.getValue());
				material.setStatus(MaterialStatus.INUSED.getValue());
				material.setWechatId(wechatId);
				materialService.save(material);

				materialTextDetail = new MaterialTextDetail();
				materialTextDetail.setContent(massConversationModel
						.getContent());
				materialTextDetail.setMaterialId(material.getId());
				materialTextDetail.setWechatId(wechatId);
				materialTextDetailMapper.insert(materialTextDetail);
			} else {
				material = materialService.getMaterial(wechatId,
						materialTextDetail.getMaterialId());
			}
			msgType = MsgType.TEXT;
		}

		Conversation conversation = new Conversation();
		conversation.setContent(massConversationModel.getContent());
		conversation.setCreatedAt(current);
		conversation.setDirection(true);
		conversation.setWechatId(wechatId);
		conversation.setUserId(user.getId());
		conversation.setKfPhoto(user.getLocalHeadImgUrl());
		conversation.setMsgType(msgType.getValue());
		conversation.setStatus(ConversationStatus.UNREAD.getValue());
		conversation.setIsMass(true);
		conversationMapper.insert(conversation);

		if (!conversationImageTextDetails.isEmpty()) {
			for (ConversationImageTextDetail citd : conversationImageTextDetails) {
				citd.setConversationId(conversation.getId());
			}
			conversationImageTextDetailMapper
					.insertList(conversationImageTextDetails);
		}

		MassConversationResult massConversationResult = new MassConversationResult();
		massConversationResult.setConditions(JSONObject
				.toJSONString(massConversationModel));
		massConversationResult.setConversationId(conversation.getId());
		massConversationResult.setCreatedAt(current);
		massConversationResult.setCreatorId(user.getId());
		massConversationResult.setWechatId(wechatId);
		massConversationResult.setMaterialId(material.getId());
		massConversationResult
				.setStatus(MassConversationResultStatus.WAIT_AUDIT.getValue());
		massConversationResult.setMsgType(msgType.getValue());
		massConversationResultMapper.insert(massConversationResult);
	}

	@Override
	public void auditMassConversation(Integer wechatId, User user,
									  MassConversationModel massConversationModel) {
		MassConversationResultStatus status = MassConversationResultStatus
				.getByName(massConversationModel.getStatus());
		IllegalArgumentUtil.notBlank(status,
				Message.CONVERSATION_STATUS_INVALID);
		if (status != MassConversationResultStatus.AUDIT_NOT_PASS
				&& status != MassConversationResultStatus.AUDIT_PASS) {
			throw new WechatException(Message.CONVERSATION_STATUS_INVALID);
		}
		MassConversationResult massConversationResult = new MassConversationResult();
		massConversationResult.setWechatId(wechatId);
		massConversationResult.setId(massConversationModel.getId());
		massConversationResult = massConversationResultMapper
				.selectOne(massConversationResult);
		massConversationResult.setStatus(status.getValue());
		IllegalArgumentUtil.notBlank(massConversationResult,
				Message.CONVERSATION_MASS_NOT_EXIST);
		massConversationResult.setStatus(status.getValue());
		massConversationResult
				.setAuditReason(massConversationModel.getReason());
		massConversationResult.setAuditAt(new Date());
		massConversationResult.setAuditBy(user.getId());
		massConversationResultMapper.updateByPrimaryKey(massConversationResult);
	}

	/**
	 * 群发消息功能,支持立即发送,以及定时发送
	 * @param wechatId
	 * @param user
	 * @param massConversationModel
	 */
	@Override
	public void sendMassConversation(Integer wechatId, User user,
									 MassConversationModel massConversationModel) {
		if (massConversationModel == null) {
			massConversationModel = new MassConversationModel();
		}
		Date current = new Date();
		Date runAt = null;
		try {
			runAt = DateUtil.parse(massConversationModel.getRunAt());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (runAt != null && runAt.compareTo(new Date()) <= 0) {
			throw new WechatException(
					Message.CONVERSATION_MASS_RUN_AT_MUST_BE_GE_NOW);
		}
		MassConversationResult massConversationResult = new MassConversationResult();
		massConversationResult.setWechatId(wechatId);
		massConversationResult.setId(massConversationModel.getId());
		massConversationResult.setStatus(MassConversationResultStatus
				.getByName(massConversationModel.getStatus()).getValue());
		massConversationResult = massConversationResultMapper
				.selectOne(massConversationResult);
		IllegalArgumentUtil.notBlank(massConversationResult,
				Message.CONVERSATION_MASS_NOT_EXIST);
		if (massConversationResult.getMaterialId() == null) {
			throw new WechatException(Message.CONVERSATION_CONTENT_NOT_BLANK);
		}
		MassConversationModel condition = JSONObject.parseObject(
				massConversationResult.getConditions(),
				MassConversationModel.class);

		Material material = materialService.getMaterial(wechatId,
				massConversationResult.getMaterialId());
		notBlank(material, Message.MATERIAL_NOT_EXIST);
		Byte materialType = material.getMaterialType();
		if (materialType != MaterialType.IMAGE_TEXT.getValue()
				&& materialType != MaterialType.IMAGE.getValue()
				&& materialType != MaterialType.TEXT.getValue()) {
			throw new WechatException(Message.CONVERSATION_CONTENT_NOT_SUPPORT);
		}
		if (materialType != MaterialType.TEXT.getValue()) {
			if (StringUtils.isBlank(material.getMediaId())) {
				throw new WechatException(Message.MATERIAL_NOT_PUSH_TO_WX);
			}
			materialService.checkMaterialInvalidInWeiXin(wechatId,
					material.getMediaId());
		}
		if (runAt == null) {// send redirect
			WxMassMessage wxMassMessage = new WxMassMessage();
			MsgType msgType = null;
			List<ConversationImageTextDetail> conversationImageTextDetails = new ArrayList<ConversationImageTextDetail>();
			ConversationImageTextDetail conversationImageTextDetail = null;
			if (materialType == MaterialType.IMAGE_TEXT.getValue()) {
				MaterialDto materialDto = materialService.getImageText(
						wechatId, massConversationResult.getMaterialId());
				List<ImageTextDto> items = materialDto.getItems();
				JSONArray itemArray = new JSONArray();
				JSONObject itemJson = null;
				for (ImageTextDto imageTextDto : items) {
					conversationImageTextDetail = new ConversationImageTextDetail();
					conversationImageTextDetail.setAuthor(imageTextDto
							.getAuthor());
					conversationImageTextDetail.setContent(imageTextDto
							.getContent());
					conversationImageTextDetail
							.setContentSourceChecked(imageTextDto
									.getContentSourceChecked());
					conversationImageTextDetail
							.setContentSourceUrl(imageTextDto
									.getContentSourceUrl());
					conversationImageTextDetail.setShowCover(imageTextDto
							.isShowCover());
					conversationImageTextDetail.setSummary(imageTextDto
							.getSummary());
					conversationImageTextDetail.setTitle(imageTextDto
							.getTitle());
					conversationImageTextDetail
							.setMaterialCoverUrl(imageTextDto
									.getMaterialCoverUrl());
					conversationImageTextDetails
							.add(conversationImageTextDetail);
					itemJson = new JSONObject();
					itemJson.put("id", imageTextDto.getId());
					itemJson.put("materialId", materialDto.getId());
					itemJson.put("title", imageTextDto.getTitle());
					itemJson.put("summary", imageTextDto.getSummary());
					itemJson.put("materialCoverUrl",
							imageTextDto.getMaterialCoverUrl());
					itemArray.add(itemJson);
				}
				massConversationModel.setContent(itemArray.toJSONString());
				wxMassMessage.setMedia_id(material.getMediaId());
				wxMassMessage.setMsgtype("mpnews");
				msgType = MsgType.MPNEWS;
			} else if (materialType == MaterialType.IMAGE.getValue()) {
				msgType = MsgType.IMAGE;
				massConversationModel.setContent(material.getPicUrl());
				wxMassMessage.setMedia_id(material.getMediaId());
				wxMassMessage.setMsgtype("image");
			} else if (materialType == MaterialType.TEXT.getValue()) {
				MaterialTextDetail materialTextDetail = new MaterialTextDetail();
				materialTextDetail.setMaterialId(material.getId());
				materialTextDetail.setWechatId(material.getWechatId());
				materialTextDetail = materialTextDetailMapper
						.selectOne(materialTextDetail);
				notBlank(materialTextDetail,
						Message.MATERIAL_TEXT_DETAIL_NOT_EXIST);

				wxMassMessage.setText(materialTextDetail.getContent());
				wxMassMessage.setMsgtype("text");
				msgType = MsgType.TEXT;
				massConversationModel.setContent(materialTextDetail
						.getContent());
			} else {
				throw new WechatException(
						Message.CONVERSATION_CONTENT_NOT_SUPPORT);
			}

			Conversation conversation = get(wechatId,
					massConversationResult.getConversationId());
			conversation.setContent(massConversationModel.getContent());
			if (user != null) {
				conversation.setUserId(user.getId());
				conversation.setKfPhoto(user.getLocalHeadImgUrl());
			}
			conversation.setPicUrl(material.getPicUrl());
			conversationMapper.updateByPrimaryKey(conversation);

			if (!conversationImageTextDetails.isEmpty()) {
				for (ConversationImageTextDetail citd : conversationImageTextDetails) {
					citd.setConversationId(conversation.getId());
				}
				conversationImageTextDetailMapper
						.insertList(conversationImageTextDetails);
			}

			//push message by KeFu interface
			if(condition.getIsForce() != null && condition.getIsForce()){
				log.info("start mass conversation with force {}!",massConversationResult.getId());
				pushMessage(wechatId, massConversationResult, msgType, wxMassMessage, current, condition, material);
				return;
			}

			//send to all by WX
			if(condition.emptyQuery()){
				log.info("start mass conversation with is_to_all {}!",massConversationResult.getId());
				sendToAllByWx(wechatId, massConversationResult, msgType, wxMassMessage, current, condition, material);
				return;
			}
			log.info("start mass conversation with send by wx {}!",massConversationResult.getId());
			asynSendMasMessage(wechatId, massConversationResult, msgType, wxMassMessage, current, condition, user);

		} else {
			massConversationResult
					.setStatus(MassConversationResultStatus.WAIT_SEND
							.getValue());
			massConversationResult.setRunAt(runAt);
			massConversationResultMapper
					.updateByPrimaryKey(massConversationResult);
			TaskCategory taskCategory = new TaskCategory();
			taskCategory.setTaskClass(MassConversationJob.class.getName());
			taskCategory = taskCategoryMapper.selectOne(taskCategory);
			IllegalArgumentUtil.notBlank(taskCategory,
					Message.TASK_CATEGORY_NOT_EXIST);
			List<TaskParam> taskParams = JSONObject.parseArray(
					taskCategory.getParams(), TaskParam.class);
			if (taskParams != null && !taskParams.isEmpty()) {
				for (TaskParam taskParam : taskParams) {
					if (StringUtils.equals(taskParam.getKey(), "massId")) {
						taskParam.setValue(massConversationModel.getId()
								.toString());
					} else if (StringUtils.equals(taskParam.getKey(),
							"wechatId")) {
						taskParam.setValue(wechatId.toString());
					}
				}
			}

			Task task = new Task();
			task.setCateId(taskCategory.getId());
			task.setCreatedAt(current);
			if (user != null) {
				task.setCreatorId(user.getId());
			}
			task.setCronExpression(DateUtil.cron.format(runAt));
			task.setParams(JSONObject.toJSONString(taskParams));
			task.setStatus(TaskStatus.STARTUP.getValue());
			task.setTaskName("MassConversation_"
					+ massConversationModel.getId());
			taskService.addTask(task);
			try {
				taskService.addJob(taskService.getTaskById(task.getId()));
			} catch (SchedulerException e) {
				log.error(e.getLocalizedMessage());
				throw new WechatException(
						Message.CONVERSATION_MASS_ADD_JOB_ERROR);
			}
		}
	}

	/**
	 * 给微信粉丝发送消息,发给所有用户,没有条件筛选,所以强制添加is_to_all参数
	 * @param wechatId
	 * @param massConversationResult
	 * @param msgType
	 * @param wxMassMessage
	 * @param current
	 * @param condition
	 * @param material
	 */
	//@Async("callerRunsExecutor")
	private void sendToAllByWx(Integer wechatId, MassConversationResult massConversationResult, MsgType msgType,WxMassMessage wxMassMessage, Date current, MassConversationModel condition, Material material) {
		String accesstoken = RefreshAccessTokenJob.getAccessTokenStr(wechatId);
		SendMessageResponse sendMessageResponse = null;
		if (msgType == MsgType.MPNEWS) {
			sendMessageResponse = JwSendMessageAPI.sendMessageToAllWithArticles(
					accesstoken, wxMassMessage.getMedia_id(), wxMassMessage.getMsgtype(), null);
		}else if(msgType == MsgType.IMAGE){
			sendMessageResponse = JwSendMessageAPI.sendMessageToAllWithArticles(
					accesstoken, wxMassMessage.getMedia_id(), wxMassMessage.getMsgtype(), null);
		}else if(msgType == MsgType.TEXT){
			sendMessageResponse = JwSendMessageAPI.sendMessageToAllWithArticles(
					accesstoken, null, wxMassMessage.getMsgtype(),
					condition.getContent());
		}
		log.info("sendMessageResponse : {}", sendMessageResponse);
		if (!StringUtils.equals(sendMessageResponse.getErrcode(), "0")) {
			throw new WechatException(Message.SYSTEM_ERROR);
		}
		massConversationResult.setSendAt(current);
		massConversationResult.setMsgId(sendMessageResponse.getMsg_id());
		massConversationResult
				.setStatus(MassConversationResultStatus.SENDING.getValue());
		massConversationResult.setMsgType(msgType.getValue());
		massConversationResultMapper
				.updateByPrimaryKey(massConversationResult);
	}

	/**
	 * 强推消息逻辑
	 * @param wechatId
	 * @param massConversationResult
	 * @param msgType
	 * @param wxMassMessage
	 * @param current
	 * @param condition
	 * @param material
	 */
	//@Async("callerRunsExecutor")
	private void pushMessage(Integer wechatId, MassConversationResult massConversationResult, MsgType msgType, WxMassMessage wxMassMessage, Date current, MassConversationModel condition, Material material) {
		List<MemberDto> members = null;

		if (condition.getMemberIds() == null
				|| condition.getMemberIds().length == 0) {
			MemberModel memberModel = condition.getMemberModel();
			members = memberMapper.search(wechatId,
					memberModel.getOpenId(), memberModel.getNickname(),
					memberModel.getSex(), memberModel.getCountry(),
					memberModel.getProvince(), memberModel.getCity(),
					memberModel.getSubscribe(), memberModel
							.getActivityStartAt(), memberModel
							.getActivityEndAt(), memberModel
							.getBatchSendOfMonthStartAt(), memberModel
							.getBatchSendOfMonthEndAt(), DateUtil
							.getDateBegin(DateUtil.parse(memberModel
									.getAttentionStartAt())), DateUtil
							.getDateEnd(DateUtil.parse(memberModel
									.getAttentionEndAt())), DateUtil
							.getDateBegin(DateUtil.parse(memberModel
									.getCancelSubscribeStartAt())),
					DateUtil.getDateEnd(DateUtil.parse(memberModel
							.getCancelSubscribeEndAt())), true,
					null, memberModel.getMobile(), memberModel.getMemberTags(),
					null, null, null);
		} else {
			members = memberMapper.selectByMemberId(
					condition.getMemberIds(), wechatId, true);
		}

		if (members.isEmpty()) {
			throw new WechatException(Message.MEMBER_NOT_BLANK);
		}

		log.info("start mass conversation by force!");
		MemberModel memberModel = condition
				.getMemberModel();
		long totalCount = 0;
		long filterCount = 0;
		int sendCount = 0;
		int errorCount = 0;
		if(condition.getMemberIds() != null && condition.getMemberIds().length > 0){
			totalCount = condition.getMemberIds().length;
			filterCount = members.size();
		}else{
			Long totalMemberCount = memberMapper.count(wechatId, memberModel.getOpenId(),
					memberModel.getNickname(), memberModel.getSex(), memberModel
							.getCountry(), memberModel.getProvince(), memberModel
							.getCity(), memberModel.getSubscribe(), memberModel
							.getActivityStartAt(), memberModel.getActivityEndAt(),
					memberModel.getBatchSendOfMonthStartAt(), memberModel
							.getBatchSendOfMonthEndAt(), DateUtil
							.getDateBegin(DateUtil.parse(memberModel
									.getAttentionStartAt())), DateUtil
							.getDateEnd(DateUtil.parse(memberModel
									.getAttentionEndAt())), DateUtil
							.getDateBegin(DateUtil.parse(memberModel
									.getCancelSubscribeStartAt())), DateUtil
							.getDateEnd(DateUtil.parse(memberModel
									.getCancelSubscribeEndAt())), true,
					null, memberModel.getMobile(),
					memberModel.getMemberTags(), null, null, null);
			if(null==totalMemberCount){
				totalCount=0;
			}else{
				totalCount=totalMemberCount.longValue();
			}
			filterCount = totalCount;
		}


		for(MemberDto dto : members){
			KfcustomSend kfcustomSend = new KfcustomSend();
			kfcustomSend.setAccess_token(RefreshAccessTokenJob.getAccessTokenStr(wechatId));
			kfcustomSend.setTouser(dto.getOpenId());
			if (msgType == MsgType.MPNEWS){
				MsgMpNews mpnews = new MsgMpNews();
				mpnews.setMedia_id(material.getMediaId());
				kfcustomSend.setMpnews(mpnews);
				kfcustomSend.setMsgtype(MsgType.MPNEWS.name().toLowerCase());
			}else if(msgType == MsgType.IMAGE){
				MsgImage image = new MsgImage();
				image.setMedia_id(material.getMediaId());
				kfcustomSend.setImage(image);
				kfcustomSend.setMsgtype(MsgType.IMAGE.name().toLowerCase());
			}else if(msgType == MsgType.TEXT){
				MsgText text = new MsgText();
				text.setContent(condition.getContent());
				kfcustomSend.setText(text);
				kfcustomSend.setMsgtype(MsgType.TEXT.name().toLowerCase());
			}
			String result = JwKfaccountAPI.sendKfMessage(kfcustomSend);
			if (result.equals("ok")){
				sendCount++;
				recordForceMsg(dto, msgType, condition, wechatId);
			}else{
				errorCount++;
			}
		}

		massConversationResult.setTotalCount((int)totalCount);
		massConversationResult.setFilterCount((int)filterCount);
		massConversationResult.setSendCount(sendCount);
		massConversationResult.setErrorCount(errorCount);
		massConversationResult.setSendAt(current);
		massConversationResult
				.setStatus(MassConversationResultStatus.SEND_SUCCESS.getValue());
		massConversationResult.setMsgType(msgType.getValue());
		massConversationResultMapper
				.updateByPrimaryKey(massConversationResult);
	}

	/**
	 * 异步根据条件群发消息
	 * @param wechatId
	 * @param massConversationResult
	 * @param msgType
	 * @param wxMassMessage
	 * @param current
	 * @param condition
	 * @param user
	 */
	//@Async("callerRunsExecutor")
	private void asynSendMasMessage(Integer wechatId, MassConversationResult massConversationResult, MsgType msgType, WxMassMessage wxMassMessage, Date current, MassConversationModel condition, User user) {
		log.info("start sendMassMessage massConversationResult : {}!",massConversationResult.getId());
		Conversation conversation = get(wechatId, massConversationResult.getConversationId());
		SendMessageResponse sendMessageResponse = null;
		int batchIndex = 1;
		if(null!=condition.getMemberIds()&&condition.getMemberIds().length>0){
			List<MemberDto> list = memberMapper.selectByMemberId(condition.getMemberIds(),wechatId,condition.getIsForce());
			List<MassConversation> massConversations = new ArrayList<MassConversation>();
			MassConversation massConversation = null;
			for (MemberDto memberDto : list) {
				massConversation = new MassConversation();
				massConversation.setConversationId(conversation.getId());
				massConversation.setCreatedAt(current);
				if (user != null) {
					massConversation.setCreatorId(user.getId());
				}
				massConversation.setMemberId(memberDto.getId());
				massConversation.setWechatId(wechatId);
				massConversation
						.setStatus(ConversationStatus.UNREAD.getValue());
				massConversations.add(massConversation);
			}
			//batch insert into DB
			insertConversationListToDB(massConversations);

			Wxuser[] wxusers = getWxUsers(list);
			log.info("wxusers size : {}", wxusers.length);
			String accesstoken = RefreshAccessTokenJob
					.getAccessTokenStr(wechatId);

			if (msgType == MsgType.IMAGE) {
				sendMessageResponse = JwSendMessageAPI
						.sendMessageToOpenidsWithMedia(accesstoken, wxusers,
								wxMassMessage);
			} else if (msgType == MsgType.MPNEWS) {
				sendMessageResponse = JwSendMessageAPI
						.sendMessageToOpenidsWithArticles(accesstoken, wxusers,
								wxMassMessage);
			} else if (msgType == MsgType.TEXT) {
				sendMessageResponse = JwSendMessageAPI
						.sendMessageToOpenidsWithText(accesstoken, wxusers,
								wxMassMessage);
			}
			log.info("sendMessageResponse : {}", sendMessageResponse);
			if (!StringUtils.equals(sendMessageResponse.getErrcode(), "0")) {
				throw new WechatException(Message.SYSTEM_ERROR);
			}
		}else{
			log.debug("start sendMassMessage by Query");
			String batchSizeStr = configService.getConfigValue(wechatId, "MASS_CONVERSATION", "BATCH_SIZE");

			int batchSize = 10000;//默认批大小为10000
			if(StringUtils.isNotBlank(batchSizeStr)){
				batchSize = Integer.parseInt(batchSizeStr);
			}

			TaskCategory taskCategory = new TaskCategory();
			taskCategory.setTaskClass(BatchMassConversationJob.class.getName());
			taskCategory = taskCategoryMapper.selectOne(taskCategory);
			IllegalArgumentUtil.notBlank(taskCategory, Message.TASK_CATEGORY_NOT_EXIST);

			for(; ; batchIndex++) {
				PageHelper.startPage(batchIndex, batchSize, false);
				MemberModel memberModel = condition.getMemberModel();
				Page<MemberDto> list =  memberMapper.massMembersSearch(wechatId, memberModel.getOpenId(),
						memberModel.getNickname(), memberModel.getSex(), memberModel
								.getCountry(), memberModel.getProvince(), memberModel
								.getCity(), memberModel.getSubscribe(), memberModel
								.getActivityStartAt(), memberModel.getActivityEndAt(),
						memberModel.getBatchSendOfMonthStartAt(), memberModel
								.getBatchSendOfMonthEndAt(), DateUtil
								.getDateBegin(DateUtil.parse(memberModel
										.getAttentionStartAt())), DateUtil
								.getDateEnd(DateUtil.parse(memberModel
										.getAttentionEndAt())), DateUtil
								.getDateBegin(DateUtil.parse(memberModel
										.getCancelSubscribeStartAt())), DateUtil
								.getDateEnd(DateUtil.parse(memberModel
										.getCancelSubscribeEndAt())), memberModel
								.getIsOnline(), null, memberModel.getMobile(),
						memberModel.getMemberTags(), condition.getSortName(),
						condition.getSortDir(), condition
								.getBindStatus());

				if(list== null || list.size() == 0) {
					break;
				}

				List<MassConversation> massConversations = new ArrayList<MassConversation>();
				MassConversation massConversation = null;
				for (MemberDto memberDto : list) {
					massConversation = new MassConversation();
					massConversation.setConversationId(conversation.getId());
					massConversation.setCreatedAt(current);
					if (user != null) {
						massConversation.setCreatorId(user.getId());
					}
					massConversation.setMemberId(memberDto.getId());
					massConversation.setWechatId(wechatId);
					massConversation.setStatus(ConversationStatus.UNREAD.getValue());
					massConversation.setPiCi(batchIndex);
					massConversations.add(massConversation);
				}
				//batch insert into DB
				insertConversationListToDB(massConversations);
				//批量新增
				massConversationBatchResultService.batchSendMassMsg(batchIndex,wechatId,list,conversation.getId(),msgType,wxMassMessage);
			}

			// 增加批量群发任务
			List<TaskParam> taskParams = com.alibaba.fastjson.JSONObject.parseArray(taskCategory.getParams(), TaskParam.class);
			if (taskParams != null && !taskParams.isEmpty()) {
				for (TaskParam taskParam : taskParams) {
					if (StringUtils.equals(taskParam.getKey(), "batchMassId")) {
						taskParam.setValue(conversation.getId().toString());
					}
					if (StringUtils.equals(taskParam.getKey(), "wechatId")) {
						taskParam.setValue(wechatId.toString());
					}
				}
			}

			Date runAt = DateUtils.addMinutes(new Date(),1);
			Task task = new Task();
			task.setCateId(taskCategory.getId());
			task.setCreatedAt(new Date());
			task.setCreatorId(user!=null?user.getId():null);
			task.setCronExpression(DateUtil.cron.format(runAt));
			task.setParams(com.alibaba.fastjson.JSONObject.toJSONString(taskParams));
			task.setStatus(TaskStatus.STARTUP.getValue());
			task.setTaskName("BatchMassConversation_" + conversation.getId());
			taskService.addTask(task);
			try {
				taskService.addJob(taskService.getTaskById(task.getId()));
			} catch (SchedulerException e) {
				log.error(e.getLocalizedMessage());
				throw new WechatException(Message.CONVERSATION_MASS_ADD_JOB_ERROR);
			}
		}

		massConversationResult.setSendAt(current);
		massConversationResult.setMsgId(sendMessageResponse!=null?sendMessageResponse.getMsg_id():null);
		massConversationResult.setStatus(MassConversationResultStatus.SENDING.getValue());
		massConversationResult.setMsgType(msgType.getValue());
		massConversationResult.setTotalBatch(batchIndex-1);
		massConversationResultMapper.updateByPrimaryKey(massConversationResult);
	}

	/**
	 * 异步插入用户任务会话
	 * @param massConversations
	 */
	//@Async("callerRunsExecutor")
	private void insertConversationListToDB(List<MassConversation> massConversations) {
		massConversationMapper.insertList(massConversations);
	}

	private void recordForceMsg(MemberDto dto, MsgType msgType, MassConversationModel massConversationModel, Integer wechatId) {
		Conversation record = new Conversation();
		Date current = new Date();
		record.setCreatedAt(current);
		record.setMsgType(msgType.getValue());
		record.setWechatId(wechatId);
		record.setStatus(ConversationStatus.UNREAD.getValue());
		record.setDirection(true);
		record.setMemberId(dto.getId());
		record.setMemberPhoto(dto.getLocalHeadImgUrl());
		record.setIsMass(false);
		record.setContent(massConversationModel.getContent());
		conversationMapper.insert(record);

	}

	private Wxuser[] getWxUsers(List<MemberDto> members) {
		Wxuser[] wxusers = new Wxuser[members.size()];
		Wxuser wxuser = null;
		for (int i = 0; i < members.size(); i++) {
			wxuser = new Wxuser();
			wxuser.setOpenid(members.get(i).getOpenId());
			wxusers[i] = wxuser;
		}
		return wxusers;
	}

	/**
	 * 用于检查预审核时符合条件的用户数量
	 * @param wechatId
	 * @param massConversationModel
	 * @return
	 */
	private Long preMassGetMembers(Integer wechatId,
								   MassConversationModel massConversationModel) {

		Long size =0l;
		//先判断是否是发送给选中的用户
		if(null!=massConversationModel.getMemberIds()&&massConversationModel.getMemberIds().length>0){
			if(massConversationModel.getIsForce()!=null && massConversationModel.getIsForce()){
				size = memberMapper.countByMemberId(
						massConversationModel.getMemberIds(), wechatId, true);
			}else{
				size = memberMapper.countByMemberId(
						massConversationModel.getMemberIds(), wechatId, null);

			}

		}else{
			//如果是强推,则强制设置在线状态为true的用户
			if(massConversationModel.getIsForce()!=null && massConversationModel.getIsForce()){
				massConversationModel.setIsOnline(true);
			}
			MemberModel memberModel = massConversationModel.getMemberModel();
			size =  memberMapper.count(wechatId, memberModel.getOpenId(),
					memberModel.getNickname(), memberModel.getSex(), memberModel
							.getCountry(), memberModel.getProvince(), memberModel
							.getCity(), memberModel.getSubscribe(), memberModel
							.getActivityStartAt(), memberModel.getActivityEndAt(),
					memberModel.getBatchSendOfMonthStartAt(), memberModel
							.getBatchSendOfMonthEndAt(), DateUtil
							.getDateBegin(DateUtil.parse(memberModel
									.getAttentionStartAt())), DateUtil
							.getDateEnd(DateUtil.parse(memberModel
									.getAttentionEndAt())), DateUtil
							.getDateBegin(DateUtil.parse(memberModel
									.getCancelSubscribeStartAt())), DateUtil
							.getDateEnd(DateUtil.parse(memberModel
									.getCancelSubscribeEndAt())), memberModel
							.getIsOnline(), null, memberModel.getMobile(),
					memberModel.getMemberTags(), massConversationModel.getSortName(),
					massConversationModel.getSortDir(), massConversationModel
							.getBindStatus());
		}

		return size;
	}


	private List<ReportMessageItemDto> findDates4Message(Date begin, Date end) {
		List<ReportMessageItemDto> list = new ArrayList<ReportMessageItemDto>();
		ReportMessageItemDto dto = new ReportMessageItemDto();
		dto.setDate(DateUtil.formatYYYYMMDD(begin));
		list.add(dto);

		Calendar calBegin = Calendar.getInstance();
		calBegin.setTime(begin);
		calBegin.set(Calendar.HOUR_OF_DAY, 0);
		calBegin.set(Calendar.MINUTE, 0);
		calBegin.set(Calendar.SECOND, 0);
		calBegin.set(Calendar.MILLISECOND, 0);
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(end);
		calEnd.set(Calendar.HOUR_OF_DAY, 0);
		calEnd.set(Calendar.MINUTE, 0);
		calEnd.set(Calendar.SECOND, 0);
		calEnd.set(Calendar.MILLISECOND, 0);
		while (calEnd.after(calBegin)) {
			calBegin.add(Calendar.DAY_OF_MONTH, 1);
			dto = new ReportMessageItemDto();
			dto.setDate(DateUtil.formatYYYYMMDD(calBegin.getTime()));
			list.add(dto);
		}
		return list;
	}

	@Override
	public ReportMessageDto messageReport(Integer wechatId, Date start, Date end) {
		// TODO Auto-generated method stub

		ReportMessageDto dto = conversationMapper.messageCount(wechatId, start,
				end);
		List<ReportMessageItemDto> allList = findDates4Message(start, end);
		List<ReportMessageItemDto> list = conversationMapper.messageReport(
				wechatId, start, end);
		for (ReportMessageItemDto ddto : allList) {
			for (ReportMessageItemDto temp : list) {
				if (ddto.getDate().equals(temp.getDate())) {
					ddto.setImage(temp.getImage());
					ddto.setImagetext(temp.getImagetext());
					ddto.setText(temp.getText());
					ddto.setVideo(temp.getVideo());
					ddto.setVoice(temp.getVoice());
					ddto.setSendTimes(temp.getSendTimes());
					ddto.setClick(temp.getClick());
					ddto.setScan(temp.getScan());
					ddto.setLink(temp.getLink());
					ddto.setLocation(temp.getLocation());
					ddto.setShortvideo(temp.getShortvideo());
					ddto.setMusic(temp.getMusic());
					break;
				}
			}
		}
		if (allList != null && allList.size() > 0) {
			dto.setList(allList);
		}
		if (dto.getReceiver() > 0) {
			dto.setPerCapita((double) dto.getSendTimes() / dto.getReceiver());
		}
		return dto;
	}

	@Override
	public ReportMessageDto hourMessageReport(Integer wechatId, Date startDate,
											  Date endDate) {
		ReportMessageDto dto = conversationMapper.messageCount(wechatId,
				startDate, endDate);
		if (dto.getReceiver() > 0) {
			dto.setPerCapita((double) dto.getSendTimes() / dto.getReceiver());
		}
		List<ReportMessageItemDto> listDtos = conversationMapper
				.messageItemReport(wechatId, startDate, endDate);
		List<ReportMessageItemDto> list = new ArrayList<ReportMessageItemDto>();
		outter: for (int i = 1; i < 25; i++) {
			String date = i + ":00";
			for (ReportMessageItemDto rmid : listDtos) {
				if (rmid.getDate().equals(date)) {
					list.add(rmid);
					continue outter;
				}
			}

			ReportMessageItemDto record = getEmptyReportMessageItemDto(date);
			list.add(record);
		}
		dto.setList(list);
		return dto;
	}

	public ReportMessageItemDto getEmptyReportMessageItemDto(String date) {
		ReportMessageItemDto record = new ReportMessageItemDto();
		record.setDate(date);
		record.setSendTimes(0);
		record.setImage(0);
		record.setImagetext(0);
		record.setScan(0);
		record.setClick(0);
		record.setLink(0);
		record.setLocation(0);
		record.setVoice(0);
		record.setVideo(0);
		record.setText(0);
		record.setShortvideo(0);
		record.setMusic(0);
		return record;
	}
}