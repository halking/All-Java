package com.d1m.wechat.wxsdk.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 微信菜单
 *
 * @author f0rb on 2016-12-06.
 */
@Getter
@Setter
public class WxMenuGroup extends WxResponse {
    //微信菜单的按钮
    private List<WxMenu> button;
    //菜单id, 删除个性化菜单时传入
    private String menuId;
    //菜单匹配规则
    private WxMenuMatchrule matchrule;
}
