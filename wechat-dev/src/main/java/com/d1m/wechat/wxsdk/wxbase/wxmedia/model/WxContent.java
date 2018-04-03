package com.d1m.wechat.wxsdk.wxbase.wxmedia.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.d1m.wechat.wxsdk.core.sendmsg.model.WxArticle;

/**
 * 图文素材的内容
 *
 * @author f0rb on 2016-12-01.
 */
@Getter
@Setter
public class WxContent {
    private List<WxArticle> news_item = new ArrayList<WxArticle>();
}
