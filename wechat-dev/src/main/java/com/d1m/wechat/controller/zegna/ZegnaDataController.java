package com.d1m.wechat.controller.zegna;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.controller.BaseController;
import com.d1m.wechat.mapper.MemberMapper;
import com.d1m.wechat.mapper.MemberProfileMapper;
import com.d1m.wechat.mapper.ZegnaHistoryMemberMapper;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.model.MemberProfile;
import com.d1m.wechat.model.ZegnaHistoryMember;
import com.d1m.wechat.service.ConfigService;
import com.d1m.wechat.service.MemberProfileService;
import com.d1m.wechat.util.DateUtil;
import com.d1m.wechat.util.ExcelUtil;
import com.d1m.wechat.util.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Controller
@RequestMapping(value = "/zegna-binding")
public class ZegnaDataController extends BaseController {

    @Autowired
    protected ConfigService configService;


    @Autowired
    private MemberProfileService memberProfileService;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MemberProfileMapper memberProfileMapper;

    @Autowired
    private ZegnaHistoryMemberMapper zegnaHistoryMemberMapper;

    @RequestMapping(value = "/data.json", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject importHistorydata(@RequestParam(value = "excelFile") MultipartFile excelFile,
                                        HttpSession session, HttpServletRequest request,
                                        HttpServletResponse response) {
        InputStream in = null;
        try {

            List<List<Object>> listob = null;
            if (excelFile.isEmpty()) {
                throw new Exception("文件不存在！");
            }
            in = excelFile.getInputStream();
            listob = ExcelUtil.getBankListByExcel(in, excelFile.getOriginalFilename());
            for (int i = 0; i < listob.size(); i++) {
                List<Object> lo = listob.get(i);
                log.info("打印信息-->userinfo : "+ JSON.toJSONString(lo));
                String openId = String.valueOf(lo.get(7));
                Integer wechatId = Integer.valueOf(String.valueOf(lo.get(8)));
                String mobile = String.valueOf(lo.get(6));
                int memberId = -1;
                Member existMember = memberMapper.getMemberByOpenId(openId, wechatId);
                if (existMember != null) {
                    existMember.setMobile(mobile);
                    existMember.setRemark("Zegna历史绑定数据");
                    existMember.setSource("History");
                    memberMapper.updateByPrimaryKey(existMember);
                } else {
                    Member newmember = new Member();
                    MemberProfile memberProfile = new MemberProfile();
                    newmember.setOpenId(String.valueOf(lo.get(7)));
                    newmember.setMobile(String.valueOf(lo.get(6)));
                    newmember.setCreatedAt(new Date());
                    newmember.setIsSubscribe(Boolean.TRUE);
                    newmember.setRemark("Zegna历史绑定数据");
                    newmember.setWechatId(wechatId);
                    newmember.setSource("History");
                    memberMapper.insert(newmember);
                }
                Member intoMember = memberMapper.getMemberByOpenIdAndMobile(openId,mobile);
                memberId = intoMember.getId();
                MemberProfile existProfile = memberProfileService.getByMemberId(wechatId, memberId);
                if (existProfile != null) {
                    existProfile = createMemberProfile(existProfile, lo, wechatId, memberId);
                    memberProfileService.updateAll(existProfile);
                } else {
                    MemberProfile memberProfile = new MemberProfile();
                    memberProfile = createMemberProfile(memberProfile, lo, wechatId, memberId);
                    memberProfileService.save(memberProfile);
                }
            }
            return representation(Message.MEMBER_PROFILE_GET_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (Exception e) {
                log.error(e.getMessage());
                return wrapException(e);
            }
        }
    }

    @RequestMapping(value = "/no-openid.json", method = RequestMethod.POST)
    @ResponseBody


    public JSONObject importVipdata(
            @RequestParam(value = "excelFile") MultipartFile excelFile,
            HttpSession session, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            List<Member> members = new ArrayList<Member>();
            List<MemberProfile> profiles = new ArrayList<MemberProfile>();
            System.out.println("通过传统方式form表单提交方式导入excel文件！");

            InputStream in = null;
            List<List<Object>> listob = null;
            if (excelFile.isEmpty()) {
                throw new Exception("文件不存在！");
            }
            in = excelFile.getInputStream();
            listob = ExcelUtil.getBankListByExcel(in, excelFile.getOriginalFilename());
            in.close();
            //该处可调用service相应方法进行数据保存到数据库中，现只对数据输出
            for (int i = 0; i < listob.size(); i++) {
                List<Object> lo = listob.get(i);
                log.info("打印信息-->userinfo : "+ JSON.toJSONString(lo));
                String mobile = String.valueOf(lo.get(6));
                ZegnaHistoryMember existHistoryMember = zegnaHistoryMemberMapper.getHistoryMemberByMobile(mobile);
                if (existHistoryMember!=null){
                    existHistoryMember.setUpdateDate(new Date());
                    existHistoryMember = createHistoryMember(existHistoryMember,lo);
                    zegnaHistoryMemberMapper.updateByPrimaryKey(existHistoryMember);
                } else {
                    ZegnaHistoryMember newHistoryMember = new ZegnaHistoryMember();
                    newHistoryMember = createHistoryMember(newHistoryMember,lo);
                    zegnaHistoryMemberMapper.insert(newHistoryMember);
                }
            }
            return representation(Message.MEMBER_PROFILE_GET_SUCCESS);
        } catch (Exception e) {
            log.error(e.getMessage());
            return wrapException(e);
        }
    }

    private MemberProfile createMemberProfile(MemberProfile memberProfile, List<Object> lo, Integer wechatId, Integer memberId) {
        memberProfile.setBindAt(new Date());
        memberProfile.setStatus((byte) 2);
        memberProfile.setVip(Boolean.FALSE);
        memberProfile.setWechatId(wechatId);
        memberProfile.setEmail(String.valueOf(lo.get(3)));
        memberProfile.setBirthDate(DateUtil.parse(String.valueOf(lo.get(1))));
        memberProfile.setMemberId(memberId);
        memberProfile.setName(String.valueOf(lo.get(4)) + String.valueOf(lo.get(5)));
        String sex = String.valueOf(lo.get(0));
        if (sex.equalsIgnoreCase("Male")) {
            memberProfile.setSex((byte) 0);
        } else {
            memberProfile.setSex((byte) 1);
        }
        return memberProfile;
    }

    private ZegnaHistoryMember createHistoryMember(ZegnaHistoryMember historyMember, List<Object> lo){
        historyMember.setMobile(String.valueOf(lo.get(6)));
        historyMember.setCreateDate(new Date());
        historyMember.setBirthDate(DateUtil.parse(String.valueOf(lo.get(1))));
        historyMember.setEmail(String.valueOf(lo.get(3)));
        String sex = String.valueOf(lo.get(0));
        historyMember.setName(String.valueOf(lo.get(4)) + String.valueOf(lo.get(5)));
        if (sex.equalsIgnoreCase("Male")) {
            historyMember.setSex((byte) 0);
        } else {
            historyMember.setSex((byte) 1);
        }
        return historyMember;
    }


}
