package com.d1m.wechat.wxsdk.wxbase.wxmedia.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.d1m.wechat.wxsdk.model.WxResponse;

/**
 * 微信素材的分页列表
 * 
 * @author f0rb on 2016-12-02.
 *
 */
@Getter
@Setter
public class WxMaterialPage extends WxResponse {
	/** 素材列表 */
	private List<WxMaterial> item;
	/** 单次拉取的素材的数量 */
	private Integer item_count;
    /** 素材总数 */
    private Integer total_count;
}
