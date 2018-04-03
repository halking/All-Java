package com.d1m.wechat.service;

import com.d1m.wechat.model.CustomerToken;

/**
 * CustomerTokenService
 *
 * @author f0rb on 2017-02-15.
 */
public interface CustomerTokenService extends IService<CustomerToken> {

    CustomerToken createToken(String appid, String appkey);

    CustomerToken checkToken(String token);

}
