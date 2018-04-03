package com.d1m.wechat.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "zegna_member_baa_log")
public class ZegnaMemberBaaLog {
    /**
     * 表主键ID
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
     * 商品ID
     */
    @Column(name = "goods_id")
    private Integer goodsId;

    /**
     * 记录时间
     */
    private Date date;

    /**
     * 获取表主键ID
     *
     * @return id - 表主键ID
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置表主键ID
     *
     * @param id 表主键ID
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
     * 获取商品ID
     *
     * @return goods_id - 商品ID
     */
    public Integer getGoodsId() {
        return goodsId;
    }

    /**
     * 设置商品ID
     *
     * @param goodsId 商品ID
     */
    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * 获取记录时间
     *
     * @return date - 记录时间
     */
    public Date getDate() {
        return date;
    }

    /**
     * 设置记录时间
     *
     * @param date 记录时间
     */
    public void setDate(Date date) {
        this.date = date;
    }
}