package com.d1m.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.common.Constant;
import com.d1m.wechat.common.ExceptionCode;
import com.d1m.wechat.dto.ZegnaBindDto;
import com.d1m.wechat.exception.CommonException;
import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.mapper.CampaignMemberTagMapper;
import com.d1m.wechat.mapper.MemberMapper;
import com.d1m.wechat.mapper.MemberProfileMapper;
import com.d1m.wechat.mapper.ZegnaCampaignMapper;
import com.d1m.wechat.mapper.ZegnaHistoryMemberMapper;
import com.d1m.wechat.model.CampaignMemberTag;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.model.MemberProfile;
import com.d1m.wechat.model.ZegnaCampaign;
import com.d1m.wechat.model.ZegnaHistoryMember;
import com.d1m.wechat.model.enums.MsgType;
import com.d1m.wechat.pamametermodel.ZegnaModel;
import com.d1m.wechat.service.ConfigService;
import com.d1m.wechat.service.VerifyTokenService;
import com.d1m.wechat.service.ZegnaBindService;
import com.d1m.wechat.util.DateUtil;
import com.d1m.wechat.util.Message;
import com.d1m.wechat.wxsdk.core.req.model.kfaccount.KfcustomSend;
import com.d1m.wechat.wxsdk.core.req.model.kfaccount.MsgText;
import com.d1m.wechat.wxsdk.core.sendmsg.JwKfaccountAPI;
import com.d1m.wechat.wxsdk.tag.JwTagAPI;
import com.d1m.wechat.wxsdk.tag.WxTags;
import com.d1m.wechat.wxsdk.user.tag.JwTagApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ZegnaBindServiceImpl implements ZegnaBindService {

    public static final String ADD_TAG = "CNY";

    @Autowired
    private MemberProfileMapper memberProfileMapper;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private ConfigService configService;

    @Autowired
    private VerifyTokenService verifyTokenService;

    @Autowired
    private ZegnaCampaignMapper zegnaCampaignMapper;

    @Autowired
    private CampaignMemberTagMapper campaignMemberTagMapper;

    @Autowired
    private ZegnaHistoryMemberMapper zegnaHistoryMemberMapper;

    @Override
    public ZegnaBindDto getProfile(ZegnaModel model, Member member) {
        MemberProfile memberProfile = memberProfileMapper.getByMemberId(model.getWechatId(), model.getMemberId());
        ZegnaBindDto zegnaBindDto = createZegnaBindDto(memberProfile, member);
        zegnaBindDto = getConfigStatus(zegnaBindDto, model.getWechatId());
        return zegnaBindDto;
    }

    @Override
    public ZegnaBindDto saveProfile(ZegnaModel model) {
        ZegnaBindDto zegnaBindDto = new ZegnaBindDto();
        zegnaBindDto = getConfigStatus(zegnaBindDto, model.getWechatId());
        if (StringUtils.isBlank(model.getCode())) {
            //编辑保存memberProfile
            MemberProfile memberProfile = memberProfileMapper.getByMemberId(model.getWechatId(), model.getMemberId());
            if (memberProfile == null) {
                memberProfile = new MemberProfile();
                memberProfile.setMemberId(model.getMemberId());
                memberProfile.setBindAt(new Date());
                memberProfile.setStatus((byte) 1);
                memberProfile.setVip(Boolean.FALSE);
                memberProfile.setWechatId(model.getWechatId());
                memberProfileMapper.insert(memberProfile);
            }
            memberProfile = updateMemberProfile(memberProfile, model);
            memberProfileMapper.updateByPrimaryKey(memberProfile);
            if (StringUtils.isBlank(model.getName()) || model.getInterests() == null || model.getInterests().length == 0) {
                throw new CommonException(ExceptionCode.MEMBER_INFO_INVALID, "会员名称或兴趣爱好不能为空！");
            }
            //发放红包
            ZegnaCampaign campaign = zegnaCampaignMapper.getByMemberProfileId(memberProfile.getId());
            if (campaign == null) {
                String accesstoken = RefreshAccessTokenJob.getAccessTokenStr(model.getWechatId());
                if (!zegnaBindDto.getStockout() && zegnaBindDto.getCampaignStatus()) {
                    ZegnaCampaign zegnaCampaign = new ZegnaCampaign();
                    zegnaCampaign.setCreateDate(new Date());
                    zegnaCampaign.setMemberProfileId(memberProfile.getId());
                    zegnaCampaign.setName("red");
                    zegnaCampaign.setStatus(Constant.VALID);
                    zegnaCampaignMapper.insert(zegnaCampaign);
                    //发送领取红包消息给user
                    Map<String, Object> configMap = configService.getConfigMap(
                            model.getWechatId(), Constant.ZEGNA_CAMPAIGN_NOTIFICATION);
                    String context = (String) configMap.get(Constant.RED_NOTIFICATION_MESSAGE);
                    KfcustomSend kfcustomSend = new KfcustomSend();
                    kfcustomSend.setAccess_token(accesstoken);
                    kfcustomSend.setTouser(model.getOpenId());
                    MsgText msgText = new MsgText();
                    msgText.setContent(context);
                    kfcustomSend.setText(msgText);
                    kfcustomSend.setMsgtype(MsgType.TEXT.name().toLowerCase());
                    String msg = JwKfaccountAPI.sendKfMessage(kfcustomSend);
                    log.info("openId:" + model.getOpenId() + "\t msg:" + msg);
                    addTagByOpenId(accesstoken, model);
                }
            }

        } else {
            //注册绑定
            if (verifyTokenService.verify(model.getWechatId(), model.getMemberId(), model.getMobile(), model.getCode())) {
                Member member = memberMapper.getMemberByMobile(model.getMobile());
                if (member != null) {
                    throw new WechatException(Message.MOBILE_USED);
                }
                member = memberMapper.getMemberByOpenId(model.getOpenId(), model.getWechatId());
                member.setMobile(model.getMobile());
                memberMapper.updateByPrimaryKey(member);

                MemberProfile memberProfile = memberProfileMapper.getByMemberId(model.getWechatId(), model.getMemberId());
                if (memberProfile == null) {
                    memberProfile = new MemberProfile();
                    createMemberProfile(memberProfile, model);
                    memberProfileMapper.insert(memberProfile);
                } else {
                    createMemberProfile(memberProfile, model);
                    memberProfileMapper.updateByPrimaryKey(memberProfile);
                }


            }
        }
        return zegnaBindDto;
    }

    public void addTagByOpenId(String accesstoken, ZegnaModel model){
        try {
            log.info("====>> Tag user start <<=====");
            log.info("access_token:" + accesstoken + "\nopenid:" + model.getOpenId());
            Integer wechatId = model.getWechatId();

            List<String> openIds = new ArrayList<>();
            openIds.add(model.getOpenId());

            //get taglist
            List<String> userExistTagList = JwTagApi.getTagByUser(accesstoken, model.getOpenId());

            Map<String, String> tagMap = new HashMap<>();
            WxTags wxTags = JwTagAPI.getTagList(accesstoken);
            List<WxTags.Tag> tagList = (List<WxTags.Tag>) wxTags.getTags();
            for (WxTags.Tag tag : tagList) {
                tagMap.put(tag.getName(), tag.getId().toString());
            }

            //get final tag name
            String finalTagName = getCompontTagName(wechatId, userExistTagList, tagMap);
            log.info("final tag:" + finalTagName);
            if (finalTagName == null) {
                return;
            }
            //remove tag
            List<String> tagNameList = Arrays.asList(finalTagName.split("_"));
            for (String tname : tagNameList) {
                if (!ADD_TAG.equalsIgnoreCase(tname)) {
                    String removeTag = configService.getConfigValue(wechatId, "ZEGNA_CAMPAIGN_RED_TAG", tname);
                    log.info("Remove tag:" + removeTag);
                    JwTagApi.memberBatchUnTag(accesstoken, tagMap.get(removeTag).toString(), openIds);
                }
            }
            String finalTag = configService.getConfigValue(wechatId, "ZEGNA_CAMPAIGN_RED_TAG", finalTagName);
            String tagId = tagMap.get(finalTag);

            if (tagId != null) {
                JwTagAPI.addTagToUser(accesstoken, openIds, Integer.parseInt(tagId));
                CampaignMemberTag memberTag = new CampaignMemberTag();
                memberTag.setCreateDate(new Date());
                memberTag.setMemberId(model.getId());
                memberTag.setName(finalTag);
                memberTag.setWxTagId(Integer.parseInt(tagId));
                campaignMemberTagMapper.insert(memberTag);
            }
            log.info("====>> Tag user end <<=====");
        }catch (Exception e){
            log.error("Failed to tag user:" + e.getMessage());
        }
    }

    public String getCompontTagName(Integer wechatId, List<String> userExistTagList, Map<String, String> tagMap){
        try {
            String mtmTagName = configService.getConfigValue(wechatId, "ZEGNA_CAMPAIGN_RED_TAG", "MTM");
            String macTagName = configService.getConfigValue(wechatId, "ZEGNA_CAMPAIGN_RED_TAG", "MAC");
            if (tagMap.containsKey(mtmTagName) && tagMap.containsKey(macTagName)
                    && userExistTagList.contains(tagMap.containsKey(mtmTagName))
                    && userExistTagList.contains(tagMap.containsKey(macTagName))) {
                return "MTM_MAC_CNY";
            }
            if (tagMap.containsKey(mtmTagName) && userExistTagList.contains(tagMap.containsKey(mtmTagName))) {
                return "MTM_CNY";
            }
            if (tagMap.containsKey(macTagName) && userExistTagList.contains(tagMap.containsKey(macTagName))) {
                return "MAC_CNY";
            }
            return "CNY";
        }catch (Exception e){
            log.error("Failed to get tag");
            return null;
        }
    }

    @Override
    public ZegnaBindDto getCampaignStatus(Member member) {
        MemberProfile memberProfile = memberProfileMapper.getByMemberId(member.getWechatId(), member.getId());
        ZegnaCampaign zegnaCampaign = zegnaCampaignMapper.getByMemberProfileId(memberProfile.getId());
        ZegnaBindDto zegnaBindDto = new ZegnaBindDto();
        zegnaBindDto = getConfigStatus(zegnaBindDto, member.getWechatId());
        if (zegnaCampaign == null) {
            zegnaBindDto.setStatus("invalid");
        } else {
            zegnaBindDto.setStatus(zegnaCampaign.getStatus());
        }
        return zegnaBindDto;
    }

    @Override
    public ZegnaBindDto updateCampaignStatus(ZegnaModel model) {
        ZegnaBindDto zegnaBindDto = new ZegnaBindDto();
        try {
            Map<String, Object> configMap = configService.getConfigMap(
                    model.getWechatId(), Constant.ZEGNA_CAMPAIGN_RED);
            String boutiqueCode = (String) configMap.get(Constant.BOUTIQUE_CODE);
            List<String> codeList = Arrays.asList(boutiqueCode.split(","));
            boolean flag = false;
            for (String code : codeList) {
                if (code.equalsIgnoreCase(model.getRemark()))
                    flag = true;
            }
            if (!flag)
                throw new CommonException(ExceptionCode.BOUTIQUE_CODE_INVALID, "核销码无效！");
            zegnaBindDto = getConfigStatus(zegnaBindDto, model.getWechatId());
            MemberProfile memberProfile = memberProfileMapper.getByMemberId(model.getWechatId(), model.getMemberId());
            ZegnaCampaign zegnaCampaign = zegnaCampaignMapper.getByMemberProfileId(memberProfile.getId());
            zegnaCampaign.setStatus(Constant.USED);
            zegnaCampaign.setRemark(model.getRemark());
            zegnaCampaign.setUpdateDate(new Date());
            int result = zegnaCampaignMapper.updateByPrimaryKey(zegnaCampaign);
            zegnaBindDto.setStatus(zegnaCampaign.getStatus());

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CommonException(ExceptionCode.COMMON_ERROR_CODE, e.getMessage());
        }
        return zegnaBindDto;
    }

    @Override
    public ZegnaBindDto getContext(ZegnaModel model) {
        Map<String, Object> configMap = configService.getConfigMap(
                model.getWechatId(), Constant.ZEGNA_CAMPAIGN_RED);
        JSONObject object = assembleContext(configMap);
        ZegnaBindDto zegnaBindDto = new ZegnaBindDto();

        zegnaBindDto = getConfigStatus(zegnaBindDto, model.getWechatId());

        MemberProfile memberProfile = memberProfileMapper.getByMemberId(model.getWechatId(), model.getMemberId());
        Member member = memberMapper.getMemberByOpenId(model.getOpenId(), model.getWechatId());

        if (StringUtils.isBlank(member.getMobile())) {
            zegnaBindDto.setBindStatus((byte) 0);
        } else {
            zegnaBindDto.setMobile(member.getMobile());
            if (memberProfile != null && StringUtils.isNotBlank(memberProfile.getInterests())) {
                zegnaBindDto.setBindStatus((byte) 1);
                zegnaBindDto.setName(memberProfile.getName());
                zegnaBindDto.setSex(memberProfile.getSex());
                zegnaBindDto.setEmail(memberProfile.getEmail());
                zegnaBindDto.setBirthday(DateUtil.formatYYYYMMDD(memberProfile.getBirthDate()));
                zegnaBindDto.setInterests(memberProfile.getInterests());
                zegnaBindDto.setStyles(memberProfile.getInterests());
            } else {
                zegnaBindDto.setBindStatus((byte) 2);
            }
        }
        zegnaBindDto.setContext(object);
        return zegnaBindDto;
    }

    @Override
    public ZegnaBindDto getConfigStatus(ZegnaBindDto zegnaBindDto, Integer wechatId) {
        Map<String, Object> configMap = configService.getConfigMap(
                wechatId, Constant.ZEGNA_CAMPAIGN_RED);
        String campaignDate = (String) configMap.get(Constant.CAMPAIGN_DATETIME);
        String campaignStatus = (String) configMap.get(Constant.CAMPAIGN_STATUS);
        Date nowDate = new Date();
        Date expirteDate = DateUtil.parse(campaignDate);
        if (nowDate.before(expirteDate)) {
            zegnaBindDto.setCampaignStatus(Boolean.TRUE);
            if (campaignStatus.equals("TRUE")) {
                zegnaBindDto.setStockout(Boolean.TRUE);
            } else {
                zegnaBindDto.setStockout(Boolean.FALSE);
            }
        } else {
            zegnaBindDto.setStockout(Boolean.TRUE);
            zegnaBindDto.setCampaignStatus(Boolean.FALSE);
        }
        return zegnaBindDto;
    }

    private ZegnaBindDto createZegnaBindDto(MemberProfile memberProfile, Member member) {
        ZegnaBindDto zegnaBindDto = new ZegnaBindDto();
        if (StringUtils.isBlank(member.getMobile())) {
            zegnaBindDto.setBindStatus((byte) 0);
        } else {
            if (memberProfile != null) {
                zegnaBindDto.setName(memberProfile.getName());
                zegnaBindDto.setEmail(memberProfile.getEmail());
                zegnaBindDto.setSex(memberProfile.getSex());
                zegnaBindDto.setInterests(memberProfile.getInterests());
                zegnaBindDto.setStyles(memberProfile.getStyles());
                zegnaBindDto.setBirthday(DateUtil.formatYYYYMMDD(memberProfile.getBirthDate()));
                if (StringUtils.isNotBlank(memberProfile.getInterests())) {
                    zegnaBindDto.setBindStatus((byte) 1);
                } else {
                    zegnaBindDto.setBindStatus((byte) 2);
                }
            }
        }
        return zegnaBindDto;
    }

    private MemberProfile updateMemberProfile(MemberProfile memberProfile, ZegnaModel model) {
        memberProfile.setName(model.getName());
        memberProfile.setEmail(model.getEmail());
        memberProfile.setSex(model.getSex());
        memberProfile.setBirthDate(DateUtil.parse(model.getBirthday()));
        memberProfile.setInterests(String.join(",", model.getInterests()));
        memberProfile.setStyles(String.join(",", model.getStyles()));

        return memberProfile;
    }

    private MemberProfile createMemberProfile(MemberProfile memberProfile, ZegnaModel model) {
        memberProfile.setMemberId(model.getMemberId());
        memberProfile.setBindAt(new Date());
        memberProfile.setStatus((byte) 1);
        memberProfile.setVip(Boolean.FALSE);
        memberProfile.setWechatId(model.getWechatId());

        ZegnaHistoryMember zegnaHistoryMember = zegnaHistoryMemberMapper.getHistoryMemberByMobile(model.getMobile());
        if (zegnaHistoryMember != null) {
            memberProfile.setName(zegnaHistoryMember.getName());
            memberProfile.setSex(zegnaHistoryMember.getSex());
            memberProfile.setEmail(zegnaHistoryMember.getEmail());
            memberProfile.setBirthDate(zegnaHistoryMember.getBirthDate());
        }
        return memberProfile;
    }


    private JSONObject assembleContext(Map<String, Object> configMap) {
        JSONObject campaign = new JSONObject();
        JSONObject cny = new JSONObject();
        JSONObject cny_register = new JSONObject();
        JSONObject cny_binding = new JSONObject();
        JSONObject cny_profile = new JSONObject();
        JSONObject cny_no_user = new JSONObject();
        JSONObject cny_have_user = new JSONObject();
        JSONObject bonus = new JSONObject();
        JSONObject stockout = new JSONObject();
        JSONObject stockout_register = new JSONObject();
        JSONObject stockout_binding = new JSONObject();
        JSONObject stockout_profile = new JSONObject();
        JSONObject routine = new JSONObject();
        JSONObject routine_register = new JSONObject();
        JSONObject routine_binding = new JSONObject();
        JSONObject routine_profile = new JSONObject();
        //cny 活动
        cny_register.put("tip", (String) configMap.get(Constant.REGISTER_CNY_NORMAL));
        cny.put("register", cny_register);
        cny_binding.put("tip", (String) configMap.get(Constant.PROFILE_BIND_NORMAL));
        cny_binding.put("confirm-btn", (String) configMap.get(Constant.CONFIRM_BUTTON));
        cny_binding.put("close-btn", (String) configMap.get(Constant.CLOSE_BUTTON));
        cny.put("personal-binding", cny_binding);
        cny_have_user.put("tip", (String) configMap.get(Constant.PROFILE_INFO_NORMAL));
        cny_have_user.put("return-btn", (String) configMap.get(Constant.PROFILE_RETURN_NORMAL_BTN));
        cny_have_user.put("close-btn", (String) configMap.get(Constant.PROFILE_NEXT_LACK_BTN));
        cny_profile.put("have-user", cny_have_user);
        cny_no_user.put("tip", (String) configMap.get(Constant.PROFILE_NO_USER_TIP));
        cny_no_user.put("return-btn", (String) configMap.get(Constant.PROFILE_NO_USER_EDIT_BTN));
        cny_no_user.put("close-btn", (String) configMap.get(Constant.PROFILE_NO_USER_CLOSE_BTN));
        cny_profile.put("no-user", cny_no_user);
        cny.put("personal-profile", cny_profile);
        bonus.put("tip", (String) configMap.get(Constant.REDEEMED_NORMAL));
        bonus.put("button", (String) configMap.get(Constant.REDEEMED_BUTTON));
        cny.put("bonus", bonus);
        campaign.put("cny", cny);
        //cny红包不足
        stockout_register.put("tip", (String) configMap.get(Constant.REGISTER_CNY_LACK));
        stockout.put("register", stockout_register);
        stockout_binding.put("tip", (String) configMap.get(Constant.PROFILE_BIND_LACK));
        stockout_binding.put("confirm-btn", (String) configMap.get(Constant.CONFIRM_BUTTON));
        stockout_binding.put("close-btn", (String) configMap.get(Constant.CLOSE_BUTTON));
        stockout.put("personal-binding", stockout_binding);
        stockout_profile.put("tip", (String) configMap.get(Constant.PROFILE_INFO_LACK));
        stockout_profile.put("return-btn", (String) configMap.get(Constant.PROFILE_RETURN_LACK_BTN));
        stockout_profile.put("close-btn", (String) configMap.get(Constant.CLOSE_BUTTON));
        stockout.put("personal-profile", stockout_profile);
        campaign.put("cny_stockout", stockout);
        //routine
        routine_register.put("tip", (String) configMap.get(Constant.REGISTER_ROUTINE));
        routine.put("register", routine_register);
        routine_binding.put("tip", (String) configMap.get(Constant.PROFILE_BIND_ROUTINE));
        routine_binding.put("confirm-btn", (String) configMap.get(Constant.CONFIRM_BUTTON));
        routine_binding.put("close-btn", (String) configMap.get(Constant.CLOSE_BUTTON));
        routine.put("personal-binding", routine_binding);
        routine_profile.put("tip", (String) configMap.get(Constant.PROFILE_INFO_ROUTINE));
        routine_profile.put("return-btn", (String) configMap.get(Constant.PROFILE_RETURN_ROUTINE_BTN));
        routine_profile.put("close-btn", (String) configMap.get(Constant.CLOSE_BUTTON));
        routine.put("personal-profile", routine_profile);
        campaign.put("routine", routine);

        return campaign;
    }

}
