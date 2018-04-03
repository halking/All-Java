package com.d1m.wechat.wxsdk.report;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d1m.wechat.exception.WechatException;
import com.d1m.wechat.wxsdk.core.req.WeiXinReqService;
import com.d1m.wechat.wxsdk.core.req.model.report.ArticleReadHour;
import com.d1m.wechat.wxsdk.core.req.model.report.ArticleSummary;
import com.d1m.wechat.wxsdk.core.req.model.report.UserCumulate;
import com.d1m.wechat.wxsdk.core.req.model.report.UserSummary;
import com.d1m.wechat.wxsdk.report.model.ArticleAnalysis;
import com.d1m.wechat.wxsdk.report.model.UserAnalysis;

public class JwReportAPI {
	private static Logger log = LoggerFactory.getLogger(JwReportAPI.class);
	
	/**
	 * 获取用户增减数据
	 * 
	 * @return
	 * @throws WechatException
	 */
	public static List<UserAnalysis> getUserSummary(String accesstoken, String beginDate, String endDate) throws WechatException {
		List<UserAnalysis> list = new ArrayList<UserAnalysis>();
		if (accesstoken != null) {
			UserSummary userSummary = new UserSummary();
			userSummary.setAccess_token(accesstoken);
			userSummary.setBegin_date(beginDate);
			userSummary.setEnd_date(endDate);
			JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(userSummary);
			log.info("getUserSummary return:{}",result.toString());
			if(result.containsKey("list")) {
				JSONArray array = result.getJSONArray("list");
				for (int i = 0; i < array.size(); i++) {
					JSONObject obj = array.getJSONObject(i);
					UserAnalysis ua = new UserAnalysis();
					ua.setRef_date(obj.getString("ref_date"));
					ua.setUser_source(obj.getInt("user_source"));
					ua.setNew_user(obj.getInt("new_user"));
					ua.setCancel_user(obj.getInt("cancel_user"));
					list.add(ua);
				}
			}
		}
		return list;
	}
	
	/**
	 * 获取累计用户数据
	 * 
	 * @return
	 * @throws WechatException
	 */
	public static List<UserAnalysis> getUserCumulate(String accesstoken, String beginDate, String endDate) throws WechatException {
		List<UserAnalysis> list = new ArrayList<UserAnalysis>();
		if (accesstoken != null) {
			UserCumulate userCumulate = new UserCumulate();
			userCumulate.setAccess_token(accesstoken);
			userCumulate.setBegin_date(beginDate);
			userCumulate.setEnd_date(endDate);
			JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(userCumulate);
			log.info("getUserCumulate return:{}",result.toString());
			if(result.containsKey("list")) {
				JSONArray array = result.getJSONArray("list");
				for (int i = 0; i < array.size(); i++) {
					JSONObject obj = array.getJSONObject(i);
					UserAnalysis ua = new UserAnalysis();
					ua.setRef_date(obj.getString("ref_date"));
					ua.setCumulate_user(obj.getInt("cumulate_user"));
					list.add(ua);
				}
			}
		}
		return list;
	}
	
	/**
	 * 获取群发图文报表
	 * 
	 * @param accesstoken
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws WechatException
	 */
	public static List<ArticleAnalysis> getArticleSummary(String accesstoken, String beginDate, String endDate) throws WechatException{
		List<ArticleAnalysis> list = new ArrayList<ArticleAnalysis>();
		if (accesstoken != null){
			ArticleSummary articleSummary = new ArticleSummary();
			articleSummary.setAccess_token(accesstoken);
			articleSummary.setBegin_date(beginDate);
			articleSummary.setEnd_date(endDate);
			JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(articleSummary);
			log.info("getArticleSummary return:{}",result.toString());
			if(result.containsKey("list")) {
				JSONArray array = result.getJSONArray("list");
				for (int i = 0; i < array.size(); i++) {
					JSONObject obj = array.getJSONObject(i);
					ArticleAnalysis aa = new ArticleAnalysis();
					aa.setRefDate(obj.getString("ref_date"));
					aa.setMsgid(obj.getString("msgid"));
					aa.setTitle(obj.getString("title"));
					aa.setPageUser(obj.getInt("int_page_read_user"));
					aa.setPageCount(obj.getInt("int_page_read_count"));
					aa.setOriPageUser(obj.getInt("ori_page_read_user"));
					aa.setOriPageCount(obj.getInt("ori_page_read_count"));
					aa.setShareUser(obj.getInt("share_user"));
					aa.setShareCount(obj.getInt("share_count"));
					aa.setAddFavUser(obj.getInt("add_to_fav_user"));
					aa.setAddFavCount(obj.getInt("add_to_fav_count"));
					list.add(aa);
				}
			}
		}
		return list;
	}
	
	/**
	 * 获取群发图文分时报表
	 * 
	 * @param accesstoken
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws WechatException
	 */
	public static List<ArticleAnalysis> getArticleReadHour(String accesstoken, String beginDate, String endDate) throws WechatException{
		List<ArticleAnalysis> list = new ArrayList<ArticleAnalysis>();
		if (accesstoken != null){
			ArticleReadHour articleReadHour = new ArticleReadHour();
			articleReadHour.setAccess_token(accesstoken);
			articleReadHour.setBegin_date(beginDate);
			articleReadHour.setEnd_date(endDate);
			JSONObject result = WeiXinReqService.getInstance().doWeinxinReqJson(articleReadHour);
			log.info("getArticleReadHour return:{}",result.toString());
			if(result.containsKey("list")) {
				JSONArray array = result.getJSONArray("list");
				for (int i = 0; i < array.size(); i++) {
					JSONObject obj = array.getJSONObject(i);
					ArticleAnalysis aa = new ArticleAnalysis();
					aa.setRefDate(obj.getString("ref_date"));
					aa.setRefHour(obj.getString("ref_hour"));
					aa.setUserSource(obj.getInt("user_source"));
					aa.setPageUser(obj.getInt("int_page_read_user"));
					aa.setPageCount(obj.getInt("int_page_read_count"));
					aa.setOriPageUser(obj.getInt("ori_page_read_user"));
					aa.setOriPageCount(obj.getInt("ori_page_read_count"));
					aa.setShareUser(obj.getInt("share_user"));
					aa.setShareCount(obj.getInt("share_count"));
					aa.setAddFavUser(obj.getInt("add_to_fav_user"));
					aa.setAddFavCount(obj.getInt("add_to_fav_count"));
					list.add(aa);
				}
			}
		}
		return list;
	}
}
