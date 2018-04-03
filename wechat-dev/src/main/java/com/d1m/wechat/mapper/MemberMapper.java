package com.d1m.wechat.mapper;

import com.d1m.wechat.dto.*;
import com.d1m.wechat.model.Member;
import com.d1m.wechat.util.MyMapper;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface MemberMapper extends MyMapper<Member> {

	Page<MemberDto> search(@Param("wechatId") Integer wechatId,
			@Param("openId") String openId, @Param("nickname") String nickname,
			@Param("sex") Byte sex, @Param("country") Integer country,
			@Param("province") Integer province, @Param("city") Integer city,
			@Param("subscribe") Boolean subscribe,
			@Param("activityStartAt") Integer activityStartAt,
			@Param("activityEndAt") Integer activityEndAt,
			@Param("batchSendOfMonthStartAt") Integer batchSendOfMonthStartAt,
			@Param("batchSendOfMonthEndAt") Integer batchSendOfMonthEndAt,
			@Param("attentionStartAt") Date attentionStartAt,
			@Param("attentionEndAt") Date attentionEndAt,
			@Param("cancelSubscribeStartAt") Date cancelSubscribeStartAt,
			@Param("cancelSubscribeEndAt") Date cancelSubscribeEndAt,
			@Param("isOnline") Boolean isOnline,
			@Param("fromWhere") String fromWhere,
			@Param("mobile") String mobile,
			@Param("memberTags") Integer[] memberTags,
			@Param("sortName") String sortName, @Param("sortDir") String sortDir, 
			@Param("bindStatus") Integer bindStatus);
	
	Page<MemberDto> massMembersSearch(@Param("wechatId") Integer wechatId,
			@Param("openId") String openId, @Param("nickname") String nickname,
			@Param("sex") Byte sex, @Param("country") Integer country,
			@Param("province") Integer province, @Param("city") Integer city,
			@Param("subscribe") Boolean subscribe,
			@Param("activityStartAt") Integer activityStartAt,
			@Param("activityEndAt") Integer activityEndAt,
			@Param("batchSendOfMonthStartAt") Integer batchSendOfMonthStartAt,
			@Param("batchSendOfMonthEndAt") Integer batchSendOfMonthEndAt,
			@Param("attentionStartAt") Date attentionStartAt,
			@Param("attentionEndAt") Date attentionEndAt,
			@Param("cancelSubscribeStartAt") Date cancelSubscribeStartAt,
			@Param("cancelSubscribeEndAt") Date cancelSubscribeEndAt,
			@Param("isOnline") Boolean isOnline,
			@Param("fromWhere") String fromWhere,
			@Param("mobile") String mobile,
			@Param("memberTags") Integer[] memberTags,
			@Param("sortName") String sortName, @Param("sortDir") String sortDir, 
			@Param("bindStatus") Integer bindStatus);

	Long count(@Param("wechatId") Integer wechatId,
						   @Param("openId") String openId, @Param("nickname") String nickname,
						   @Param("sex") Byte sex, @Param("country") Integer country,
						   @Param("province") Integer province, @Param("city") Integer city,
						   @Param("subscribe") Boolean subscribe,
						   @Param("activityStartAt") Integer activityStartAt,
						   @Param("activityEndAt") Integer activityEndAt,
						   @Param("batchSendOfMonthStartAt") Integer batchSendOfMonthStartAt,
						   @Param("batchSendOfMonthEndAt") Integer batchSendOfMonthEndAt,
						   @Param("attentionStartAt") Date attentionStartAt,
						   @Param("attentionEndAt") Date attentionEndAt,
						   @Param("cancelSubscribeStartAt") Date cancelSubscribeStartAt,
						   @Param("cancelSubscribeEndAt") Date cancelSubscribeEndAt,
						   @Param("isOnline") Boolean isOnline,
						   @Param("fromWhere") String fromWhere,
						   @Param("mobile") String mobile,
						   @Param("memberTags") Integer[] memberTags,
						   @Param("sortName") String sortName, @Param("sortDir") String sortDir,
						   @Param("bindStatus") Integer bindStatus);

	List<MemberDto> selectByWechat(@Param("wechatId") Integer wechatId);

	List<MemberDto> selectByMemberId(@Param("ids") Integer[] memberId,
			@Param("wechatId") Integer wechatId, @Param("isForce") Boolean isForce);

	Long countByMemberId(@Param("ids") Integer[] memberId,
									 @Param("wechatId") Integer wechatId, @Param("isForce") Boolean isForce);

	List<MemberTagDto> getMemberMemberTags(@Param("wechatId") Integer wechatId,
			@Param("memberId") Integer memberId);

	List<MemberDto> searchBySql(@Param("wechatId") Integer wechatId,
			@Param("sql") String sql);

	Integer trendReportTotal(@Param("wechatId") Integer wechatId, @Param("end") Date end);

	List<TrendBaseDto> trendReportAttention(
			@Param("wechatId") Integer wechatId, @Param("start") Date start,
			@Param("end") Date end);

	List<TrendBaseDto> trendReportCancel(@Param("wechatId") Integer wechatId,
			@Param("start") Date start, @Param("end") Date end);

	List<PieBaseDto> pieReport(@Param("wechatId") Integer wechatId,
			@Param("start") Date start, @Param("end") Date end,
			@Param("type") String type);

	Page<ReportActivityUserDto> activityUser(
			@Param("wechatId") Integer wechatId, @Param("start") Date start,
			@Param("end") Date end, @Param("top") Integer top);

	List<ReportUserSourceDto> sourceUser(@Param("wechatId") Integer wechatId,
			@Param("start") Date start, @Param("end") Date end);

	List<ReportAreaBaseDto> pieAreaReport(@Param("wechatId") Integer wechatId,
			@Param("start") Date start, @Param("end") Date end,
			@Param("type") String type);

	List<ReportAreaBaseDto> getProvice(@Param("wechatId") Integer wechatId,
			@Param("start") Date start, @Param("end") Date end,
			@Param("country") Integer country);

	List<ReportAreaBaseDto> getCity(@Param("wechatId") Integer wechatId,
			@Param("start") Date start, @Param("end") Date end,
			@Param("country") Integer country,
			@Param("province") Integer province);

	void deleteMemberTag(@Param("wechatId") Integer wechatId,
			@Param("memberId") Integer memberId,
			@Param("memberTagId") Integer memberTagId);

	List<MemberStatusDto> memberStatus(@Param("wechatId") Integer wechatId,
			@Param("end") Date end);

	MemberLevelDto selectMemberProfile(@Param("id") Integer id, @Param("wechatId") Integer wechatId);

	MemberDto selectByOpenId(@Param("openId") String openId, @Param("wechatId") Integer wechatId);

	Integer selectWechatIdByOpenId(@Param("openId") String openId);

	List<Integer> getMemberIdsByOpenIds(@Param("openIdList") List<String> openIdList);

	List<TrendBaseDto> trendReportAttentionTimes(@Param("wechatId") Integer wechatId, @Param("start") Date start,
			@Param("end") Date end);

	List<TrendBaseDto> trendReportCancelTimes(@Param("wechatId") Integer wechatId, @Param("start") Date start,
			@Param("end") Date end);

	void resetMonthSend();

	void updateBatchSendMonth(@Param("idList") List<Integer> idList);

	Member getMemberByOpenIdAndMobile(@Param("openId") String openId, @Param("mobile") String mobile);

	Member getMemberByMobile(@Param("mobile") String mobile);

	Member getMemberByOpenId(@Param("openId") String openId, @Param("wechatId") Integer wechatId);
}
