package com.d1m.wechat.service.impl;

import com.d1m.wechat.mapper.CampaignMemberTagMapper;
import com.d1m.wechat.mapper.WxMemberTagMapper;
import com.d1m.wechat.model.CampaignMemberTag;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.model.WxMemberTag;
import com.d1m.wechat.service.MemberService;
import com.d1m.wechat.service.WxMemberTagService;
import com.d1m.wechat.wxsdk.core.req.model.user.Tag;
import com.d1m.wechat.wxsdk.user.tag.JwTagApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lisa on 2018/1/23.
 */
@Service
@Slf4j
public class WxMemberTagServiceImpl extends BaseService<WxMemberTag> implements
        WxMemberTagService {

    @Autowired
    private WxMemberTagMapper wxMemberTagMapper;

    @Autowired
    private CampaignMemberTagMapper campaignMemberTagMapper;

    @Autowired
    private MemberService memberService;

    @Override
    public Mapper<WxMemberTag> getGenericMapper() {
        return wxMemberTagMapper;
    }

    @Override
    public synchronized void syncMemberTagFromWechat(Integer wechatId, Integer tagId) {
        try {
            String accessToken = RefreshAccessTokenJob.getAccessTokenStr(wechatId);
            if (StringUtils.isBlank(accessToken)) {
                return;
            }
            //get tag list from wechat
            Date syncDate = new Date();
            List<Tag> tagList = JwTagApi.getAllTag(accessToken);

            //delete
            wxMemberTagMapper.deleteBySyncDate(wechatId, syncDate);

            //delete CampaignMemberTag
            campaignMemberTagMapper.bunchDelete(getTagIdList(tagList), wechatId);

            for (Tag tag : tagList) {
                WxMemberTag wxMemberTag = new WxMemberTag();
                wxMemberTag.setCreatorAt(syncDate);
                wxMemberTag.setWxId(Integer.parseInt(tag.getId()));
                wxMemberTag.setName(tag.getName());
                wxMemberTag.setWechatId(wechatId);
                wxMemberTagMapper.insert(wxMemberTag);

                //get follower list by tag
                List<String> openIdList = JwTagApi.getUserByTag(accessToken, tag.getId(), "");
                if(openIdList == null){
                    continue;
                }
                List<CampaignMemberTag> campaignMemberTags = new ArrayList<>();
                for(String openId : openIdList) {
                    CampaignMemberTag campaignMemberTag = new CampaignMemberTag();
                    Member member = memberService.getMemberByOpenId(wechatId, openId);
                    if (member == null) {
                        continue;
                    }
                    campaignMemberTag.setMemberId(member.getId());
                    campaignMemberTag.setWxTagId(Integer.parseInt(tag.getId()));
                    campaignMemberTag.setCreateDate(syncDate);
                    campaignMemberTags.add(campaignMemberTag);
                }
                campaignMemberTagMapper.insertList(campaignMemberTags);
            }
        }catch (Exception e){
            log.error("Sync member tag from WX failed:" + e.getMessage());
        }
    }

    private List<String> getTagIdList(List<Tag> tagList){
        List<String> tagIdList = new ArrayList<>();
        for (Tag tag : tagList) {
            tagIdList.add(tag.getId());
        }
        return tagIdList;
    }
}
