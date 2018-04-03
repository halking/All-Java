package com.d1m.wechat.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "coupon_activity")
public class CouponActivity {
    /**
     * 表主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
     * 票数
     */
    private Integer number;

    /**
     * 剩余票数
     */
    @Column(name = "left_number")
    private Integer leftNumber;

    /**
     * 获取表主键
     *
     * @return id - 表主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置表主键
     *
     * @param id 表主键
     */
    public void setId(Integer id) {
        this.id = id;
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
     * 获取票数
     *
     * @return number - 票数
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * 设置票数
     *
     * @param number 票数
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * 获取剩余票数
     *
     * @return left_number - 剩余票数
     */
    public Integer getLeftNumber() {
        return leftNumber;
    }

    /**
     * 设置剩余票数
     *
     * @param leftNumber 剩余票数
     */
    public void setLeftNumber(Integer leftNumber) {
        this.leftNumber = leftNumber;
    }
}