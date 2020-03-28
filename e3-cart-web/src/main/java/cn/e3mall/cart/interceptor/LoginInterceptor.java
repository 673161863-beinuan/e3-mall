package cn.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.config.annotation.Reference;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3mallResult;
import cn.e3mall.entity.TbItem;
import cn.e3mall.entity.TbUser;
import cn.e3mall.sso.service.TokenService;
/**
 * 用户是否登录状态拦截器
 */

public class LoginInterceptor implements HandlerInterceptor {

	//sso系统查询token信息
	@Autowired
	private TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		//在cookie中token
		String token = CookieUtils.getCookieValue(request,"token");
		//判断token是否为空，如果没有token，未登录状态，直接放行
		if(StringUtils.isBlank(token)){
			return true;
		}
		//取到token，需要调用sso系统的服务，根据token取用户信息
		E3mallResult result = tokenService.getUserByToken(token);
		if(result.getStatus() != 200){
			//没有取到用户信息。登录过期，直接放行。
			return true;
		}
		//取到用户信息。登录状态。
		TbUser user = (TbUser) result.getData();
		//把用户信息放到request中。只需要在Controller中判断request中是否包含user信息。放行
		request.setAttribute("user", user);
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {

	}



	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e)
			throws Exception {

	}

}
