package com.d1m.wechat.service;

import com.d1m.wechat.dto.CouponActivityDto;
import com.d1m.wechat.dto.CouponActivityInfoDto;
import com.d1m.wechat.dto.CouponActivityJoinInfoDto;
import com.d1m.wechat.model.CouponActivity;
import com.d1m.wechat.pamametermodel.CouponActivityJoinModel;

import java.util.List;

/**
 * Created by D1M on 2017/4/24.
 */
public interface CouponActivityService extends IService<CouponActivity>{
    List<CouponActivityDto> getCouponActivity();

    List<CouponActivityJoinInfoDto> getActivityJoinInfo(Integer memberId);

    void join(CouponActivityJoinModel model, Integer memberId);
}
