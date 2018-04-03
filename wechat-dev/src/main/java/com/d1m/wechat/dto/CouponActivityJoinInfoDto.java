package com.d1m.wechat.dto;

/**
 * Created by D1M on 2017/4/24.
 */
public class CouponActivityJoinInfoDto {
    private String dayStr;
    private String timeStr;
    private Integer businessId;
    private Integer number;
    private Integer sessionId;
    private Integer leftNumber;

    public String getDayStr() {
        return dayStr;
    }

    public void setDayStr(String dayStr) {
        this.dayStr = dayStr;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getLeftNumber() {
        return leftNumber;
    }

    public void setLeftNumber(Integer leftNumber) {
        this.leftNumber = leftNumber;
    }
}
