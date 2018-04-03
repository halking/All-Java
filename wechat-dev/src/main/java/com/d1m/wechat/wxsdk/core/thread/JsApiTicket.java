package com.d1m.wechat.wxsdk.core.thread;

public class JsApiTicket {

	private final static int AHEAD_TIME=1000;  //提前量 秒
	private String ticket="";
	private int expired=7200;
	private long lastRefreshTimeMillis;  //最后一次刷新的时间

	/**
	 * @param ticket
	 * @param expired
	 */
	public JsApiTicket(String ticket, int expired) {
		super();
		this.setTicket(ticket);
		this.expired = expired;
	}

	public int getExpired() {
		return expired;
	}

	public String getTicket() {
		return ticket;
	}

	public void setExpired(int expired) {
		this.expired = expired;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
		this.resetLastRefreshTimeMillis();
	}
	/**
	 * 检查accessToken是否过期
	 * @return
	 */
	public boolean isExpired(){
		return System.currentTimeMillis()-this.lastRefreshTimeMillis>=(this.expired-JsApiTicket.AHEAD_TIME)*1000;
	}

	/**
	 * 设置最后一次刷新时间
	 */
	public void resetLastRefreshTimeMillis(){
		this.lastRefreshTimeMillis=System.currentTimeMillis();
	}

	public void setLastRefreshTimeMillis(long lastRefreshTimeMillis) {
		this.lastRefreshTimeMillis = lastRefreshTimeMillis;
	}

	public long getLastRefreshTimeMillis() {
		return lastRefreshTimeMillis;
	}

}
