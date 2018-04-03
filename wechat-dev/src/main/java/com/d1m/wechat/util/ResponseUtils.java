package com.d1m.wechat.util;

import com.d1m.wechat.common.response.BaseResponse;
import com.d1m.wechat.common.response.ErrorResponse;
import com.d1m.wechat.common.response.SuccessResponse;

/**
 * Created by Lisa on 2017/11/8.
 */
public class ResponseUtils {

    public static BaseResponse getSuccessReponse(Object object) {
        return new SuccessResponse(object);
    }

    public static BaseResponse getErrorReponse(int status, String errorMessage) {
        return new ErrorResponse(status, errorMessage);
    }
}
