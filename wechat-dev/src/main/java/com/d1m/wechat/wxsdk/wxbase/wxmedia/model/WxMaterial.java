package com.d1m.wechat.wxsdk.wxbase.wxmedia.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import com.d1m.wechat.wxsdk.model.WxResponse;

/**
 * 微信素材
 *
 * @author f0rb on 2016-12-02.
 */
@Getter
@Setter
public class WxMaterial extends WxResponse {
    /** 图文素材的内容 */
    private WxContent content;
    /** 媒体id */
    private String media_id;
    /** 这该素材最后更新的时间 */
    private Date update_time;
    /** 图片,视频,语音文件的名称 */
    private String name;
    /** 图文素材的URL或者是图片,视频,语音的URL */
    private String url;

    public void setUpdate_time(Date update_time) {
        if (System.currentTimeMillis() / 1000 > update_time.getTime()) {
            //微信返的update_time是以秒为单位
            update_time = new Date(update_time.getTime() * 1000);
        }
        this.update_time = update_time;
    }
}
