package com.d1m.wechat.mapper;

import com.d1m.wechat.model.MemberConversationSession;
import com.d1m.wechat.util.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface MemberConversationSessionMapper extends MyMapper<MemberConversationSession> {

    MemberConversationSession getLastSessionByOpenId(@Param("fromUserName")String fromUserName, @Param("wechatId")Integer wechatId);
}