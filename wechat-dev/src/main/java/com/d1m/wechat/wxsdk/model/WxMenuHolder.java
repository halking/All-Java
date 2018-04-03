package com.d1m.wechat.wxsdk.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 微信菜单查询返回
 *
 * @author f0rb on 2016-12-06.
 */
@Getter
@Setter
public class WxMenuHolder extends WxResponse {
    //默认菜单
    private WxMenuGroup menu;
    //个性化菜单
    private List<WxMenuGroup> conditionalmenu;
}
