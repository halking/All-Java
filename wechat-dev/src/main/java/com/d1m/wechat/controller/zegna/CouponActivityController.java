package com.d1m.wechat.controller.zegna;

import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.controller.BaseController;
import com.d1m.wechat.dto.BusinessDto;
import com.d1m.wechat.dto.CouponActivityDto;
import com.d1m.wechat.dto.CouponActivityInfoDto;
import com.d1m.wechat.dto.CouponActivityJoinInfoDto;
import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.pamametermodel.CouponActivityJoinModel;
import com.d1m.wechat.service.BusinessService;
import com.d1m.wechat.service.CouponActivityService;
import com.d1m.wechat.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by D1M on 2017/4/24.
 */
@Slf4j
@Controller
@RequestMapping("/coupon_activity")
public class CouponActivityController extends BaseController {


    @Autowired
    private CouponActivityService couponActivityService;

    @Autowired
    private BusinessService businessService;

    @RequestMapping(value = "/get.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject register(
            HttpSession session, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Member member = getMember(request);
            if (member == null) {
                return representation(Message.MEMBER_NOT_LOGIN);
            }
            CouponActivityInfoDto couponActivityInfoDto = new CouponActivityInfoDto();
            final List<CouponActivityDto> couponActivity = couponActivityService.getCouponActivity();
            couponActivityInfoDto.setCouponActivity(couponActivity);

            List<CouponActivityJoinInfoDto> activityJoinInfo = couponActivityService.getActivityJoinInfo(member.getId());
            couponActivityInfoDto.setJoinInfoList(activityJoinInfo);
            if(activityJoinInfo != null) {
                log.info("activityJoinInfo size: {}", activityJoinInfo.size());
            }

            final List<BusinessDto> shanghaiBusinessList = businessService.getShanghaiBusinessList();
            couponActivityInfoDto.setBusinessList(shanghaiBusinessList);

            return representation(Message.SUCCESS, couponActivityInfoDto);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            return wrapException(e);
        }
    }

    @RequestMapping(value = "/join.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject join(@RequestBody(required = false) CouponActivityJoinModel model,
            HttpSession session, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Member member = getMember(request);
            if (member == null) {
                return representation(Message.MEMBER_NOT_LOGIN);
            }
            if (model == null) {
                model = new CouponActivityJoinModel();
            }
            couponActivityService.join(model, member.getId());
            return representation(Message.SUCCESS);
        } catch (WechatException e) {
            log.error(e.getLocalizedMessage());
            return wrapBookingException(e);
        }
    }
}
