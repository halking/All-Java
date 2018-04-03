package com.d1m.wechat.schedule.job;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.d1m.wechat.mapper.ReportArticleHourSourceMapper;
import com.d1m.wechat.mapper.ReportArticleSourceMapper;
import com.d1m.wechat.model.ReportArticleHourSource;
import com.d1m.wechat.model.ReportArticleSource;
import com.d1m.wechat.model.Wechat;
import com.d1m.wechat.schedule.BaseJob;
import com.d1m.wechat.schedule.ExecResult;
import com.d1m.wechat.service.ReportArticleHourSourceService;
import com.d1m.wechat.service.ReportArticleSourceService;
import com.d1m.wechat.service.WechatService;
import com.d1m.wechat.service.impl.RefreshAccessTokenJob;
import com.d1m.wechat.util.DateUtil;
import com.d1m.wechat.wxsdk.report.JwReportAPI;
import com.d1m.wechat.wxsdk.report.model.ArticleAnalysis;

@Component
public class FetchArticleSourceReportJob extends BaseJob{
	private Logger log = LoggerFactory.getLogger(FetchArticleSourceReportJob.class);

	@Autowired
	private ReportArticleSourceService reportService;

	@Autowired
	private ReportArticleHourSourceService reportHourService;

	@Autowired
	private WechatService wechatService;

	@Autowired
	private ReportArticleSourceMapper reportArticleSourceMapper;

	@Autowired
	private ReportArticleHourSourceMapper reportArticleHourSourceMapper;

	@Override
	public ExecResult run(Map<String, String> params) {
		ExecResult ret = new ExecResult();
		try {
			List<Wechat> list = wechatService.getWechatList();
			for (Wechat wechat:list) {
				for(int i=-1; i>-6; i--){
					String date = DateUtil.getDate(i);
					saveReportArticleSource(wechat.getId(), date);
					saveReportArticleHourSource(wechat.getId(), date);
				}
			}
			ret.setStatus(true);

		} catch (Exception e) {
			ret.setStatus(false);
			ret.setMsg(e.getMessage());
			log.error("获取群发图文报表失败：" + e.getMessage());
		}
		return ret;
	}

	private void saveReportArticleSource(Integer wechatId, String date) {
		String accessToken = RefreshAccessTokenJob.getAccessTokenStr(wechatId);
		List<ArticleAnalysis> articleList = JwReportAPI.getArticleSummary(accessToken, date, date);
		for (ArticleAnalysis aa : articleList){
			ReportArticleSource record = new ReportArticleSource();
			record.setWechatId(wechatId);
			record.setMsgid(aa.getMsgid());
			record.setRefDate(aa.getRefDate());
			List<ReportArticleSource> list = reportArticleSourceMapper.select(record);
			if(list!=null && list.size()>0){
				record = list.get(0);
			}else {
				record = null;
			}
			if(record != null){
				record.setTitle(aa.getTitle());
				record.setCreatedAt(DateUtil.parse(date));
				record.setPageUser(aa.getPageUser());
				record.setPageCount(aa.getPageCount());
				record.setOriPageUser(aa.getOriPageUser());
				record.setOriPageCount(aa.getOriPageCount());
				record.setShareUser(aa.getShareUser());
				record.setShareCount(aa.getShareCount());
				record.setAddFavUser(aa.getAddFavUser());
				record.setAddFavCount(aa.getAddFavCount());
				reportService.updateNotNull(record);
			}else{
				ReportArticleSource ras = new ReportArticleSource();
				ras.setWechatId(wechatId);
				ras.setCreatedAt(DateUtil.parse(date));
				ras.setRefDate(aa.getRefDate());
				ras.setMsgid(aa.getMsgid());
				ras.setTitle(aa.getTitle());
				ras.setPageUser(aa.getPageUser());
				ras.setPageCount(aa.getPageCount());
				ras.setOriPageUser(aa.getOriPageUser());
				ras.setOriPageCount(aa.getOriPageCount());
				ras.setShareUser(aa.getShareUser());
				ras.setShareCount(aa.getShareCount());
				ras.setAddFavUser(aa.getAddFavUser());
				ras.setAddFavCount(aa.getAddFavCount());
				reportService.save(ras);
			}
		}
	}

	private void saveReportArticleHourSource(Integer wechatId, String date) {
		String accessToken = RefreshAccessTokenJob.getAccessTokenStr(wechatId);
		List<ArticleAnalysis> articleHourList = JwReportAPI.getArticleReadHour(accessToken, date, date);
		for (ArticleAnalysis aa : articleHourList){
			ReportArticleHourSource record = new ReportArticleHourSource();
			record.setWechatId(wechatId);
			record.setRefDate(aa.getRefDate());
			record.setRefHour(aa.getRefHour());
			List<ReportArticleHourSource> list = reportArticleHourSourceMapper.select(record);
			if(list!=null && list.size()>0){
				record = list.get(0);
			}else {
				record = null;
			}
			if(record != null){
				record.setCreatedAt(DateUtil.parse(date));
				record.setUserSource(aa.getUserSource());
				record.setPageUser(aa.getPageUser());
				record.setPageCount(aa.getPageCount());
				record.setOriPageUser(aa.getOriPageUser());
				record.setOriPageCount(aa.getOriPageCount());
				record.setShareUser(aa.getShareUser());
				record.setShareCount(aa.getShareCount());
				record.setAddFavUser(aa.getAddFavUser());
				record.setAddFavCount(aa.getAddFavCount());
				reportHourService.updateNotNull(record);
			}else{
				ReportArticleHourSource rahs = new ReportArticleHourSource();
				rahs.setWechatId(wechatId);
				rahs.setCreatedAt(DateUtil.parse(date));
				rahs.setRefDate(aa.getRefDate());
				rahs.setRefHour(aa.getRefHour());
				rahs.setUserSource(aa.getUserSource());
				rahs.setPageUser(aa.getPageUser());
				rahs.setPageCount(aa.getPageCount());
				rahs.setOriPageUser(aa.getOriPageUser());
				rahs.setOriPageCount(aa.getOriPageCount());
				rahs.setShareUser(aa.getShareUser());
				rahs.setShareCount(aa.getShareCount());
				rahs.setAddFavUser(aa.getAddFavUser());
				rahs.setAddFavCount(aa.getAddFavCount());
				reportHourService.save(rahs);
			}

		}

	}

}
