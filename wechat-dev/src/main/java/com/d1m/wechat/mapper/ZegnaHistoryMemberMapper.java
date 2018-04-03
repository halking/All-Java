package com.d1m.wechat.mapper;

import com.d1m.wechat.model.ZegnaHistoryMember;
import com.d1m.wechat.util.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface ZegnaHistoryMemberMapper extends MyMapper<ZegnaHistoryMember> {

    ZegnaHistoryMember getHistoryMemberByMobile(@Param("mobile") String mobile);
}
