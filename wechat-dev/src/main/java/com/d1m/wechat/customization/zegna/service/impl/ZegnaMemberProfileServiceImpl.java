package com.d1m.wechat.customization.zegna.service.impl;

import java.util.Date;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import com.d1m.wechat.customization.zegna.model.ZegnaMemberProfileModel;
import com.d1m.wechat.customization.zegna.service.ZegnaMemberProfileService;
import com.d1m.wechat.customization.zegna.mapper.ZegnaMemberProfileMapper;
import com.d1m.wechat.customization.zegna.model.ZegnaMemberProfile;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.model.enums.MemberProfileStatus;
import com.d1m.wechat.service.MemberService;
import com.d1m.wechat.service.impl.BaseService;
import com.d1m.wechat.util.IllegalArgumentUtil;
import com.d1m.wechat.util.Message;

/**
 * ZegnaMemberProfileServiceImpl
 *
 * @author f0rb on 2017-02-10.
 */
@Slf4j
@Service
public class ZegnaMemberProfileServiceImpl extends BaseService<ZegnaMemberProfile> implements ZegnaMemberProfileService {
    @Resource
    private ZegnaMemberProfileMapper zegnaMemberProfileMapper;

    @Resource
    private MemberService memberService;

    @Override
    public Mapper<ZegnaMemberProfile> getGenericMapper() {
        return zegnaMemberProfileMapper;
    }

    @Override
    public void bind(ZegnaMemberProfileModel model) {
        IllegalArgumentUtil.notBlank(model.getMobile(), Message.VERIFY_TOKEN_MOBILE_NOT_BLANK);
        //IllegalArgumentUtil.notBlank(model.getCaptcha(), Message.VERIFY_TOKEN_CODE_NOT_BLANK);
        log.info("openId : {}", model.getOpenId());

        Member member = memberService.getMember(model.getWechatId(), model.getMemberId());
        member.setMobile(model.getMobile());
        memberService.updateNotNull(member);

        ZegnaMemberProfile memberProfile = new ZegnaMemberProfile();
        memberProfile.setFirstname(model.getFirstname());
        memberProfile.setLastname(model.getLastname());
        memberProfile.setMobile(model.getMobile());
        memberProfile.setEmail(model.getEmail());

        memberProfile.setMemberId(member.getId());
        memberProfile.setWechatId(member.getWechatId());
        memberProfile.setStatus(MemberProfileStatus.BIND.getValue());

        memberProfile.setBindAt(new Date());
        zegnaMemberProfileMapper.insert(memberProfile);
    }



    /*@Override
    public MemberProfileDto getMemberProfile(Member member) {
        if(StringUtils.isBlank(member.getMobile())){
            throw new WechatException(Message.MEMBER_NOT_LOGIN);
        }
        MemberProfile memberProfile =  memberProfileMapper.getByMemberId(member.getWechatId(), member.getId());
        logger.info("memberProfile : {}", (memberProfile != null ? memberProfile.getId() : ""));
        if(memberProfile == null || memberProfile.getStatus() == 0){
            throw new WechatException(Message.MEMBER_NOT_LOGIN);
        }
        MemberProfileDto dto = new MemberProfileDto();
        dto.setName(memberProfile.getName());
        dto.setMobile(member.getMobile());
        dto.setProvince(memberProfile.getProvince());
        dto.setCity(memberProfile.getCity());
        dto.setAddress(memberProfile.getAddress());
        dto.setBirthDate(memberProfile.getBirthDate());
        if(memberProfile.getBirthDate()!=null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(memberProfile.getBirthDate());
            dto.setYear(cal.get(Calendar.YEAR));
            dto.setMonth(cal.get(Calendar.MONTH));
        }
        dto.setBrand(memberProfile.getBrand());
        dto.setMonthlyIncome(memberProfile.getMonthlyIncome());
        dto.setModel(memberProfile.getModel());
        dto.setContactChannel(memberProfile.getContactChannel());
        dto.setPurchaseChannel(memberProfile.getPurchaseChannel());

        List<MemberAddressDto> addressModelList = memberAddressService.select(member.getWechatId(), member.getId());
        dto.setAddresses(addressModelList);
        return dto;

    }

    @Override
    public void update(RegisterModel memberProfileModel) {
        Integer wechatId = memberProfileModel.getWechatId();
        Integer memberId = memberProfileModel.getMemberId();
        Member member = memberService.getMember(wechatId, memberId);
        member.setMobile(memberProfileModel.getMobile());

        MemberProfile memberProfile = checkExistProfile(memberProfileModel);
        memberProfile.setProvince(memberProfileModel.getProvince());
        memberProfile.setCity(memberProfileModel.getCity());
        memberProfile.setAddress(memberProfileModel.getAddress());
        memberProfile.setBrand(memberProfileModel.getBrand());
        memberProfile.setModel(memberProfileModel.getModel());
        // 先更新本地
        memberProfileMapper.updateByPrimaryKeySelective(memberProfile);
        memberService.updateNotNull(member);
        memberAddressService.saveAddresses(member.getWechatId(), member.getId(), memberProfileModel.getAddresses());
    }

    private MemberProfile checkExistProfile(RegisterModel memberProfileModel) {
        MemberProfile memberProfile = getByMemberId(memberProfileModel.getWechatId(), memberProfileModel.getMemberId());
        if (memberProfile == null) {
            throw new WechatException(Message.MEMBER_PROFILE_NOT_BIND);
        }
        return memberProfile;
    }*/
}
