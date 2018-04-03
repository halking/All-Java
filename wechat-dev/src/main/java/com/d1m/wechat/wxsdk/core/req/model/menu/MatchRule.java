package com.d1m.wechat.wxsdk.core.req.model.menu;

public class MatchRule {

	private String tag_id;

	private String sex;

	private String country;

	private String province;

	private String city;

	private Byte client_platform_type;

	private String language;

	public String getCity() {
		return city;
	}

	public Byte getClient_platform_type() {
		return client_platform_type;
	}

	public String getCountry() {
		return country;
	}

	public String getLanguage() {
		return language;
	}

	public String getProvince() {
		return province;
	}

	public String getSex() {
		return sex;
	}

	public String getTag_id() {
		return tag_id;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setClient_platform_type(Byte client_platform_type) {
		this.client_platform_type = client_platform_type;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public void setTag_id(String tag_id) {
		this.tag_id = tag_id;
	}

}
