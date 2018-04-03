package com.d1m.wechat.dto;

import java.util.List;

/**
 * Created by D1M on 2017/4/24.
 */
public class CouponActivityDto {
    private Integer id;
    private String dayStr;
    private Integer number;
    private Integer leftNumber;
    private String timeStr;
    private List<CouponActivityDto> couponActivityList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDayStr() {
        return dayStr;
    }

    public void setDayStr(String dayStr) {
        this.dayStr = dayStr;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getLeftNumber() {
        return leftNumber;
    }

    public void setLeftNumber(Integer leftNumber) {
        this.leftNumber = leftNumber;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public List<CouponActivityDto> getCouponActivityList() {
        return couponActivityList;
    }

    public void setCouponActivityList(List<CouponActivityDto> couponActivityList) {
        this.couponActivityList = couponActivityList;
    }
}
