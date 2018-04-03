package com.d1m.wechat.wxsdk.template.model;

public class TemplateAnalysis {
	
	private String templateId;
	private String primaryIndustry;
	private String deputyIndustry;
	private String title;
	private String content;
	
	public String getTemplateId() {
		return templateId;
	}
	public String getPrimaryIndustry() {
		return primaryIndustry;
	}
	public String getDeputyIndustry() {
		return deputyIndustry;
	}
	public String getTitle() {
		return title;
	}
	public String getContent() {
		return content;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public void setPrimaryIndustry(String primaryIndustry) {
		this.primaryIndustry = primaryIndustry;
	}
	public void setDeputyIndustry(String deputyIndustry) {
		this.deputyIndustry = deputyIndustry;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
