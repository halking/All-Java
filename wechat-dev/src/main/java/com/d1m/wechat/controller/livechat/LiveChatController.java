package com.d1m.wechat.controller.livechat;

import com.d1m.wechat.common.response.BaseResponse;
import com.d1m.wechat.controller.api.ApiController;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.service.ConfigService;
import com.d1m.wechat.service.LiveChatService;
import com.d1m.wechat.service.MemberService;
import com.d1m.wechat.service.WechatService;
import com.d1m.wechat.util.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by Lisa on 2017/11/7.
 */

@Controller
@RequestMapping("/liveChat")
@Slf4j
public class LiveChatController  extends ApiController {

    @Value("${arvato.wechatid}")
    private int arvatoWechatId;

    @Autowired
    private LiveChatService liveChatService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ConfigService configService;

    @Resource
    private WechatService wechatService;

    @RequestMapping(value = "/getProfile", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse GetProfile(@RequestParam String openId){
        try{
            Member member = memberService.getMemberByOpenId(arvatoWechatId, openId);
            if(member != null) {
                return ResponseUtils.getSuccessReponse(liveChatService.getUserProfileDto(member));
            }else{
                return ResponseUtils.getErrorReponse(HttpStatus.SC_NOT_FOUND, "User profile not exist");
            }
        }catch (Exception e){
            log.error("Failed to get user profile");
            log.error(e.getMessage());
            return ResponseUtils.getErrorReponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @RequestMapping(value = "/getPublicAccessToken", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse getPublicAccessToken() {
        try {
            String secret = configService.getConfigValue(arvatoWechatId, "ARVATO", "SECRECT");
            return ResponseUtils.getSuccessReponse(liveChatService.getWechatAccessToken(arvatoWechatId,secret));
        } catch (Exception e){
            log.error(e.getMessage(),e);
            return ResponseUtils.getErrorReponse(HttpStatus.SC_INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }

    @RequestMapping(value = "/closeSession", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse close(@RequestParam String openId) {
        try {
            liveChatService.closeSessionByMemberOpenId(openId, arvatoWechatId);
            return ResponseUtils.getSuccessReponse("Session closed successfully");
        } catch (NotFoundException e){
            log.error(e.getMessage(),e);
            return ResponseUtils.getErrorReponse(HttpStatus.SC_BAD_REQUEST,e.getMessage());
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return ResponseUtils.getErrorReponse(HttpStatus.SC_INTERNAL_SERVER_ERROR,e.getMessage());
        }
    }
}
