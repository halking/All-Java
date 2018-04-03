package com.d1m.wechat.wxsdk.core.req.model;

import com.d1m.wechat.wxsdk.core.annotation.ReqType;

@ReqType("mediaUploadImg")
public class UploadMediaImg extends WeixinReqParam{
	
	private String filePathName;

	public String getFilePathName() {
		return filePathName;
	}

	public void setFilePathName(String filePathName) {
		this.filePathName = filePathName;
	}

}
