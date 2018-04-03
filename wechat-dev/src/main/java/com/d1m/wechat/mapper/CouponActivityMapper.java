package com.d1m.wechat.mapper;

import com.d1m.wechat.dto.CouponActivityDto;
import com.d1m.wechat.dto.CouponActivityInfoDto;
import com.d1m.wechat.dto.CouponActivityJoinInfoDto;
import com.d1m.wechat.model.CouponActivity;
import com.d1m.wechat.util.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CouponActivityMapper extends MyMapper<CouponActivity> {
    List<CouponActivityDto> getCouponActivity();

    List<CouponActivityJoinInfoDto> getActivityJoinInfo(@Param("memberId")Integer memberId);
}