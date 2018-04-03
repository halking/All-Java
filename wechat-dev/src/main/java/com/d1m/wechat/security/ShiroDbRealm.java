package com.d1m.wechat.security;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.d1m.wechat.dto.RoleDto;
import com.d1m.wechat.dto.WechatDto;
import com.d1m.wechat.model.User;
import com.d1m.wechat.service.RoleService;
import com.d1m.wechat.service.UserService;

/**
 * 自定义数据库Realm
 * @author d1m
 */
public class ShiroDbRealm extends AuthorizingRealm {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	/**
     *  认证回调函数,登录时调用.
     */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		String username = token.getUsername();
		User user = userService.getByUsername(username);
		if (user != null) {
			// 先要对user判空, 再去查wechat数据, forb
			List<WechatDto> wechats = userService.listVisibleWechat(user);
			if (!wechats.isEmpty()) {
				for(WechatDto wechatDto : wechats){
					if(wechatDto.getIsUse() == 1){
						user.setWechatId(wechatDto.getId());
						break;
					}
				}
				if (user.getWechatId() == null){
					throw new LockedAccountException("wechat isn't binded by user.");
				}
				//user.setWechatId(wechats.get(0).getId());
			}

			// 判断账号是否已删除或锁定
			if (user.getStatus() == 0) {
				throw new LockedAccountException("user is deleted or locked.");
			}
			return new SimpleAuthenticationInfo(user, user.getPassword(), null, getName());
		} else {
			throw new UnknownAccountException("user[" + username + "] not found.");
		}
	}

	/**
     *  给当前用户设置授权
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		User user = (User) principals.fromRealm(getName()).iterator().next();
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		
		RoleDto roleDto = roleService.getById(user.getRoleId(), user.getCompanyId());
		if(roleDto!=null){
			List<String> functions = roleDto.getFunctionCodes();
			if(functions!=null){
				for(String code:functions){
					info.addStringPermission(code);
				}
			}
		}
		return info;
	}
}
