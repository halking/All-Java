package com.d1m.wechat.wxsdk.core.req.model.user;

public class Tag {

	private String id;

	private String name;

	private Integer count;

	public Integer getCount() {
		return count;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

}
