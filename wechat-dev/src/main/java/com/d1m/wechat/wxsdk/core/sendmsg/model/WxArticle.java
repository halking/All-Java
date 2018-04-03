package com.d1m.wechat.wxsdk.core.sendmsg.model;

import lombok.Getter;
import lombok.Setter;

import com.d1m.wechat.wxsdk.model.WxResponse;

/**
 * 图文消息
 * 
 * @author LIAIJUN
 *
 */
@Getter
@Setter
public class WxArticle extends WxResponse {

	/** 图文消息缩略图的media_id */
	private String thumb_media_id;
	/** 图文消息的作者 */
	private String author;
	/** 图文消息的标题 */
	private String title;
	/** 在图文消息页面点击“阅读原文”后的页面 */
	private String content_source_url;
	/** 图文消息页面的内容，支持HTML标签 */
	private String content;
	/** 图文消息的描述 */
	private String digest;
	/** 是否显示封面，1为显示，0为不显示 */
	private String show_cover_pic;

	private String url;

	private String thumb_url;

	private String fileName;

	private String filePath;

}
