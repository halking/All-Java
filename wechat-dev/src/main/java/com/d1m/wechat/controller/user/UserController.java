package com.d1m.wechat.controller.user;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.d1m.wechat.controller.BaseController;
import com.d1m.wechat.controller.file.Upload;
import com.d1m.wechat.controller.file.UploadController;
import com.d1m.wechat.dto.FunctionDto;
import com.d1m.wechat.dto.RoleDto;
import com.d1m.wechat.dto.UserDto;
import com.d1m.wechat.dto.WechatDto;
import com.d1m.wechat.model.User;
import com.d1m.wechat.pamametermodel.FunctionModel;
import com.d1m.wechat.pamametermodel.UserModel;
import com.d1m.wechat.service.FunctionService;
import com.d1m.wechat.service.LoginTokenService;
import com.d1m.wechat.service.RoleService;
import com.d1m.wechat.service.UserService;
import com.d1m.wechat.service.WechatService;
import com.d1m.wechat.util.Constants;
import com.d1m.wechat.util.Message;
import com.github.pagehelper.Page;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;

	@Autowired
	private FunctionService functionService;
	
	@Autowired
	private LoginTokenService loginTokenService;
	
	@Autowired
	private WechatService wechatService;

	public static Cookie addCookie(String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);
		cookie.setPath("/");
		return cookie;
	}

	public static String getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length == 0) {
			return null;
		}
		for (Cookie cookie : cookies) {
			if (StringUtils.equals(cookie.getName(), name)) {
				return cookie.getValue();
			}
		}
		return null;
	}

	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject login(@RequestBody(required = false) UserModel userModel,
			HttpServletResponse response, HttpSession session) {
		UsernamePasswordToken token = new UsernamePasswordToken(userModel.getUsername(), DigestUtils.sha256Hex(userModel.getPassword()));  
	    //token.setRememberMe(true);  
	    Subject subject = SecurityUtils.getSubject();  
	    try {  
	        subject.login(token);  
	        if (subject.isAuthenticated()) {
	        	UserDto profile = userService.getProfile(getUser(session));
	    		RoleDto roleDto = roleService.getById(profile.getRoleId(), profile.getCompanyId());
	    		profile.setFunctionIds(roleDto.getFunctionIds());
	    		profile.setFunctionCodes(roleDto.getFunctionCodes());
	    		FunctionModel functionModel = new FunctionModel();
	    		List<FunctionDto> functionDtos = functionService.search(functionModel);
	    		profile.setFunctionDtos(functionDtos);
	        	return representation(Message.USER_LOGIN_SUCCESS,profile);
	        } else {  
	        	return representation(Message.NOT_LOGGED_IN);
	        }  
	    } catch (Exception e) {  
	    	return representation(Message.USER_NAME_OR_PASSWORD_ERROR);
	    }
//		try {
//			User user = userService.login(userModel.getUsername(),
//					userModel.getPassword());
//			LoginToken loginToken = new LoginToken();
//			List<WechatDto> wechats = userService.listVisibleWechat(user);
//			if (!wechats.isEmpty()) {
//				loginToken.setWechatId(wechats.get(0).getId());
//			}
//
//			String supportMultiplePeopleLogin = FileUploadConfigUtil
//					.getInstance().getValue("support_multiple_people_login");
//			supportMultiplePeopleLogin = StringUtils
//					.trim(supportMultiplePeopleLogin);
//			if (!StringUtils.equals(supportMultiplePeopleLogin,
//					Constants.SUPPORT_MULTIPLE_PEOPLE_LOGIN)) {
//				List<LoginToken> existLoginTokens = loginTokenService
//						.getActiveTokens(user);
//				for (LoginToken lt : existLoginTokens) {
//					lt.setExpiredAt(new Date());
//					loginTokenService.updateAll(lt);
//				}
//			}
//
//			Date current = new Date();
//			loginToken.setCreatedAt(current);
//			loginToken.setExpiredAt(DateUtil.changeDate(current,
//					Calendar.HOUR_OF_DAY, 8));
//			String token = DigestUtils.sha256Hex(user.getUsername() + "@"
//					+ System.currentTimeMillis());
//			loginToken.setToken(token);
//			loginToken.setUserId(user.getId());
//			loginTokenService.save(loginToken);
//
//			user.setLastLoginAt(current);
//			userService.updateAll(user);
//
//			response.addCookie(addCookie(Constants.TOKEN, token, 60 * 60 * 12));
//
//			return representation(Message.USER_LOGIN_SUCCESS);
//		} catch (Exception e) {
//			log.error(e.getMessage());
//			return wrapException(e);
//		}
	}

	@RequestMapping(value = "logout.json", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject logout(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
//		String token = getCookie(request, Constants.TOKEN);
//		log.info("token logout : {}", token);
//		if (token != null) {
//			LoginToken loginToken = loginTokenService.getByToken(token);
//			if (loginToken != null) {
//				loginToken.setExpiredAt(new Date());
//				loginTokenService.updateAll(loginToken);
//			}
//			response.addCookie(addCookie(Constants.TOKEN, token, 0));
//		}
		return representation(Message.LOGOUT_SUCCESS);
	}

	@RequestMapping(value = "profile.json", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject getProfile(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) {
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		List<WechatDto> wechats = userService.listVisibleWechat(user);
		for(WechatDto wechatDto : wechats){
			if(wechatDto.getIsUse() == 1){
				user.setWechatId(wechatDto.getId());
				break;
			}
		}
		UserDto profile = userService.getProfile(getUser(session));
		RoleDto roleDto = roleService.getById(profile.getRoleId(), profile.getCompanyId());
		profile.setFunctionIds(roleDto.getFunctionIds());
		profile.setFunctionCodes(roleDto.getFunctionCodes());
		FunctionModel functionModel = new FunctionModel();
		List<FunctionDto> functionDtos = functionService.search(functionModel);
		profile.setFunctionDtos(functionDtos);
		return representation(Message.LOGOUT_SUCCESS, profile);
	}

	@RequestMapping(value = "list.json", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("system-setting:user-list")
	public JSONObject listUser(
			@RequestBody(required = false) UserModel userModel,
			HttpServletResponse response, HttpSession session) {
		try {
			if (userModel == null) {
				userModel = new UserModel();
			}
			userModel.setCompanyId(getCompanyId(session));
			Page<UserDto> page = userService.search(userModel, true);
			JSONObject json = representation(Message.USER_LIST_SUCCESS, page.getResult(),
					userModel.getPageSize(), userModel.getPageNum(),
					page.getTotal());
			json.put("isSystemRole", getIsSystemRole(session));
			return json;
		} catch (Exception e) {
			log.error(e.getMessage());
			return wrapException(e);
		}
	}

	@RequestMapping(value = "avatar/upload.json", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("system-setting:user-list")
	public JSONObject uploadAvatar(
			@RequestParam(required = false) CommonsMultipartFile file,
			HttpServletResponse response, HttpSession session) {
		try {
			Upload upload = UploadController.upload(file, Constants.IMAGE,
					Constants.USER);
			JSONObject json = new JSONObject();
			json.put("url", upload.getAccessPath());
			return representation(Message.FILE_UPLOAD_SUCCESS, json);
		} catch (Exception e) {
			log.error(e.getMessage());
			return wrapException(e);
		}
	}

	@RequestMapping(value = "wechat/list.json", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions("system-setting:user-list")
	public JSONObject listWechat(HttpServletResponse response,
			HttpSession session) {
		try {
			List<WechatDto> wechats = userService
					.listAvailableWechat(getUser(session));
			return representation(Message.USER_WECHAT_LIST_SUCCESS, wechats);
		} catch (Exception e) {
			log.error(e.getMessage());
			return wrapException(e);
		}
	}
	
	@RequestMapping(value = "role/list.json", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions("system-setting:user-list")
	public JSONObject listRole(HttpServletResponse response,
			HttpSession session) {
		try {
			List<RoleDto> roleDtos = userService
					.listVisibleRole(getCompanyId(session));
			return representation(Message.USER_ROLE_LIST_SUCCESS, roleDtos);
		} catch (Exception e) {
			log.error(e.getMessage());
			return wrapException(e);
		}
	}

	@RequestMapping(value = "new.json", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("system-setting:user-list")
	public JSONObject create(
			@RequestBody(required = false) UserModel userModel,
			HttpSession session) {
		try {
			userService.create(getUser(session), userModel);
			return representation(Message.USER_CREATE_SUCCESS);
		} catch (Exception e) {
			log.error(e.getMessage());
			return wrapException(e);
		}

	}
	
	@RequestMapping(value = "{id}/get.json", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions("system-setting:user-list")
	public JSONObject get(@PathVariable Integer id){
		try {
			UserDto userDto = userService.getById(id);
			return representation(Message.USER_GET_SUCCESS, userDto);
		} catch (Exception e) {
			log.error(e.getMessage());
			return wrapException(e);
		}
	}
	
	@RequestMapping(value = "{id}/update.json", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("system-setting:user-list")
	public JSONObject update(@PathVariable Integer id,
			@RequestBody(required = false) UserModel userModel,
			HttpSession session, HttpServletRequest request,
			HttpServletResponse response){
		try {
			int resultCode = userService.update(id, userModel, getUser(session));
			return representation(Message.USER_UPDATE_SUCCESS, resultCode);
		} catch (Exception e) {
			log.error(e.getMessage());
			return wrapException(e);
		}
		
	}
	
	@RequestMapping(value = "{id}/delete.json", method = RequestMethod.GET)
	@ResponseBody
	@RequiresPermissions("system-setting:user-list")
	public JSONObject delete(@PathVariable Integer id, HttpSession session){
		try {
			int resultCode = userService.deleteById(id, getCompanyId(session));
			return representation(Message.USER_DELETE_SUCCESS, resultCode);
		} catch (Exception e) {
			log.error(e.getMessage());
			return wrapException(e);
		}
	}
	
	@RequestMapping(value = "resetPassword.json", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions("system-setting:user-list")
	public JSONObject updatePwd(@RequestBody(required = false) UserModel userModel,
			HttpSession session, HttpServletRequest request){
		try {
			int resultCode = userService.updatePwd(userModel.getUserId(), userModel.getPassword());
			return representation(Message.USER_UPDATE_PASSWORD_SUCCESS, resultCode);
		} catch (Exception e) {
			log.error(e.getMessage());
			return wrapException(e);
		}
	}
	
	@RequestMapping(value = "change-wechat.json", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject changeWechat(@RequestParam(required = true) Integer wechatId, 
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		try {
			
			wechatService.updateIsUse(getUser(session), wechatId);
			
			User user = (User) SecurityUtils.getSubject().getPrincipal(); 
			user.setWechatId(wechatId);
			
			JSONObject json = new JSONObject();
			json.put("user", user);
			return representation(Message.USER_CHANGE_WECHAT_SUCCESS, json);
		} catch (Exception e) {
			log.error(e.getMessage());
			return wrapException(e);
		}
		
	}
	
	@RequestMapping(value = "init.json", method = RequestMethod.GET)
	public JSONObject init(@RequestParam(required = true) String company,
			@RequestParam(required = true) String wechat,
			HttpServletRequest request, HttpServletResponse response){
		try {
			userService.init(company, wechat);
			return representation(Message.USER_INIT_SUCCESS);
		} catch (Exception e) {
			log.error(e.getMessage());
			return wrapException(e);
		}
		
	}
	

}
