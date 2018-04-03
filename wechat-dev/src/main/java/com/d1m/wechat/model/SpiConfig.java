package com.d1m.wechat.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "spi_config")
public class SpiConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "wechat_id")
    private Integer wechatId;

    private String event;

    private String action;

    private String params;

    @Column(name = "creator_id")
    private Integer creatorId;

    @Column(name = "created_at")
    private Date createdAt;

    private Byte status;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return wechat_id
     */
    public Integer getWechatId() {
        return wechatId;
    }

    /**
     * @param wechatId
     */
    public void setWechatId(Integer wechatId) {
        this.wechatId = wechatId;
    }

    /**
     * @return event
     */
    public String getEvent() {
        return event;
    }

    /**
     * @param event
     */
    public void setEvent(String event) {
        this.event = event == null ? null : event.trim();
    }

    /**
     * @return action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action
     */
    public void setAction(String action) {
        this.action = action == null ? null : action.trim();
    }

    /**
     * @return params
     */
    public String getParams() {
        return params;
    }

    /**
     * @param params
     */
    public void setParams(String params) {
        this.params = params == null ? null : params.trim();
    }

    /**
     * @return creator_id
     */
    public Integer getCreatorId() {
        return creatorId;
    }

    /**
     * @param creatorId
     */
    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * @return created_at
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return status
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(Byte status) {
        this.status = status;
    }
}