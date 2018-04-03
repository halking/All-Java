package com.d1m.wechat.controller.zegna;

import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.controller.BaseController;
import com.d1m.wechat.dto.ZegnaBindDto;
import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.model.VerifyToken;
import com.d1m.wechat.pamametermodel.ZegnaModel;
import com.d1m.wechat.service.*;
import com.d1m.wechat.util.Message;
import com.d1m.wechat.zegna.ZegnaSMSUtil;
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
import java.util.Enumeration;

@Slf4j
@Controller
@RequestMapping(value = "/zegna-binding")
public class ZegnaBindingController extends BaseController {

    @Autowired
    protected ConfigService configService;

    @Autowired
    private VerifyTokenService verifyTokenService;

    @Autowired
    private ZegnaBindService zegnaBindService;

    @RequestMapping(value = "/sendSms.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject sendMobileCode(
            @RequestBody(required = true) ZegnaModel model,
            HttpSession session, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Member zegnaMember = getMember(request);
            if (zegnaMember == null) {
                return representation(Message.MEMBER_NOT_LOGIN);
            }
            VerifyToken verifyToken = verifyTokenService.sendMobileCode(
                    zegnaMember.getWechatId(), zegnaMember.getId(),
                    model.getMobile());
            ZegnaSMSUtil.sendMsg(verifyToken.getAccept(), verifyToken.getCode());
            return representation(Message.VERIFY_TOKEN_SEND_SUCCESS, verifyToken.getCode());
        } catch (WechatException e){
            return wrapBookingException(e);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }


    @RequestMapping(value = "/get.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getProfile(
            @RequestBody(required = false) ZegnaModel model,
            HttpSession session, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Member zegnaMember = getMember(request);
            if (zegnaMember == null) {
                return representation(Message.MEMBER_NOT_LOGIN);
            }
            if (model == null) {
                model = new ZegnaModel();
            }
            model.setMemberId(zegnaMember.getId());
            model.setWechatId(zegnaMember.getWechatId());
            model.setOpenId(zegnaMember.getOpenId());

            ZegnaBindDto zegnaBindDto = zegnaBindService.getProfile(model, zegnaMember);

            return representation(Message.MEMBER_PROFILE_GET_SUCCESS, zegnaBindDto);

        } catch (WechatException e){
            return wrapBookingException(e);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }

    @RequestMapping(value = "/save.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject saveProfile(
            @RequestBody(required = true) ZegnaModel zegnaModel,
            HttpSession session, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Member zegnaMember = getMember(request);
            if (zegnaMember == null) {
                return representation(Message.MEMBER_NOT_LOGIN);
            }
            zegnaModel.setMemberId(zegnaMember.getId());
            zegnaModel.setWechatId(zegnaMember.getWechatId());
            zegnaModel.setOpenId(zegnaMember.getOpenId());

            zegnaBindService.saveProfile(zegnaModel);

            return representation(Message.MEMBER_PROFILE_UPDATE_SUCCESS);

        } catch (WechatException e){
            return wrapBookingException(e);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }

    @RequestMapping(value = "/lookRed.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject lookRedPackage(
            @RequestBody(required = false) ZegnaModel model,
            HttpSession session, HttpServletRequest request,
            HttpServletResponse response) {
        try {

            Member zegnaMember = getMember(request);

            if (zegnaMember == null) {
                return representation(Message.MEMBER_NOT_LOGIN);
            }
            ZegnaBindDto zegnaBindDto = zegnaBindService.getCampaignStatus(zegnaMember);

            return representation(Message.CAMPAIGN_LOOK_SUCCESS,zegnaBindDto);

        } catch (WechatException e){
            return wrapBookingException(e);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }

    @RequestMapping(value = "/redeemed.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject exchangeRedPackage(
            @RequestBody(required = false) ZegnaModel model,
            HttpSession session, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Member zegnaMember = getMember(request);
            if (zegnaMember == null) {
                return representation(Message.MEMBER_NOT_LOGIN);
            }
            if (model == null) {
                model = new ZegnaModel();
            }
            model.setMemberId(zegnaMember.getId());
            model.setWechatId(zegnaMember.getWechatId());
            model.setOpenId(zegnaMember.getOpenId());
            ZegnaBindDto zegnaBindDto = zegnaBindService.updateCampaignStatus(model);

            return representation(Message.CAMPAIGN_REDEEMED_SUCCESS,zegnaBindDto);
        } catch (WechatException e){
            return wrapBookingException(e);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }

    @RequestMapping(value = "/getContext.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getContext(
            @RequestBody(required = false) ZegnaModel model,
            HttpSession session, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Member zegnaMember = getMember(request);
            if (zegnaMember == null) {
                return representation(Message.MEMBER_NOT_LOGIN);
            }
            if (model == null) {
                model = new ZegnaModel();
            }

            model.setMemberId(zegnaMember.getId());
            model.setWechatId(zegnaMember.getWechatId());
            model.setOpenId(zegnaMember.getOpenId());

            ZegnaBindDto zegnaBindDto = zegnaBindService.getContext(model);

            return representation(Message.MEMBER_PROFILE_GET_SUCCESS, zegnaBindDto);
        } catch (WechatException e){
            return wrapBookingException(e);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }

}
