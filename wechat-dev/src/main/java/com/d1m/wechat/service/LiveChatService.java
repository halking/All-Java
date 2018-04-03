package com.d1m.wechat.service;

import com.d1m.wechat.dto.UserProfileDto;
import com.d1m.wechat.dto.WechatAccessTokenDto;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.model.MemberConversationSession;
import org.apache.ibatis.javassist.NotFoundException;

import java.util.Date;


/**
 * Created by Lisa on 2017/11/8.
 */
public interface LiveChatService {

    UserProfileDto getUserProfileDto(Member member);

    boolean sendMsgToLiveChat(String xml, Integer wechatId);

    WechatAccessTokenDto getWechatAccessToken(Integer wechatId, String secret);

    void closeSessionByMemberOpenId(String memberOpenId, Integer wechatId) throws NotFoundException;

    MemberConversationSession getMemberConversationSession(String memberOpenId, Integer wechatId);

    void sendOldMsg(String fromUserName, String toUserName, Integer wechatId, Date endAt);

    void startSession(String memberOpenId, Integer wechatId, Date startAt);

    boolean clickKefuMenuAndStartSession(String toUserName, String fromUserName, Integer wechatId, Date startAt);
}
