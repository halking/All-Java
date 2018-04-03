package com.d1m.wechat.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "book_on_appointment")
public class BookOnAppointment {
    /**
     * 主键ID
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
     * 预订日期
     */
    @Column(name = "book_date")
    private Date bookDate;

    /**
     * 店铺ID
     */
    @Column(name = "store_id")
    private Integer storeId;

    /**
     * 1: 上午    2：下午
     */
    @Column(name = "book_time")
    private Integer bookTime;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
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
     * 获取预订日期
     *
     * @return book_date - 预订日期
     */
    public Date getBookDate() {
        return bookDate;
    }

    /**
     * 设置预订日期
     *
     * @param bookDate 预订日期
     */
    public void setBookDate(Date bookDate) {
        this.bookDate = bookDate;
    }

    /**
     * 获取店铺ID
     *
     * @return store_id - 店铺ID
     */
    public Integer getStoreId() {
        return storeId;
    }

    /**
     * 设置店铺ID
     *
     * @param storeId 店铺ID
     */
    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    /**
     * 获取1: 上午    2：下午
     *
     * @return book_time - 1: 上午    2：下午
     */
    public Integer getBookTime() {
        return bookTime;
    }

    /**
     * 设置1: 上午    2：下午
     *
     * @param bookTime 1: 上午    2：下午
     */
    public void setBookTime(Integer bookTime) {
        this.bookTime = bookTime;
    }
}