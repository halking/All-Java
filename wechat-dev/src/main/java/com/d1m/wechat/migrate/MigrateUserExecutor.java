package com.d1m.wechat.migrate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.d1m.wechat.mapper.MemberMapper;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.service.MemberService;
import com.d1m.wechat.service.impl.MemberServiceImpl;
import com.d1m.wechat.wxsdk.user.user.JwUserAPI;
import com.d1m.wechat.wxsdk.user.user.model.Wxuser;

/**
 * 类描述
 *
 * @author f0rb on 2016-12-08.
 */
@Slf4j
@Component
public class MigrateUserExecutor {
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private MemberService memberService;

    @Async("callerRunsExecutor")
    public Future<String> execute(List<String> openIdList, Integer wechatId) {

        List<Wxuser> wxuserList = JwUserAPI.batchGetWxUser(openIdList, wechatId);

        log.info("wechatId[{}] start pull wx member", wechatId);

        List<Member> members = new ArrayList<>();
        Date current = new Date();
        for (Wxuser wxuser : wxuserList) {
            // 数据转换
            Member newMember = memberService.getMemberByWxUser(wxuser, wechatId, current);
            Member exist = memberService.getMemberByOpenId(wechatId, wxuser.getOpenid());
            if (exist != null) {
                log.info("member exist {}.", wxuser.getOpenid());
                MemberServiceImpl.updateMemberByWxMember(exist, newMember);
                log.info("nickname : {}", exist.getNickname());
                memberService.updateAll(exist);
            } else {
                log.info("member add {}.", wxuser.getOpenid());
                members.add(newMember);
            }
        }

        // 插入数据库
        if (!members.isEmpty()) {
            memberMapper.insertList(members);
        }
        String result = Thread.currentThread().getName() + " 成功处理了" + members.size() + "条用户信息";
        return new AsyncResult<>(result);
    }
}
