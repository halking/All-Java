package com.d1m.wechat.schedule.job;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.d1m.wechat.model.Wechat;
import com.d1m.wechat.service.WechatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.d1m.wechat.model.ReportUserSource;
import com.d1m.wechat.schedule.BaseJob;
import com.d1m.wechat.schedule.ExecResult;
import com.d1m.wechat.service.ReportUserSourceService;
import com.d1m.wechat.service.impl.RefreshAccessTokenJob;
import com.d1m.wechat.util.DateUtil;
import com.d1m.wechat.wxsdk.report.JwReportAPI;
import com.d1m.wechat.wxsdk.report.model.UserAnalysis;

/**
 * 定时抓取微信数据来源报表
 * @author d1m
 */
@Component
public class FetchUserSourceReportJob extends BaseJob {
	private Logger log = LoggerFactory.getLogger(FetchUserSourceReportJob.class);
	
	@Autowired
	private ReportUserSourceService reportService;

	@Autowired
	private WechatService wechatService;
	
	@Override
	public ExecResult run(Map<String, String> params) {
		ExecResult ret = new ExecResult();
		try {
			String accessToken = null;
			String beginDate = DateUtil.getDate(-7);
			String endDate = DateUtil.getYestoday();
			List<Wechat> list = wechatService.getWechatList();
			for (Wechat wechat:list) {
				accessToken = RefreshAccessTokenJob.getAccessTokenStr(wechat.getId());
				// 查询用户来源
				List<UserAnalysis> summaryList = JwReportAPI.getUserSummary(accessToken, beginDate, endDate);
				log.info("summaryList size : {}", summaryList.size());
				for(UserAnalysis ua:summaryList){
					ReportUserSource us = reportService.getRecordByDate(wechat.getId(), ua.getRef_date());
					if(us!=null){
						setUserSource(us,ua);
						us.setCreatedAt(new Date());
						reportService.updateAll(us);
					}else{
						us = new ReportUserSource();
						us.setWechatId(wechat.getId());
						us.setDate(ua.getRef_date());
						us.setCreatedAt(new Date());
						setUserSource(us,ua);
						reportService.save(us);
					}
				}
				
				// 查询用户累计
				List<UserAnalysis> cumulateList = JwReportAPI.getUserCumulate(accessToken, beginDate, endDate);
				log.info("cumulateList size : {}", cumulateList.size());
				for(UserAnalysis ua:cumulateList){
					ReportUserSource us = reportService.getRecordByDate(wechat.getId(), ua.getRef_date());
					if(us!=null){
						us.setCumulateUser(ua.getCumulate_user());
						us.setCreatedAt(new Date());
						reportService.updateAll(us);
					}else{
						us = new ReportUserSource();
						us.setWechatId(wechat.getId());
						us.setDate(ua.getRef_date());
						us.setCreatedAt(new Date());
						us.setCumulateUser(ua.getCumulate_user());
						reportService.save(us);
					}
				}
			}
			ret.setStatus(true);
		} catch (Exception e) {
			ret.setStatus(false);
			ret.setMsg(e.getMessage());
			log.error("获取用户来源报表失败："+e.getMessage());
		}
		return ret;
	}
	
	private void setUserSource(ReportUserSource us, UserAnalysis ua){
		int source = ua.getUser_source();
		switch (source) {
			case 0:
				us.setNewUser0(ua.getNew_user());
				us.setCancelUser0(ua.getCancel_user());
				break;
			case 1:
				us.setNewUser1(ua.getNew_user());
				us.setCancelUser1(ua.getCancel_user());
				break;
			case 17:
				us.setNewUser17(ua.getNew_user());
				us.setCancelUser17(ua.getCancel_user());
				break;
			case 30:
				us.setNewUser30(ua.getNew_user());
				us.setCancelUser30(ua.getCancel_user());
				break;
			case 43:
				us.setNewUser43(ua.getNew_user());
				us.setCancelUser43(ua.getCancel_user());
				break;
			case 51:
				us.setNewUser51(ua.getNew_user());
				us.setCancelUser51(ua.getCancel_user());
				break;
			case 57:
				us.setNewUser57(ua.getNew_user());
				us.setCancelUser57(ua.getCancel_user());
				break;
			case 75:
				us.setNewUser75(ua.getNew_user());
				us.setCancelUser75(ua.getCancel_user());
				break;
			case 78:
				us.setNewUser78(ua.getNew_user());
				us.setCancelUser78(ua.getCancel_user());
				break;
			default:
				break;
		}
	}

}
