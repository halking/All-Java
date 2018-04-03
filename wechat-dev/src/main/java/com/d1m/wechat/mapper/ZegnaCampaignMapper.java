package com.d1m.wechat.mapper;

import com.d1m.wechat.model.ZegnaCampaign;
import com.d1m.wechat.util.MyMapper;

public interface ZegnaCampaignMapper extends MyMapper<ZegnaCampaign> {

    ZegnaCampaign getByMemberProfileId(Integer profileId);
}
