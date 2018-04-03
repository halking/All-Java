package com.d1m.wechat.mapper;

import com.d1m.wechat.model.CampaignMemberTag;
import com.d1m.wechat.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CampaignMemberTagMapper extends MyMapper<CampaignMemberTag> {
    void bunchDelete(@Param("tagIdList") List<String> tagIdList, @Param("wechatId") Integer wechatId);
}
