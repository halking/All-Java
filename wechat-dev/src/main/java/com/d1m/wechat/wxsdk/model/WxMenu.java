package com.d1m.wechat.wxsdk.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 微信菜单的按钮
 *
 * @author f0rb on 2016-12-06.
 */
@Getter
@Setter
public class WxMenu {

    //菜单的类型，
    //公众平台官网上能够设置的菜单类型有view（跳转网页）、text（返回文本，下同）、img、photo、video、voice。使用API设置的则有8种
	private String type;

	private String name;

	private String key;

	private String url;

	private String value;

	private List<WxMenu> sub_button;

}
