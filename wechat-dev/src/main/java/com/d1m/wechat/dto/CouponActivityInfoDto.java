package com.d1m.wechat.dto;

import java.util.List;

/**
 * Created by D1M on 2017/4/24.
 */
public class CouponActivityInfoDto {
    List<CouponActivityDto> couponActivity;
    List<BusinessDto> businessList;
    List<CouponActivityJoinInfoDto> joinInfoList;

    public List<CouponActivityDto> getCouponActivity() {
        return couponActivity;
    }

    public void setCouponActivity(List<CouponActivityDto> couponActivity) {
        this.couponActivity = couponActivity;
    }

    public List<BusinessDto> getBusinessList() {
        return businessList;
    }

    public void setBusinessList(List<BusinessDto> businessList) {
        this.businessList = businessList;
    }

    public List<CouponActivityJoinInfoDto> getJoinInfoList() {
        return joinInfoList;
    }

    public void setJoinInfoList(List<CouponActivityJoinInfoDto> joinInfoList) {
        this.joinInfoList = joinInfoList;
    }
}
