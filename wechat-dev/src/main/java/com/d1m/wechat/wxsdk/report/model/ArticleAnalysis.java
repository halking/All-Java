package com.d1m.wechat.wxsdk.report.model;

public class ArticleAnalysis {
	
	private String msgid;
	private String title;
	private String refDate;
	private Integer pageUser;
	private Integer pageCount;
	private Integer oriPageUser;
	private Integer oriPageCount;
	private Integer addFavUser;
	private Integer addFavCount;
	private Integer shareUser;
	private Integer shareCount;
	private String refHour;
	private Integer userSource;
	
	public String getMsgid() {
		return msgid;
	}
	public String getTitle() {
		return title;
	}
	public String getRefDate() {
		return refDate;
	}
	public Integer getPageUser() {
		return pageUser;
	}
	public Integer getPageCount() {
		return pageCount;
	}
	public Integer getOriPageUser() {
		return oriPageUser;
	}
	public Integer getOriPageCount() {
		return oriPageCount;
	}
	public Integer getAddFavUser() {
		return addFavUser;
	}
	public Integer getAddFavCount() {
		return addFavCount;
	}
	public Integer getShareUser() {
		return shareUser;
	}
	public Integer getShareCount() {
		return shareCount;
	}
	public String getRefHour() {
		return refHour;
	}
	public Integer getUserSource() {
		return userSource;
	}
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setRefDate(String refDate) {
		this.refDate = refDate;
	}
	public void setPageUser(Integer pageUser) {
		this.pageUser = pageUser;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	public void setOriPageUser(Integer oriPageUser) {
		this.oriPageUser = oriPageUser;
	}
	public void setOriPageCount(Integer oriPageCount) {
		this.oriPageCount = oriPageCount;
	}
	public void setAddFavUser(Integer addFavUser) {
		this.addFavUser = addFavUser;
	}
	public void setAddFavCount(Integer addFavCount) {
		this.addFavCount = addFavCount;
	}
	public void setShareUser(Integer shareUser) {
		this.shareUser = shareUser;
	}
	public void setShareCount(Integer shareCount) {
		this.shareCount = shareCount;
	}
	public void setRefHour(String refHour) {
		this.refHour = refHour;
	}
	public void setUserSource(Integer userSource) {
		this.userSource = userSource;
	}

}
