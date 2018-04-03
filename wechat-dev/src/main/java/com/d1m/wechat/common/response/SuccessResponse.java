package com.d1m.wechat.common.response;

import lombok.Data;
import org.apache.http.HttpStatus;

@Data
public class SuccessResponse<T> implements BaseResponse {
	
	private static final long serialVersionUID = 1L;

	private int status;
	private String message;
	private T data;

	public SuccessResponse() {
	}

	public SuccessResponse(T t) {
		this();
		this.status = HttpStatus.SC_OK;
		this.setMessage("Success");
		this.setData(t);
	}
}
