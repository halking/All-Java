package com.d1m.wechat.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by D1M on 2017/5/3.
 */
public class MemberProfileSource {
    private String sourceName;
    private Integer sourceId;

    public MemberProfileSource(Integer sourceId, String sourceName) {
        this.sourceId = sourceId;
        this.sourceName = sourceName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public static List<MemberProfileSource> getAllSource() {
        List<MemberProfileSource> sourceList = new ArrayList<>();
        sourceList.add(new MemberProfileSource(1, "杰尼亚精品店邀约"));
        sourceList.add(new MemberProfileSource(2, "媒体邀请（澎湃）"));
        sourceList.add(new MemberProfileSource(3, "媒体邀请（GQ）"));
        sourceList.add(new MemberProfileSource(4, "媒体邀请（睿士）"));
        sourceList.add(new MemberProfileSource(5, "媒体邀请（费加罗）"));
        sourceList.add(new MemberProfileSource(6, "酩悦轩尼诗邀请"));
        sourceList.add(new MemberProfileSource(7, "品牌亲友"));
        return sourceList;
    }
}



