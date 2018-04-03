package com.d1m.wechat.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "coupon_activity_member")
public class CouponActivityMember {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用户ID
     */
    @Column(name = "member_id")
    private Integer memberId;

    /**
     * 活动日期
     */
    private Date day;

    /**
     * 活动场次
     */
    @Column(name = "activity_session_id")
    private Integer activitySessionId;

    /**
     * 领取数量
     */
    private Integer number;

    /**
     * 门店ID
     */
    @Column(name = "business_id")
    private Integer businessId;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取用户ID
     *
     * @return member_id - 用户ID
     */
    public Integer getMemberId() {
        return memberId;
    }

    /**
     * 设置用户ID
     *
     * @param memberId 用户ID
     */
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    /**
     * 获取活动日期
     *
     * @return day - 活动日期
     */
    public Date getDay() {
        return day;
    }

    /**
     * 设置活动日期
     *
     * @param day 活动日期
     */
    public void setDay(Date day) {
        this.day = day;
    }

    /**
     * 获取活动场次
     *
     * @return activity_session_id - 活动场次
     */
    public Integer getActivitySessionId() {
        return activitySessionId;
    }

    /**
     * 设置活动场次
     *
     * @param activitySessionId 活动场次
     */
    public void setActivitySessionId(Integer activitySessionId) {
        this.activitySessionId = activitySessionId;
    }

    /**
     * 获取领取数量
     *
     * @return number - 领取数量
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * 设置领取数量
     *
     * @param number 领取数量
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * 获取门店ID
     *
     * @return business_id - 门店ID
     */
    public Integer getBusinessId() {
        return businessId;
    }

    /**
     * 设置门店ID
     *
     * @param businessId 门店ID
     */
    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }
}