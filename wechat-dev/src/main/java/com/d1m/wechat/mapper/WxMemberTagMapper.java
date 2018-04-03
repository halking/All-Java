package com.d1m.wechat.mapper;

import com.d1m.wechat.model.WxMemberTag;
import com.d1m.wechat.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface WxMemberTagMapper extends MyMapper<WxMemberTag> {
    void deleteBySyncDate(@Param("wechatId") Integer wechatId, @Param("syncDate") Date syncDate);
}