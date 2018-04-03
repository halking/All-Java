package com.d1m.wechat.service.impl;

import com.d1m.wechat.mapper.MemberConversationSessionMapper;
import com.d1m.wechat.model.MemberConversationSession;
import com.d1m.wechat.service.MemberConversationSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

/**
 * Created by Lisa on 2017/11/14.
 */
@Service
public class MemberConversationSessionServiceImpl extends BaseService<MemberConversationSession> implements
        MemberConversationSessionService {

    @Autowired
    MemberConversationSessionMapper memberConversationSessionMapper;

    @Override
    public Mapper<MemberConversationSession> getGenericMapper() {
        return memberConversationSessionMapper;
    }
}
