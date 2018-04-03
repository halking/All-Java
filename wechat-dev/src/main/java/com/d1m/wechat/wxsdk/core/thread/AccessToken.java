package com.d1m.wechat.wxsdk.core.thread;

public class AccessToken {

	private final static int AHEAD_TIME=1000;  //提前量 秒
	private String accessToken="";
	private int expired=7200;
	private long lastRefreshTimeMillis;  //最后一次刷新的时间

	public AccessToken(String accessToken, int expired) {
		this.setAccessToken(accessToken);
		this.expired = expired;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public long getExpired() {
		return expired;
	}

	/**
	 * 每次重新设置时 重置为最新的时间
	 * @param accessToken
     */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
		this.resetLastRefreshTimeMillis();
	}

	public void setExpired(int expired) {
		this.expired = expired;
	}

	/**
	 * 检查accessToken是否过期
	 * @return
     */
	public boolean isExpired(){
		return System.currentTimeMillis()-this.lastRefreshTimeMillis>=(this.expired-AccessToken.AHEAD_TIME)*1000;
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
