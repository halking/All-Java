package com.d1m.wechat.filter;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.controller.user.UserController;
import com.d1m.wechat.model.LoginToken;
import com.d1m.wechat.model.User;
import com.d1m.wechat.service.LoginTokenService;
import com.d1m.wechat.service.UserService;
import com.d1m.wechat.util.AppContextUtils;
import com.d1m.wechat.util.Constants;
import com.d1m.wechat.util.DateUtil;
import com.d1m.wechat.util.Message;

public class VerifyFilter implements Filter {

	private static Logger log = LoggerFactory.getLogger(VerifyFilter.class);

	public static final String staticResources = "jpg,png,bmp,css";

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String requestURI = request.getRequestURI();
		if (StringUtils.contains(requestURI, Constants.LOGIN_URL)
				|| StringUtils.contains(requestURI, Constants.WX_NOTIFY)
				|| StringUtils.contains(requestURI, Constants.WS_SOCKET)) {
			chain.doFilter(request, response);
			return;
		}
		boolean isStaticResource = false;
		if (StringUtils.contains(requestURI, ".")) {
			String suffix = requestURI.substring(
					requestURI.lastIndexOf(".") + 1).toLowerCase();
			isStaticResource = StringUtils.contains(staticResources, suffix);
		}

		if (isStaticResource) {
			chain.doFilter(request, response);
		} else {
			String token = UserController.getCookie(request, Constants.TOKEN);
			log.info("token : {}", token);
			if (StringUtils.isNotBlank(token)) {
				LoginTokenService loginTokenService = AppContextUtils
						.getBean(LoginTokenService.class);
				LoginToken loginToken = loginTokenService.getByToken(token);
				log.info("login token : {}", loginToken);
				if (loginToken != null) {
					UserService userService = AppContextUtils
							.getBean(UserService.class);
					User user = userService.selectByKey(loginToken.getUserId());
					Date current = new Date();
					boolean effective = loginToken.getExpiredAt().compareTo(
							current) >= 0;
					log.info("user : {}", user);
					log.info("token effective : {}", effective);
					if (user != null && effective) {
						loginToken.setExpiredAt(DateUtil.changeDate(current,
								Calendar.HOUR_OF_DAY, 8));
						loginTokenService.updateAll(loginToken);

						response.addCookie(UserController.addCookie(
								Constants.TOKEN, token, 60 * 60 * 12));

						log.info("refresh cookie : {}", token);
						HttpSession session = request.getSession();
						session.setAttribute("user", user);
						session.setAttribute("wechatId",
								loginToken.getWechatId());
						chain.doFilter(request, response);
						return;
					} else {
						log.info("remove cookie : {}", token);
						response.addCookie(UserController.addCookie(
								Constants.TOKEN, token, 0));
					}
				}
			}
			JSONObject json = new JSONObject();
			json.put("resultCode", Message.NOT_LOGGED_IN.getCode());
			json.put("msg", Message.NOT_LOGGED_IN.name());
			response.getWriter().print(json);
		}
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
