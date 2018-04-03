package com.d1m.wechat.common.response;

import lombok.Data;

@Data
public class ErrorResponse implements BaseResponse {
	
	private int status;
	private String errorMessage;
	
	public ErrorResponse(int status, String errorMessage) {
		this.status = status;
		this.errorMessage = errorMessage;
	}
}
