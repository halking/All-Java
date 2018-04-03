package com.d1m.wechat.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "activity_session")
public class ActivitySession {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 开始时间
     */
    private Date start;

    /**
     * 结束时间
     */
    private Date end;

    /**
     * 场次名称
     */
    private String name;

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
     * 获取开始时间
     *
     * @return start - 开始时间
     */
    public Date getStart() {
        return start;
    }

    /**
     * 设置开始时间
     *
     * @param start 开始时间
     */
    public void setStart(Date start) {
        this.start = start;
    }

    /**
     * 获取结束时间
     *
     * @return end - 结束时间
     */
    public Date getEnd() {
        return end;
    }

    /**
     * 设置结束时间
     *
     * @param end 结束时间
     */
    public void setEnd(Date end) {
        this.end = end;
    }

    /**
     * 获取场次名称
     *
     * @return name - 场次名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置场次名称
     *
     * @param name 场次名称
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}