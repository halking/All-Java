package com.d1m.wechat.service.impl;

import com.d1m.wechat.dto.CouponActivityDto;
import com.d1m.wechat.dto.CouponActivityInfoDto;
import com.d1m.wechat.dto.CouponActivityJoinInfoDto;
import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.mapper.ConversationMapper;
import com.d1m.wechat.mapper.CouponActivityMapper;
import com.d1m.wechat.mapper.CouponActivityMemberMapper;
import com.d1m.wechat.model.Conversation;
import com.d1m.wechat.model.CouponActivity;
import com.d1m.wechat.model.CouponActivityMember;
import com.d1m.wechat.pamametermodel.CouponActivityJoinModel;
import com.d1m.wechat.service.ConversationService;
import com.d1m.wechat.service.CouponActivityService;
import com.d1m.wechat.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by D1M on 2017/4/24.
 */
@Service
@Slf4j
public class CouponActivityServiceImpl extends BaseService<CouponActivity>
        implements CouponActivityService {

    @Autowired
    private CouponActivityMapper couponActivityMapper;

    @Autowired
    private CouponActivityMemberMapper couponActivityMemberMapper;

    @Override
    public List<CouponActivityDto> getCouponActivity() {
        return couponActivityMapper.getCouponActivity();
    }

    @Override
    public List<CouponActivityJoinInfoDto> getActivityJoinInfo(Integer memberId) {
        return couponActivityMapper.getActivityJoinInfo(memberId);
    }

    @Override
    public void join(CouponActivityJoinModel model, Integer memberId) {
        int alreadyGot = 0;
        List<CouponActivityJoinInfoDto> activityJoinInfo = getActivityJoinInfo(memberId);
        for(CouponActivityJoinInfoDto joinInfoDto : activityJoinInfo) {
            alreadyGot += joinInfoDto.getNumber();
        }
        if(alreadyGot >= 2) {
            throw new WechatException(Message.COUPON_BOOKING_ALREADY);
        }
        if(model.getBusinessId() == null) {
            throw new WechatException(Message.COUPON_BOOKING_BUSINESS_NOT_BLANK);
        }
        if(model.getSessionId() == null) {
            throw new WechatException(Message.COUPON_BOOKING_SESSION_NOT_BLANK);
        }
        if(model.getNumber() == null || model.getNumber() == 0) {
            throw new WechatException(Message.COUPON_BOOKING_NUMBER_NOT_ZERO);
        }
        if(alreadyGot + model.getNumber() > 2) {
            throw new WechatException(Message.COUPON_BOOKING_NUMBER_NOT_MORE_THAN_MAX);
        }
        CouponActivity ca = new CouponActivity();
        ca.setId(model.getSessionId());
        ca = couponActivityMapper.selectOne(ca);
        if(ca == null) {
            throw new WechatException(Message.COUPON_BOOKING_SESSION_NOT_EXISTED);
        }
        if(ca.getLeftNumber() - model.getNumber() < 0) {
            log.error(Message.COUPON_BOOKING_NUMBER_NOT_ENOUGH + " number: {}, sessionId: {}", model.getNumber(), model.getSessionId());
            throw new WechatException(Message.COUPON_BOOKING_NUMBER_NOT_ENOUGH);
        }
        ca.setLeftNumber(ca.getLeftNumber() - model.getNumber());
        couponActivityMapper.updateByPrimaryKey(ca);

        if(activityJoinInfo != null && 1 == activityJoinInfo.size()) {
            CouponActivityMember couponActivityMember = new CouponActivityMember();
            couponActivityMember.setDay(ca.getDay());
            couponActivityMember.setActivitySessionId(ca.getActivitySessionId());
            couponActivityMember.setMemberId(memberId);
            couponActivityMember = couponActivityMemberMapper.selectOne(couponActivityMember);
            if(couponActivityMember != null) {
                couponActivityMember.setNumber(couponActivityMember.getNumber() + model.getNumber());
                couponActivityMemberMapper.updateByPrimaryKeySelective(couponActivityMember);
            }
        } else {
            CouponActivityMember couponActivityMember = new CouponActivityMember();
            couponActivityMember.setDay(ca.getDay());
            couponActivityMember.setActivitySessionId(ca.getActivitySessionId());
            couponActivityMember.setBusinessId(model.getBusinessId());
            couponActivityMember.setNumber(model.getNumber());
            couponActivityMember.setMemberId(memberId);
            couponActivityMemberMapper.insert(couponActivityMember);
        }

    }

    @Override
    public Mapper<CouponActivity> getGenericMapper() {
        return couponActivityMapper;
    }
}
