package com.d1m.wechat.service.impl;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import com.d1m.wechat.mapper.CustomerInfoMapper;
import com.d1m.wechat.mapper.CustomerTokenMapper;
import com.d1m.wechat.model.CustomerInfo;
import com.d1m.wechat.service.CustomerInfoService;
import com.d1m.wechat.service.CustomerTokenService;

/**
 * CustomerInfoServiceImpl
 *
 * @author f0rb on 2017-02-15.
 */
@Slf4j
@Service
public class CustomerInfoServiceImpl extends BaseService<CustomerInfo> implements CustomerInfoService {

    private static int EXPIRED_SECONDS = 7200;

    @Resource
    private CustomerInfoMapper customerInfoMapper;

    @Resource
    private CustomerTokenMapper customerTokenMapper;

    @Resource
    private CustomerTokenService customerTokenService;

    @Override
    public Mapper<CustomerInfo> getGenericMapper() {
        return customerInfoMapper;
    }


}
