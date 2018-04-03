package com.d1m.wechat.service;

import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.model.VerifyToken;

public interface VerifyTokenService extends IService<VerifyToken> {

	VerifyToken getVerifyTokenByCode(Integer wechatId, Integer memberId,
			String accept, String code);

	VerifyToken getVerifyTokenByToken(Integer wechatId,
			Integer memberId, String accept, String token);

	VerifyToken sendMobileCode(Integer wechatId, Integer memberId,
			String mobile);

	boolean verify(Integer wechatId, Integer memberId, String accept,
			String code)  throws WechatException;

}
