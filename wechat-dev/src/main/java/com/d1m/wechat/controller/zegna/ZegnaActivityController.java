package com.d1m.wechat.controller.zegna;

import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.controller.BaseController;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.model.MemberProfile;
import com.d1m.wechat.model.VerifyToken;
import com.d1m.wechat.util.MemberProfileSource;
import com.d1m.wechat.pamametermodel.ZegnaModel;
import com.d1m.wechat.service.BookOnAppointmentService;
import com.d1m.wechat.service.BusinessService;
import com.d1m.wechat.service.MemberProfileService;
import com.d1m.wechat.service.VerifyTokenService;
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

/**
 * Created by D1M on 2017/4/21.
 */
@Slf4j
@Controller
@RequestMapping("/zegna-activity")
public class ZegnaActivityController extends BaseController {
    @Autowired
    private VerifyTokenService verifyTokenService;

    @Autowired
    private MemberProfileService memberProfileService;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private BookOnAppointmentService bookOnAppointmentService;

    @RequestMapping(value = "/send-sms.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject sendMobileCode(
            @RequestBody(required = false) ZegnaModel model,
            HttpSession session, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Member zegnaMember = getZegnaMember(request);
            if (zegnaMember == null) {
                return representation(Message.MEMBER_NOT_LOGIN);
            }
            if (model == null) {
                model = new ZegnaModel();
            }
            VerifyToken verifyToken = verifyTokenService.sendMobileCode(
                    zegnaMember.getWechatId(), zegnaMember.getId(),
                    model.getMobile());
            log.info("start send.");
            ZegnaSMSUtil.sendMsg(verifyToken.getAccept(), verifyToken.getCode());
            return representation(Message.VERIFY_TOKEN_SEND_SUCCESS, verifyToken.getCode());
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }

    @RequestMapping(value = "/bind.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject bind(
            @RequestBody(required = false) ZegnaModel model,
            HttpSession session, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Member zegnaMember = getZegnaMember(request);
            if (zegnaMember == null) {
                return representation(Message.MEMBER_NOT_LOGIN);
            }
            if (model == null) {
                model = new ZegnaModel();
            }
            model.setMemberId(zegnaMember.getId());
            model.setWechatId(zegnaMember.getWechatId());
            model.setOpenId(zegnaMember.getOpenId());

            memberProfileService.bind(model);
            return representation(Message.MEMBER_PROFILE_BIND_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }

    @RequestMapping(value = "/isBind.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject isBind(
            HttpSession session, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Member zegnaMember = getZegnaMember(request);
            if (zegnaMember == null) {
                return representation(Message.MEMBER_NOT_LOGIN);
            }
            MemberProfile memberProfile = memberProfileService.getByMemberId(zegnaMember.getWechatId(), zegnaMember.getId());
            if(memberProfile == null) {
                return representation(Message.MEMBER_PROFILE_GET_SUCCESS, "0");
            }
            if(bookOnAppointmentService.isBooked(zegnaMember.getId())) {
                return representation(Message.MEMBER_PROFILE_GET_SUCCESS, "2");
            }
            return representation(Message.MEMBER_PROFILE_GET_SUCCESS, "1");
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }

    @RequestMapping(value = "/getSource.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getSource(
            HttpSession session, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            return representation(Message.MEMBER_PROFILE_GET_SUCCESS, MemberProfileSource.getAllSource());
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }

    @RequestMapping(value = "/getStore.json", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject getStore(
            HttpSession session, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            return representation(Message.MEMBER_PROFILE_GET_SUCCESS, businessService.getAllGroupedBusiness());
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }


    @RequestMapping(value = "/bookOnAppointment.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject bookOnAppointment(
            @RequestBody(required = false) ZegnaModel model,
            HttpSession session, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Member zegnaMember = getZegnaMember(request);
            if (zegnaMember == null) {
                return representation(Message.MEMBER_NOT_LOGIN);
            }
            if (model == null) {
                model = new ZegnaModel();
            }
            model.setMemberId(zegnaMember.getId());
            model.setWechatId(zegnaMember.getWechatId());
            model.setOpenId(zegnaMember.getOpenId());

            bookOnAppointmentService.book(model);
            return representation(Message.MEMBER_PROFILE_BIND_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }


    @RequestMapping(value = "/logUser.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject logUser(@RequestBody(required = false) ZegnaModel model,
            HttpSession session, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Member zegnaMember = getZegnaMember(request);
            if (zegnaMember == null) {
                return representation(Message.MEMBER_NOT_LOGIN);
            }
            model.setMemberId(zegnaMember.getId());
            bookOnAppointmentService.logMember(model);
            return representation(Message.MEMBER_LOG_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }
}
