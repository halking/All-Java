package com.d1m.wechat.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.util.Message;

/**
 * 扩展shiro针对ajax请求返回的封装
 * @author d1m
 */
public class AuthcAuthenticationFilter extends AuthorizationFilter {

	/**
	 * 有权限处理
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object obj) throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest) request;  
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Subject subject = getSubject(request, response);

        if (subject.getPrincipal()!=null) {
            return true;
        } else {
            if (isAjax(httpRequest)) {
            	JSONObject json = new JSONObject();
    			json.put("resultCode", Message.NOT_LOGGED_IN.getCode());
    			json.put("msg", Message.NOT_LOGGED_IN.name());
    			PrintWriter out = httpResponse.getWriter();
    			out.print(json);
    			out.flush();
				out.close();
            } else {
            	saveRequestAndRedirectToLogin(request, response);
            }
        }
        return false;
	}

	/**
	 * 无权限处理
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
			throws IOException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;  
        HttpServletResponse httpResponse = (HttpServletResponse) response;  
  
        Subject subject = getSubject(request, response);  
  
        if (subject.getPrincipal() == null) {
            if (isAjax(httpRequest)) {  
            	JSONObject json = new JSONObject();
    			json.put("resultCode", Message.NOT_LOGGED_IN.getCode());
    			json.put("msg", Message.NOT_LOGGED_IN.name());
    			PrintWriter out = httpResponse.getWriter();
    			out.print(json);
    			out.flush();
				out.close();
            } else {  
                saveRequestAndRedirectToLogin(request, response);  
            }  
        } else {
            if (isAjax(httpRequest)) {  
            	JSONObject json = new JSONObject();
    			json.put("resultCode", Message.NO_PERMISSION.getCode());
    			json.put("msg", Message.NO_PERMISSION.name());
    			PrintWriter out = httpResponse.getWriter();
    			out.print(json);
    			out.flush();
				out.close();
            } else {  
                String unauthorizedUrl = getUnauthorizedUrl();  
                if (StringUtils.hasText(unauthorizedUrl)) {  
                    WebUtils.issueRedirect(request, response, unauthorizedUrl);  
                } else {  
                    WebUtils.toHttp(response).sendError(401);  
                }  
            }  
        }  
        return false; 
	}

	/**
	 * 判断ajax请求
	 * @param request
	 * @return
	 */
	private boolean isAjax(HttpServletRequest request){
		return true;
	    //return  (request.getHeader("X-Requested-With") != null  && "XMLHttpRequest".equals( request.getHeader("X-Requested-With").toString())   ) ;
	}
}
