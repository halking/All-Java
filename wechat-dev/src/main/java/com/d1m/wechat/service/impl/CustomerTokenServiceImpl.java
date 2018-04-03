package com.d1m.wechat.service.impl;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import com.d1m.wechat.common.Constant;
import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.mapper.CustomerInfoMapper;
import com.d1m.wechat.mapper.CustomerTokenMapper;
import com.d1m.wechat.model.CustomerInfo;
import com.d1m.wechat.model.CustomerToken;
import com.d1m.wechat.service.CustomerTokenService;
import com.d1m.wechat.util.Message;
import com.d1m.wechat.util.Rand;

/**
 * CustomerTokenServiceImpl
 *
 * @author f0rb on 2017-02-15.
 */
@Slf4j
@Service
public class CustomerTokenServiceImpl extends BaseService<CustomerToken> implements CustomerTokenService {

    private static int EXPIRED_SECONDS = 7200;

    @Resource
    private CustomerInfoMapper customerInfoMapper;

    @Resource
    private CustomerTokenMapper customerTokenMapper;

    @Override
    public Mapper<CustomerToken> getGenericMapper() {
        return customerTokenMapper;
    }

    @Override
    public CustomerToken createToken(String appid, String appkey) {
        //校验appid和appkey
        CustomerInfo query = new CustomerInfo();
        query.setAppid(appid);
        query.setAppkey(appkey);
        List<CustomerInfo> customerInfoList = customerInfoMapper.select(query);
        if (customerInfoList.size() != 1) {
            log.warn("查询条件:appid={}, appkey={}, 记录总数={}", customerInfoList.size());
            return null;
        }
        CustomerInfo customerInfo = customerInfoList.get(0);

        //废弃掉旧的token
        CustomerToken queryToken = new CustomerToken();
        queryToken.setCustomerInfoId(customerInfo.getId());
        queryToken.setStatus(Constant.STATUS_VALID);
        List<CustomerToken> latestTokenList = customerTokenMapper.select(queryToken);
        long CURRENT = System.currentTimeMillis();
        if (!latestTokenList.isEmpty()) {
            // 暂时这样处理并发时插入多条customerToken的问题
            CustomerToken lastToken = latestTokenList.get(latestTokenList.size() - 1);
            if (lastToken.getExpiredAt().getTime() > CURRENT) {
                lastToken.setExpiresIn((int) ((lastToken.getExpiredAt().getTime() - CURRENT)/1000));
                return lastToken;
            }
        }
        for (CustomerToken customerToken: latestTokenList) {
            customerToken.setExpiredAt(new Date());
            customerToken.setStatus(Constant.STATUS_INVALID);
            customerTokenMapper.updateByPrimaryKey(customerToken);
        }
        //生成新的token
        CustomerToken customerToken = new CustomerToken();
        customerToken.setCustomerInfoId(customerInfo.getId());
        customerToken.setAppid(customerInfo.getAppid());
        customerToken.setAppkey(customerInfo.getAppkey());
        customerToken.setWechatId(customerInfo.getWechatId());
        customerToken.setToken(generateToken());
        customerToken.setCreatedAt(new Date());
        customerToken.setExpiredAt(new Date(CURRENT + EXPIRED_SECONDS * 1000));
        customerToken.setStatus(Constant.STATUS_VALID);

        customerToken.setExpiresIn(EXPIRED_SECONDS);
        customerTokenMapper.insert(customerToken);

        return customerToken;
    }

    @Override
    public CustomerToken checkToken(String token) {
        if (StringUtils.isNotBlank(token)) {
            CustomerToken queryToken = new CustomerToken();
            queryToken.setToken(token);
            CustomerToken customerToken = customerTokenMapper.selectOne(queryToken);
            if (customerToken != null) {
                // token标志为有效, 但是已经过期, 需更新token的状态
                if (customerToken.getStatus() == Constant.STATUS_VALID
                        && System.currentTimeMillis() > customerToken.getExpiredAt().getTime()) {
                    customerToken.setStatus(Constant.STATUS_INVALID);
                    customerTokenMapper.updateByPrimaryKey(customerToken);
                }

                // token有效或者失效时间在1分钟以内
                if (customerToken.getStatus() == Constant.STATUS_VALID
                        || System.currentTimeMillis() - customerToken.getExpiredAt().getTime() < 60 * 1000) {
                    return customerToken;
                }
            } else {
                log.warn("token不存在: {} ", token);
            }
        }
        throw new WechatException(Message.CUSTOMER_TOKEN_INVALID);
    }

    private String generateToken() {
        return Rand.randomString(98) + "==";
    }
}
