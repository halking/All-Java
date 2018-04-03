package com.d1m.wechat.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.dto.MemberProfileDto;
import com.d1m.wechat.dto.ZegnaBindDto;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.pamametermodel.ZegnaModel;

public interface ZegnaBindService {

    ZegnaBindDto getProfile(ZegnaModel model, Member Member);

    ZegnaBindDto saveProfile(ZegnaModel model);

    ZegnaBindDto getCampaignStatus(Member member);

    ZegnaBindDto updateCampaignStatus(ZegnaModel model);

    ZegnaBindDto getContext(ZegnaModel model);

    ZegnaBindDto getConfigStatus(ZegnaBindDto zegnaBindDto, Integer wechatId);

}
