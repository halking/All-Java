package com.d1m.wechat.service;

import com.d1m.wechat.model.MemberProfile;
import com.d1m.wechat.pamametermodel.ZegnaModel;

public interface MemberProfileService extends IService<MemberProfile> {

	MemberProfile getByMemberId(Integer wechatId, Integer memberId);

	Integer getMemberBindStatus(Integer id, Integer wechatId);

	MemberProfile bind(ZegnaModel model);

}
