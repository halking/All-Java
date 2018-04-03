package com.d1m.wechat.service.impl;

import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.mapper.MemberMapper;
import com.d1m.wechat.mapper.MemberProfileMapper;
import com.d1m.wechat.mapper.TaskCategoryMapper;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.model.MemberProfile;
import com.d1m.wechat.model.enums.MemberProfileStatus;
import com.d1m.wechat.pamametermodel.ZegnaModel;
import com.d1m.wechat.service.*;
import com.d1m.wechat.util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import static com.d1m.wechat.util.IllegalArgumentUtil.notBlank;

@Service
public class MemberProfileServiceImpl extends BaseService<MemberProfile>
		implements MemberProfileService {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private MemberProfileMapper memberProfileMapper;

	@Autowired
	private MemberMapper memberMapper;

	@Autowired
	private VerifyTokenService verifyTokenService;

	@Autowired
	private MemberService memberService;

	@Autowired
	private AreaInfoService areaInfoService;

	@Autowired
	private TaskCategoryMapper taskCategoryMapper;

	@Autowired
	private TaskService taskService;

	@Autowired
	private QrcodeService qrcodeService;

	@Override
	public Mapper<MemberProfile> getGenericMapper() {
		return memberProfileMapper;
	}

	@Override
	public MemberProfile getByMemberId(Integer wechatId, Integer memberId) {
		return memberProfileMapper.getByMemberId(wechatId, memberId);
	}

	@Override
	public Integer getMemberBindStatus(Integer id, Integer wechatId) {
		MemberProfile record = new MemberProfile();
		record.setMemberId(id);
		record.setWechatId(wechatId);
		Integer resultCode = null;
		int count = memberProfileMapper.selectCount(record);
		if (count == 0) {
			resultCode = null;
		} else {
			resultCode = memberProfileMapper.getMemberBindStatus(id, wechatId);
		}
		return resultCode;
	}

	@Override
	public MemberProfile bind(ZegnaModel bindModel) {
		if(bindModel.getXing() == null || bindModel.getXing().length() == 0){
			throw new WechatException(Message.MEMBER_PROFILE_XING_NOT_BLANK);
		}

		if(bindModel.getMing() == null || bindModel.getMing().length() == 0){
			throw new WechatException(Message.MEMBER_PROFILE_MING_NOT_BLANK);
		}

		if(bindModel.getEmail() == null || bindModel.getEmail().length() == 0){
			throw new WechatException(Message.MEMBER_PROFILE_EMAIL_NOT_BLANK);
		}

//		verifyTokenService.verify(bindModel.getWechatId(),
//				bindModel.getMemberId(), bindModel.getMobile(),
//				bindModel.getCode());
		Member member = memberService.getMember(
				bindModel.getWechatId(),
				bindModel.getMemberId());
		member.setMobile(bindModel.getMobile());
		memberService.updateNotNull(member);

		MemberProfile memberProfile = getByMemberId(member.getWechatId(),
				member.getId());
		if (memberProfile == null) {
			memberProfile = new MemberProfile();
			memberProfile.setMemberId(member.getId());
			memberProfile.setStatus(MemberProfileStatus.BIND.getValue());
			memberProfile.setWechatId(member.getWechatId());
			memberProfile.setEmail(bindModel.getEmail());
			memberProfile.setXing(bindModel.getXing());
			memberProfile.setMing(bindModel.getMing());
			memberProfile.setSourceid(bindModel.getSourceId());
			memberProfileMapper.insert(memberProfile);
		}

		return memberProfileMapper.getByMemberId(bindModel.getWechatId(), bindModel.getMemberId());
	}


}
