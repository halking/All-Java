package com.d1m.wechat.service.impl;

import com.d1m.wechat.dto.UserProfileDto;
import com.d1m.wechat.dto.WechatAccessTokenDto;
import com.d1m.wechat.exception.CommonException;
import com.d1m.wechat.mapper.ConversationMapper;
import com.d1m.wechat.mapper.MemberConversationSessionMapper;
import com.d1m.wechat.model.Conversation;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.model.MemberConversationSession;
import com.d1m.wechat.model.enums.Language;
import com.d1m.wechat.service.AreaInfoService;
import com.d1m.wechat.service.ConfigService;
import com.d1m.wechat.service.LiveChatService;
import com.d1m.wechat.util.Security;
import com.d1m.wechat.wxsdk.core.thread.AccessToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Consts;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.ibatis.javassist.NotFoundException;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Created by Lisa on 2017/11/8.
 */
@Service
@Slf4j
public class LiveChatServiceImpl implements LiveChatService{

    @Autowired
    private ConfigService configService;

    @Autowired
    private AreaInfoService areaInfoService;

    @Resource
    private MemberConversationSessionMapper memberConversationSessionMapper;

    @Resource
    private ConversationMapper conversationMapper;

    @Override
    public UserProfileDto getUserProfileDto(Member member){
        UserProfileDto userProfileDto = new UserProfileDto();
        BeanUtils.copyProperties(member, userProfileDto);

        userProfileDto.setLanguage(Language.getByValue(member.getLanguage()).toString());
        if (member.getCountry() != null){
            String country = areaInfoService.selectNameById(member.getCountry(), null);
            userProfileDto.setCountry(country);
        }
        if (member.getProvince() != null){
            String province = areaInfoService.selectNameById(member.getProvince(), null);
            userProfileDto.setProvince(province);
        }
        if (member.getCity() != null){
            String city = areaInfoService.selectNameById(member.getCity(), null);
            userProfileDto.setCity(city);
        }
        userProfileDto.setSubscribeTime((int)(member.getSubscribeAt().getTime()/1000));
        userProfileDto.setSubscribe(member.getIsSubscribe()?1:0);

        return userProfileDto;
    }

    @Override
    public boolean sendMsgToLiveChat(String xml, Integer wechatId){
        try {
            String url = configService.getConfigValue(wechatId, "ARVATO", "MESSAGE_URL");
            log.info("Post to arvato start: url={} body={} ", url, xml);
            String response = Request.Post(url)
                    .bodyString(xml, ContentType.APPLICATION_XML.withCharset(Consts.UTF_8))
                    .execute().returnContent().asString();
            log.info("Post to arvato end: response={}", response);
            return true;
        } catch (IOException e){
            log.error("转发消息失败", e);
            return false;
        }
    }

    @Override
    public boolean clickKefuMenuAndStartSession(String toUserName, String fromUserName, Integer wechatId, Date startAt){
        try {
            boolean isSuccess = sendMsgToLiveChat(buildTextXml(toUserName, fromUserName), wechatId);
            startSession(fromUserName, wechatId, startAt);
            return true;
        }catch (Exception e){
            log.error("客户点击菜单开启session失败",e);
            return false;
        }
    }

    private String buildTextXml(String toUserName, String fromUserName) {
        return String.format(
                "<xml>\n" +
                        "<ToUserName><![CDATA[%s]]></ToUserName>\n" +
                        "<FromUserName><![CDATA[%s]]></FromUserName>\n" +
                        "<CreateTime>%d</CreateTime>\n" +
                        "<MsgType><![CDATA[text]]></MsgType>\n" +
                        "<Content><![CDATA[%s]]></Content>\n" +
                        "</xml>",
                toUserName, fromUserName, System.currentTimeMillis() / 1000, "客户点击客服菜单"
        );
    }

    @Override
    public WechatAccessTokenDto getWechatAccessToken(Integer wechatId, String secret) {
        WechatAccessTokenDto tokenDto = new WechatAccessTokenDto();
        AccessToken accessToken = RefreshAccessTokenJob.getLatestAccessToken(wechatId);
        if(accessToken != null){
            String secretStr = Security.encryptMsgWithAES(accessToken.getAccessToken(), secret);
            DateTime dt = new DateTime(accessToken.getLastRefreshTimeMillis());
            tokenDto.setAccessToken(secretStr);
            log.info("===" + dt.plusSeconds((int)accessToken.getExpired()));
            tokenDto.setExpireTime(dt.plusSeconds((int)accessToken.getExpired()).toString("yyyy-MM-dd HH:mm:ss"));
        }
        return tokenDto;
    }

    @Override
    public void closeSessionByMemberOpenId(String memberOpenId, Integer wechatId) throws NotFoundException {
        List<MemberConversationSession> sessionList = getMemberConversationSessionList(memberOpenId, wechatId);
        if(sessionList == null || sessionList.size() == 0){
            throw new NotFoundException("Session not found with user member openId:" + memberOpenId);
        }else{
            for(MemberConversationSession session : sessionList) {
                session.setEndAt(new Date());
                session.setIsClosed(true);
                memberConversationSessionMapper.updateByPrimaryKey(session);
                log.info(memberOpenId + " Session created at:" + session.getEndAt());
            }
        }
    }

    public List<MemberConversationSession> getMemberConversationSessionList(String memberOpenId, Integer wechatId){
        MemberConversationSession query = new MemberConversationSession();
        query.setMemberOpenId(memberOpenId);
        query.setWechatId(wechatId);
        query.setIsClosed(false);
        return memberConversationSessionMapper.select(query);
    }

    @Override
    public MemberConversationSession getMemberConversationSession(String memberOpenId, Integer wechatId){
        List<MemberConversationSession> sessionList = getMemberConversationSessionList(memberOpenId, wechatId);
        if(sessionList != null && sessionList.size() > 0) {
            return sessionList.get(0);
        }
        return null;
    }

    private String buildHistoryXml(String toUserName, String fromUserName, List<Conversation> conversationsList) {
        StringBuilder messagesStrBuilder = new StringBuilder();
        for(Conversation conversation : conversationsList){
            messagesStrBuilder = messagesStrBuilder.append(buildHistorySingleMsgXml(conversation));
        }

        return String.format(
                "<xml>\n" +
                        "<ToUserName><![CDATA[%s]]></ToUserName>\n" +
                        "<FromUserName><![CDATA[%s]]></FromUserName>\n" +
                        "<messages>%s</messages>\n" +
                        "</xml>",
                toUserName, fromUserName, messagesStrBuilder.toString()
        );
    }

    private String buildHistorySingleMsgXml(Conversation conversation) {
        return String.format(
                "<message>\n" +
                        "<MsgId><![CDATA[%s]]></MsgId>\n" +
                        "<CreateTime>%d</CreateTime>\n" +
                        "<Content><![CDATA[%s]]></Content>\n" +
                        "</message>",
                conversation.getMsgId(),
                (int) (conversation.getCreatedAt().getTime() / 1000),
                conversation.getContent()
        );
    }

    @Override
    public void sendOldMsg(String fromUserName, String toUserName, Integer wechatId, Date endAt) {
        try {
            Conversation conversationQuery = new Conversation();
            conversationQuery.setOpenId(fromUserName);
            String messageTimePeriod = configService.getConfigValue(wechatId, "ARVATO", "MESSAGE_PERIOD_MINS");
            //get last conversation
            Date startAt = new DateTime().minusMinutes(Integer.parseInt(messageTimePeriod)).toDate();

            MemberConversationSession lastSession = memberConversationSessionMapper.getLastSessionByOpenId(fromUserName, wechatId);
            if(lastSession != null){
                if(startAt.getTime() < lastSession.getEndAt().getTime()){
                    startAt = lastSession.getEndAt();
                }
            }
            log.info("startAt" + startAt);
            log.info("endAt" + endAt);
            List<Conversation> conversationsList = conversationMapper.getHistoryMessageList(wechatId, fromUserName, startAt, endAt);
            if(conversationsList!=null && conversationsList.size() > 0) {
                String xml = buildHistoryXml(toUserName, fromUserName, conversationsList);
                String url = configService.getConfigValue(wechatId, "ARVATO", "OLD_MESSAGE_URL");
                log.info("Post to oldmsg arvato start: url={} body={} ", url, xml);
                String response = Request.Post(url)
                        .bodyString(xml, ContentType.APPLICATION_XML.withCharset(Consts.UTF_8))
                        .execute().returnContent().asString();
                log.info("Post to arvato end: response={}", response);
            }
        }catch (Exception e){
            log.error("Failed to send history message:" + e.getMessage());
        }
    }

    /**
     * Start and create conversation session
     *
     * @param openId the member open id
     * @param wechatId
     * @throws CommonException the technical exception
     */
    @Override
    public void startSession(String openId, Integer wechatId, Date startAt){
        if(getMemberConversationSession(openId, wechatId) == null){
            log.info(openId + " Session created at:" + startAt);
            MemberConversationSession memberConversationSession = new MemberConversationSession();
            memberConversationSession.setMemberOpenId(openId);
            memberConversationSession.setWechatId(wechatId);
            memberConversationSession.setStartAt(startAt);
            memberConversationSession.setType("LIVE_CHAT_SESSION");
            memberConversationSession.setIsClosed(false);
            memberConversationSessionMapper.insertSelective(memberConversationSession);
        }
    }

}

